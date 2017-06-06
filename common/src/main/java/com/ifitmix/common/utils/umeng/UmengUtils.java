package com.ifitmix.common.utils.umeng;

import com.alibaba.fastjson.JSON;
import com.ifitmix.common.utils.XingeUtils;
import com.ifitmix.common.utils.umeng.android.AndroidUnicast;
import com.ifitmix.common.utils.umeng.ios.IOSUnicast;
import com.ifitmix.utils.StringUtil;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by zhangtao on 2017/6/5.
 * <p>
 * 友盟推送
 */
public class UmengUtils {

    protected static Logger logger = LoggerFactory.getLogger(UmengUtils.class);

    private static final String APP_KEY = "5528bab4fd98c5ca0a000cab";

    private static final String APP_SECRET = "unad3r8gezudowkqmbvrrhtjzbwewbrp";

    private static final String APP_KEY_ANDROID = "5528b9b8fd98c5e67a001ae9";

    private static final String APP_SECRET_ANDROID = "ltainhihcwh5xkvj0hjuft2tszddce2j";

    private static AndroidUnicast androidUnicast;

    private static IOSUnicast iosUnicast;

    private static UmengUtils instance;

    private static UmengClient client;

    private UmengUtils() {
        try {
            client = new UmengClient();
            iosUnicast = new IOSUnicast(APP_KEY, APP_SECRET);
            androidUnicast = new AndroidUnicast(APP_KEY_ANDROID, APP_SECRET_ANDROID);

            /*if (ApplicationConfig.SERVER) {// 不同环境初始化推送模式
                iosUnicast.setProductionMode();
                androidUnicast.setProductionMode();
            } else {
                iosUnicast.setTestMode();
                androidUnicast.goAppAfterOpen();
                androidUnicast.setTestMode();
            }*/
            iosUnicast.setTestMode();
            androidUnicast.goAppAfterOpen();
            androidUnicast.setTestMode();

        } catch (Exception e) {
            logger.info("init push error.. {} ", e);
        }
    }

    public static synchronized UmengUtils getInstance() {
        if (instance == null) {
            instance = new UmengUtils();
        }
        return instance;
    }


    public int iosUnicast(String token, String alert, int badge, Map<String, Object> customizedField) throws Exception {
        IOSUnicast unicast = iosUnicast;
        unicast.setDeviceToken(token);
        unicast.setAlert(alert);
        unicast.setBadge(badge);
        unicast.setSound("default");

        Map<String, String> custom = new HashedMap();
        if(customizedField != null) {
            for(String key : customizedField.keySet()) {
                custom.put(key, JSON.toJSONString(customizedField.get(key)));
            }
        }

        unicast.setFields(custom);
        return client.send(unicast);
    }

    public int androidUnicast(String token, String title, String context, Map<String, Object> customizedField) throws Exception {
        AndroidUnicast unicast = androidUnicast;
        unicast.setDeviceToken(token);
        unicast.setTicker("Android unicast ticker");
        unicast.setTitle(title);
        if (StringUtil.isEmpty(context)) {
            context = "空值";
        }
        unicast.setText(context);
        unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);

        Map<String, String> custom = new HashedMap();
        if(customizedField != null) {
            for(String key : customizedField.keySet()) {
                custom.put(key, JSON.toJSONString(customizedField.get(key)));
            }
        }

        unicast.setFields(custom);
        client.send(unicast);
        return 0;
    }


}
