package com.ifitmix.common.event;

import com.ifitmix.base.constants.EventType;
import com.ifitmix.base.constants.FailureReason;
import com.ifitmix.base.event.domain.BaseEvent;
import com.ifitmix.base.event.domain.NotifyEvent;
import com.ifitmix.common.event.constant.AskEventStatus;
import com.ifitmix.common.exception.EventException;
import com.ifitmix.utils.JsonUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * Created by zhangtao on 2017/4/21.
 */
public class EventUtils {

    /**
     * 成功回调 函数名称
     */
    public static final String SUCCESS_CALLBACK_NAME = "onSuccess";
    /**
     * 失败回调 函数名称
     */
    public static final String FAILED_CALLBACK_NAME = "onFailure";

    /**
     * 获取 回调方法
     * @param success
     * @return
     */
    public static String getAskCallbackMethodName(boolean success) {
        return success ? SUCCESS_CALLBACK_NAME : FAILED_CALLBACK_NAME;
    }

    /**
     * 对象转成 json
     * @param baseEvent
     * @return
     */
    public static String serializeEvent(BaseEvent baseEvent) {
        return JsonUtils.object2Json(baseEvent);
    }

    /**
     * 将接收到的 对象转换成 map
     * @param payload
     * @return
     */
    public static Map<String, Object> retrieveEventMapFromJson(String payload) {
        Map<String, Object> map = JsonUtils.json2Object(payload, Map.class);
        String type = (String) map.get("type");
        if(StringUtils.isBlank(type)) {
            throw new EventException(String.format("event type is blank, payload: %s", payload));
        }
        if(EventType.valueOfIgnoreCase(type) == null) {
            throw new EventException(String.format("unknown event type: %s, payload: %s", type, payload));
        }
        if(map.get("id") == null) {
            throw new EventException(String.format("event id is blank, payload: %s", payload));
        }
        return map;
    }

    /**
     * 将 json 专成事件对象
     * @param payload
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends BaseEvent> T deserializeEvent(String payload, Class<T> clazz) {
        return JsonUtils.json2Object(payload, clazz);
    }

    public static FailureReason fromAskEventStatus(AskEventStatus askEventStatus) {
        switch (askEventStatus) {
            case CANCELLED:
                return FailureReason.CANCELLED;
            case FAILED:
                return FailureReason.FAILED;
            case TIMEOUT:
                return FailureReason.TIMEOUT;
            default:
                throw new EventException("unknown FailureReason from AskEventStatus: " + askEventStatus);
        }
    }
}
