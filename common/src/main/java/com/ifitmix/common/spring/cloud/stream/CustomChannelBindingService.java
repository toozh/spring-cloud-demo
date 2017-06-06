package com.ifitmix.common.spring.cloud.stream;

import com.ifitmix.base.constants.EventType;
import com.ifitmix.common.event.EventRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.cloud.stream.binder.*;
import org.springframework.cloud.stream.binding.BindingService;
import org.springframework.cloud.stream.config.BindingServiceProperties;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.MessageChannel;
import org.springframework.validation.beanvalidation.CustomValidatorBean;
import com.google.common.base.Stopwatch;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 1. 重新定义bindConsumer，因为在启动的时候需要监听事件的都调用了EventRegistry.register
 * 所有EventRegistry已经包含了所有的刚兴趣的事件类型（即topic），在这里就将所有的刚兴趣的topic注册到Processor.INPUT这个 channel
 * <p>
 * 2. 重新定义bindProducer, 因为output channel 全都是根据事件名称动态创建的，他们的配置全部都沿用Processor.OUTPUT 这个channel 的配置
 * <p>
 * <p>
 * Created by zhangtao on 2017/4/24.
 */
public class CustomChannelBindingService extends BindingService {

    private static Logger log = LoggerFactory.getLogger(CustomChannelBindingService.class);

    private final CustomValidatorBean validatorBean;

    private BinderFactory binderFactory;

    private final BindingServiceProperties bindingServiceProperties;

    private final Map<String, List<Binding<MessageChannel>>> consumerBindings = new HashMap<>();

    private final Map<String, Binding<MessageChannel>> producerBindings = new HashMap<>();

    private final EventRegistry eventRegistry;


    public CustomChannelBindingService(BindingServiceProperties bindingServiceProperties, BinderFactory binderFactory,
                                       EventRegistry eventRegistry) {
        super(bindingServiceProperties, binderFactory);

        this.bindingServiceProperties = bindingServiceProperties;
        this.binderFactory = binderFactory;
        this.eventRegistry = eventRegistry;
        this.validatorBean = new CustomValidatorBean();
        this.validatorBean.afterPropertiesSet();

    }


    @Override
    public <T> Collection<Binding<T>> bindConsumer(T input, String inputName) {
        Set<EventType> eventTypeSet = eventRegistry.getInterestedEventTypes();
        String[] channelBindingTargets = eventTypeSet.stream().map(EventType::name).collect(Collectors.toList()).toArray(new String[eventTypeSet.size()]);
        if (log.isInfoEnabled()) {
            log.info("spring kafka consumer bind to these topics: " + Arrays.toString(channelBindingTargets));
        }
        List<Binding<T>> bindings = new ArrayList<>();
        Binder<MessageChannel, ConsumerProperties, ?> binder = (Binder<MessageChannel, ConsumerProperties, ?>) getBinderForChannel(inputName);
        ConsumerProperties consumerProperties = this.bindingServiceProperties.getConsumerProperties(inputName);

        if(binder instanceof ExtendedPropertiesBinder) {
            ExtendedPropertiesBinder extendedPropertiesBinder = (ExtendedPropertiesBinder) binder;
            Object extension = extendedPropertiesBinder.getExtendedConsumerProperties(inputName);
            ExtendedConsumerProperties extendedConsumerProperties = new ExtendedConsumerProperties(extension);
            BeanUtils.copyProperties(consumerProperties, extendedConsumerProperties);
            consumerProperties = extendedConsumerProperties;
        }
        validate(consumerProperties);

        for(String target : channelBindingTargets) {
            Binding<T> binding = (Binding<T>) binder.bindConsumer(target, bindingServiceProperties.getGroup(inputName), (MessageChannel) input, consumerProperties);
            bindings.add(binding);
        }
        List<Binding<MessageChannel>> bindingList = new ArrayList<>();
        bindings.parallelStream().forEach((binding) -> bindingList.add((Binding<MessageChannel>) binding));
        this.consumerBindings.put(inputName, bindingList);
        return bindings;
    }


    @Override
    public <T> Binding<T> bindProducer(T output, String outputName) {
        String channelBindingTarget = this.bindingServiceProperties.getBindingDestination(outputName);
        Binder<MessageChannel, ?, ProducerProperties> binder = (Binder<MessageChannel, ?, ProducerProperties>) getBinderForChannel(outputName);
        // 统一使用OUTPUT 的配置
        String channelNameForProperties = Processor.OUTPUT;
        ProducerProperties producerProperties = this.bindingServiceProperties.getProducerProperties(channelNameForProperties);
        if(binder instanceof ExtendedPropertiesBinder) {
            Object extension = ((ExtendedPropertiesBinder) binder).getExtendedProducerProperties(channelNameForProperties);
            ExtendedProducerProperties extendedProducerProperties = new ExtendedProducerProperties<>(extension);
            BeanUtils.copyProperties(producerProperties, extendedProducerProperties);
            producerProperties = extendedProducerProperties;
        }
        validate(producerProperties);

        Stopwatch stopWatch = null;
        if(log.isDebugEnabled()) {
            stopWatch = Stopwatch.createStarted();
        }

        Binding<T> binding = (Binding<T>) binder.bindProducer(channelBindingTarget, (MessageChannel) output, producerProperties);

        if(log.isDebugEnabled() && stopWatch != null) {
            stopWatch.stop();
            log.debug(String.format("bind kafka producer [%s] cost %d ms", outputName, stopWatch.elapsed(TimeUnit.MILLISECONDS)));
        }

        this.producerBindings.put(outputName, (Binding<MessageChannel>) binding);

        return binding;
    }

    private Binder<MessageChannel, ?, ?> getBinderForChannel(String channelName) {
        String transport = this.bindingServiceProperties.getBinder(channelName);
        return binderFactory.getBinder(transport, MessageChannel.class);
    }

    private void validate(Object properties) {
        RelaxedDataBinder dataBinder = new RelaxedDataBinder(properties);
        dataBinder.setValidator(validatorBean);
        dataBinder.validate();
        if(dataBinder.getBindingResult().hasErrors()) {
            throw new IllegalStateException(dataBinder.getBindingResult().toString());
        }
    }


}
