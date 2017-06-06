package com.ifitmix.common.event;

import com.ifitmix.base.constants.FailureInfo;
import com.ifitmix.base.event.domain.AskEvent;
import com.ifitmix.base.event.domain.BaseEvent;
import com.ifitmix.common.event.domain.AskRequestEventPublish;
import com.ifitmix.common.exception.EventException;
import com.ifitmix.utils.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by zhangtao on 2017/4/21.
 */
public class AskEventCallback {

    private static Logger logger = LoggerFactory.getLogger(AskEventCallback.class);

    private final String callbackClassName;

    private final Class<?> callbackClass;

    private final Method successMethod;

    private final List<Parameter> successParameters;

    private final Optional<Method> failureMethod;

    private final List<Parameter> failureParameters;

    public AskEventCallback(String callbackClassName, Class<?> callbackClass, Method successMethod, Optional<Method> failureMethod,
                            List<Parameter> successParameters, List<Parameter> failureParameters) {
        this.callbackClassName = callbackClassName;
        this.callbackClass = callbackClass;
        this.successMethod = successMethod;
        this.failureMethod = failureMethod;
        this.successParameters = successParameters;
        this.failureParameters = failureParameters;
    }

    /**
     *
     * @param callbackClassName
     * @return
     * @throws Exception
     */
    public static AskEventCallback createCallback(String callbackClassName) throws Exception {
        Class<?> callbackClass = Class.forName(callbackClassName);

        List<Method> methods = Arrays.asList(callbackClass.getMethods());
        Optional<Method> successMethodOptional = getCallbackMethod(callbackClassName, methods, true);
        Optional<Method> failureMethodOptional = getCallbackMethod(callbackClassName, methods, false);
        if(!successMethodOptional.isPresent()) {
            throw new EventException(String.format("回调类%s中没有%s方法", callbackClassName, EventUtils.SUCCESS_CALLBACK_NAME));
        }

        List<Parameter> successParameters = Arrays.asList(successMethodOptional.get().getParameters());
        checkCallbackParameters(callbackClassName, successParameters);

        List<Parameter> failureParameters = new ArrayList<>();
        if(failureMethodOptional.isPresent()) {
            failureParameters = Arrays.asList(failureMethodOptional.get().getParameters());
            checkCallbackParameters(callbackClassName, failureParameters);
        }

        return new AskEventCallback(callbackClassName, callbackClass, successMethodOptional.get(), failureMethodOptional,
                successParameters, failureParameters);
    }

    /**
     * 检查 回调方法参数名称
     * @param callbackClassName
     * @param parameters
     */
    private static void checkCallbackParameters(String callbackClassName, List<Parameter> parameters) {
        parameters.stream().map(Parameter::getType).forEach(parameterType -> {
            if(!BaseEvent.class.isAssignableFrom(parameterType) && !parameterType.equals(FailureInfo.class)
                    && !parameterType.equals(String.class)) {
                throw new EventException(String.format("回调方法参数类型必须是String, " +
                        "FailureInfo或者BaseEvent的子类，实际类型：%s，类名：%s",
                        parameterType, callbackClassName));
            }
        });

    }

    /**
     * 查找回调成功或者失败的方法
     * @param callbackClassName
     * @param methods
     * @param success
     * @return
     */
    private static Optional<Method> getCallbackMethod(String callbackClassName, List<Method> methods, boolean success) throws Exception {
        String methodName = EventUtils.getAskCallbackMethodName(success);
        List<Method> targetMethods = methods.stream().filter(method -> methodName.equals(method.getName())).collect(Collectors.toList());
        if(targetMethods.size() > 1) {
            throw new EventException(String.format("回调类%s有%d个%s方法，应该只能有1个", callbackClassName, targetMethods.size(),
                    methodName));
        }
        return targetMethods.isEmpty() ? Optional.empty() : Optional.of(targetMethods.get(0));
    }

    /**
     * 执行回调函数
     * @param eventRegistry
     * @param success
     * @param askEvents
     * @param extraParams
     * @param failureInfo
     */
    public void call(EventRegistry eventRegistry, boolean success, List<AskRequestEventPublish> askEvents, String extraParams, FailureInfo failureInfo) {
        if(!success && !failureMethod.isPresent()) {
            return;
        }
        if(StringUtils.isBlank(extraParams)) {
            extraParams = "{}";
        }
        final Map<String, String> extraParamsMap = JsonUtils.json2Object(extraParams, Map.class);

        List<Parameter> parameters = success ? successParameters : failureParameters;
        Method method = success ? successMethod : failureMethod.get();

        Map<Class<?>, BaseEvent> askEventMap = askEvents.stream()
                .map(x -> eventRegistry.deserializeEvent(x.getEventType(), x.getPayload()))
                .collect(Collectors.toMap(x -> x.getClass(), Function.identity()));

        List<Object> invokeMethodParameters = parameters.stream().map(p -> {
            Class<?> parameterType = p.getType();
            if(BaseEvent.class.isAssignableFrom(parameterType)) {
                return askEventMap.get(parameterType);
            } else if(parameterType.equals(FailureInfo.class)) {
                return failureInfo;
            } else if(parameterType.equals(String.class)) {
                LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
                String[] parameterNames = u.getParameterNames(method);
                return extraParamsMap.get(parameterNames[Integer.parseInt(p.getName().replaceAll("arg", ""))]);
            } else {
                throw new EventException(String.format("回调方法参数类型必须是String, FailureInfo 或者 BaseEvent 的子类，" +
                        "实际类型：%s, 类名：%s", parameterType, callbackClassName));
            }
        }).collect(Collectors.toList());

        try {
            if(logger.isDebugEnabled()) {
                logger.debug(String.format("invoke callback: %s, method: %s, params: %s", callbackClassName, EventUtils.getAskCallbackMethodName(success),
                        invokeMethodParameters));
            }
            method.invoke(callbackClass.newInstance(), invokeMethodParameters.toArray());
        } catch (IllegalAccessException | InstantiationException e) {
            throw new EventException(e);
        } catch (InvocationTargetException e) {
            if(e.getTargetException() instanceof EventException) {
                throw (EventException)e.getTargetException();
            } else {
                throw new EventException(e.getTargetException());
            }
        }
    }

    /**
     * 校验回调方法参数和实际传的值是否匹配
     * @param united
     * @param askEvents
     */
    public void checkMethodParameter(boolean united, List<? extends AskEvent> askEvents) {

        if(!united && askEvents.size() != 1) {
            throw new EventException("ask请求不是united但是askEvent数量不等于1");
        } else if(united && askEvents.size() <= 1){
            throw new EventException("ask请求是united但是askEvent数量小于等于1");
        }

        checkParameterType(true);
        checkParameterType(false);

        checkAskEventParameter(true, askEvents);
        checkAskEventParameter(false, askEvents);
    }

    /**
     * 校验方法参数类型
     * @param success
     */
    private void checkParameterType(boolean success) {
        List<Parameter> parameters = success ? successParameters : failureParameters;

        boolean allParameterValid;
        allParameterValid = parameters.stream()
                .map(Parameter::getType)
                .allMatch(clazz -> BaseEvent.class.isAssignableFrom(clazz) || clazz.equals(String.class)
                        || clazz.equals(FailureInfo.class));
        if(!allParameterValid) {
            throw new EventException(String.format("回调类%s的%s方法参数类型必须是String, FailureInfo或者BaseEvent的子类",
                    callbackClassName, EventUtils.getAskCallbackMethodName(success)));
        }

    }

    /**
     * 校验方法askEvent参数声明与实际是否匹配
     * @param success
     * @param askEvents
     */
    private void checkAskEventParameter(boolean success, List<? extends AskEvent> askEvents) {

        List<Parameter> parameters = success ? successParameters : failureParameters;

        Set<Class<?>> methodParameterOfEventClass = parameters.stream()
                .map(Parameter::getType)
                .filter(BaseEvent.class::isAssignableFrom)
                .collect(Collectors.toSet());

        List<? extends Class<? extends AskEvent>> askEventClassList =
                askEvents.stream().map(x -> x.getClass()).collect(Collectors.toList());

        boolean allParameterValid = askEventClassList.stream()
                .allMatch(eventClass -> methodParameterOfEventClass.stream()
                        .anyMatch(parameterClass -> parameterClass.isAssignableFrom(eventClass)));

        if(!allParameterValid) {
            throw new EventException(String.format("回调类%s的%s方法参数不匹配, 方法声明: %s, 实际参数: %s",
                    callbackClassName, EventUtils.getAskCallbackMethodName(success),
                    methodParameterOfEventClass, askEventClassList));
        }
    }

}
