package com.ifitmix.common.utils;

import com.alibaba.fastjson.JSON;
import com.tencent.xinge.Message;
import com.tencent.xinge.MessageIOS;
import com.tencent.xinge.Style;
import com.tencent.xinge.XingeApp;
import org.apache.commons.collections.map.HashedMap;
import org.json.JSONObject;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * Created by zhangtao on 2017/6/2.
 *
 * 信鸽推送
 *
 */
public class XingeUtils {

    private static Long ACCESS_ID_ANDROID = 2100250590L;

    private static String SECRET_KEY_ANDROID = "749d53fc2a2a7611a477b5cb663444fd";

    private static Long ACCESS_ID_IOS = 2200250591L;

    private static String SECRET_KEY_IOS = "dde31e9a4c2489e4955d274dc017dae8";

    private static XingeApp xingeAppAndroid;

    private static XingeApp xingeAppIOS;

    private static XingeUtils instance;

    private XingeUtils() {
        xingeAppAndroid = new XingeApp(ACCESS_ID_ANDROID, SECRET_KEY_ANDROID);
        xingeAppIOS = new XingeApp(ACCESS_ID_IOS, SECRET_KEY_IOS);
    }

    public static synchronized XingeUtils getInstance() {
        if (instance == null) {
            instance = new XingeUtils();
        }
        return instance;
    }

    public String pushSingleDeviceIOS(String deviceToken, String alert, Map<String, Object> custom) {
        MessageIOS messageIOS = new MessageIOS();

        messageIOS.setCustom(custom);
        messageIOS.setAlert(alert);
        messageIOS.setExpireTime(86400);

        JSONObject result = xingeAppIOS.pushSingleDevice(deviceToken, messageIOS, XingeApp.IOSENV_DEV);
        return JSON.toJSONString(result);
    }

    public String pushSingleDeviceAndroid(String deviceToken, String title, String content, Map<String, Object> custom) {
        Message message = new Message();
        message.setTitle(title);
        message.setContent(content);
        message.setType(Message.TYPE_NOTIFICATION);
        message.setStyle(new Style(0, 0, 0, 1, 1, 1, 0, 1));


        message.setCustom(custom);
        message.setExpireTime(86400);

        JSONObject result = xingeAppAndroid.pushSingleDevice(deviceToken, message);
        return result.toString();
    }


}
