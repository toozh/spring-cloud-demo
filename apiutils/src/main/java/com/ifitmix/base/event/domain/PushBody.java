package com.ifitmix.base.event.domain;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Created by zhangtao on 2017/6/5.
 */
public class PushBody {

    ///app处理类型

    //无操作
    public static final Integer RESPONSE_TYPE_NO_ACTION = 0;
    //启动app
    public static final Integer RESPONSE_TYPE_START_APP = 1;
    //打开网址
    public static final Integer RESPONSE_TYPE_OPEN_LINK = 2;
    //打开指定action
    public static final Integer RESPONSE_TYPE_OPEN_ACTION = 3;
    //更新应用
    public static final Integer RESPONSE_TYPE_UPDATE_APP = 4;

    //通知
    public static final Integer TYPE_NOTIFICATIONS = 1;
    //消息
    public static final Integer TYPE_MESSAGE = 2;

    /// 推送类型

    ///
    /// 系统模块
    public static final int PUSH_TYPE_SYS = 0;
    //运营推送的消息
    public static final int PUSH_TYPE__SYS_3 = 3;
    //训练计划推送
    public static final int PUSH_TYPE_RUN_PLAN = 4;

    ///
    ///赛事模块
    public static final int PUSH_TYPE_ACTIVITY = 1;
    //最近赛事
    public static final int PUSH_TYPE_ACTIVITY_1 = 1;

    ///
    ///俱乐部模块
    public static final int PUSH_TYPE_CLUB = 2;
    //俱乐部留言
    public static final int PUSH_TYPE_CLUB_11 = 11;
    //俱乐部公告
    public static final int PUSH_TYPE_CLUB_12 = 12;

    public static ImmutableMap<Integer, String> PUSH_TITLE_ALERT = ImmutableMap.<Integer, String>builder()
            .put(1, "【{0}】赛事将在{1}进行举办,去参与→→→")
            .put(11, "您在{0}俱乐部中有新消息")
            .put(12, "{0}在{1}俱乐部中发起了【{2}】活动 @全体成员")
            .build();

    /// 消息类型

    //没有内容的消息
    public static final int CONTENT_TYPE_EMPTY = 0;
    //文本
    public static final int CONTENT_TYPE_TEXT = 1;
    //语音
    public static final int CONTENT_TYPE_VOICE = 2;
    //图片
    public static final int CONTENT_TYPE_IMG = 3;
    //链接
    public static final int CONTENT_TYPE_LINK = 4;

    /**
     * 消息内容
     */
    private String content;
    /**
     * 文件链接(网站、语音、图片等)
     */
    private String fileLink;
    /**
     * 消息内容标题
     */
    private String title;
    /**
     * 消息提醒标题
     */
    private String alert;
    /**
     * push 1、通知：发送后会在系统通知栏收到展现，同时响铃或振动提醒用户。
     * push 2、消息：发送后不会在系统通知栏展现，SDK将消息传给第三方应用后需要开发者写展现代码才能看到。
     */
    private Integer type;
    /**
     * 1、文本
     * 2、语音
     * 3、图片
     * 4、链接
     * 5、短视频
     * 6、图文 TODO 不支持图文，暂时没想好图文数据怎么处理
     */
    private Integer contentType;
    /**
     * push类型
     * 0-10 平台消息推送(0-版本升级、1-最新赛事、2-音乐推荐、3-运营推送的消息)
     * 11-20、俱乐部消息(11-俱乐部留言、12-俱乐部公告)
     * 21-30、个人消息 (A-B)
     * 31-40、数据系统消息
     */
    private Integer pushType;
    /**
     * 响应类型
     * 0、无操作
     * 1、启动app
     * 2、打开网址
     * 3、打开指定activity
     * 4、更新应用
     */
    private Integer responseType;
    /**
     * 消息在客户端创建时间戳
     */
    private long bornTimestamp;
    /**
     * 其他信息
     *
     * 俱乐部id clubId
     * 赛事id activityId
     * 个人id uid
     */
    private Map<String, Object> otherInfo;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getContent() {
        return content;
    }

    public Integer getResponseType() {
        return responseType;
    }

    public void setResponseType(Integer responseType) {
        this.responseType = responseType;
    }

    public Integer getPushType() {
        return pushType;
    }

    public void setPushType(Integer pushType) {
        this.pushType = pushType;
    }

    public long getBornTimestamp() {
        return bornTimestamp;
    }

    public void setBornTimestamp(long bornTimestamp) {
        this.bornTimestamp = bornTimestamp;
    }

    public Integer getContentType() {
        return contentType;
    }

    public void setContentType(Integer contentType) {
        this.contentType = contentType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Map<String, Object> getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(Map<String, Object> otherInfo) {
        this.otherInfo = otherInfo;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

}
