package com.ifitmix.club.domain;

import com.ifitmix.common.mongo.GeneratedValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by edward on 2016/8/9.
 * <p>
 * 通知处理 记录
 * <p>
 * TODO MessageTaskRecord 表改为 NoticeHandleRecord
 */
@Document(collection = "NoticeHandleRecord")
public class NoticeHandleRecord {

    /**
     * 处理的消息嘛 --- 空的设备类型
     */
    public static final String HANDLE_CODE_ERROR_1 = "-1";
    /**
     * 处理成功
     */
    public static final String HANDLE_CODE_ERROR_SUCCESS = "0";

    /**
     * 未发送
     */
    public static final Integer SEND_STATUS_0 = 0;
    /**
     * 已发送
     */
    public static final Integer SEND_STATUS_1 = 1;
    /**
     * 已经阅读
     */
    public static final Integer SEND_STATUS_2 = 2;

    @Id
    @GeneratedValue
    private Long id;

    /**
     * 事件Id
     * {@link com.ifitmix.base.event.domain.BaseEvent#id}
     */
    private String eventId;
    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 消息业务类型
     * 短信、邮件、push
     */
    private Integer noticeType;
    /**
     * 处理状态
     * 0 未处理
     * 1 已经处理
     * 2 用户已经阅读消息、已经完成
     */
    private Integer handleStatus;
    /**
     * 接收推送消息的用户
     * {userId,mailId,mobileId等}
     */
    private String toTargetId;
    /**
     * 消息编号
     */
    private Long noticeId;
    /**
     * MQ处理时间
     */
    private Long handleTime;
    /**
     * 阅读消息时间,完成时间
     * (说明:简单的消息查看时间为消息发送成功时间)
     */
    private Long successTime;

    /// 消息推送
    /**
     * 处理次数
     */
    private Integer handleNum;
    /**
     * 处理的结果消息码(参考友盟消息码)
     */
    private String handleCode;

    public Integer getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(Integer noticeType) {
        this.noticeType = noticeType;
    }

    public Integer getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(Integer handleStatus) {
        this.handleStatus = handleStatus;
    }

    public String getToTargetId() {
        return toTargetId;
    }

    public void setToTargetId(String toTargetId) {
        this.toTargetId = toTargetId;
    }

    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    public Long getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Long handleTime) {
        this.handleTime = handleTime;
    }

    public Long getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(Long successTime) {
        this.successTime = successTime;
    }

    public Integer getHandleNum() {
        return handleNum;
    }

    public void setHandleNum(Integer handleNum) {
        this.handleNum = handleNum;
    }

    public String getHandleCode() {
        return handleCode;
    }

    public void setHandleCode(String handleCode) {
        this.handleCode = handleCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
