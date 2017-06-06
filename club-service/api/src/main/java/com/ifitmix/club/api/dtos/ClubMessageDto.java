package com.ifitmix.club.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ifitmix.common.mongo.GeneratedValue;
import com.ifitmix.user.api.dtos.UserDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 俱乐部消息
 * <p>
 *     在此 是留言板
 * </p>
 */
public class ClubMessageDto {

    private Long id;

    /**
     * 留言类型
     * 1、文本
     * 2、语音
     * 3、图片
     */
    private Integer msgType;
    /**
     * 用户编号（发送者 | 留言者）
     */
    private Integer uid;
    /**
     * 俱乐部编号
     */
    private Long clubId;
    /**
     * 内容
     */
    private String content;
    /**
     * 文件链接
     */
    private String fileLink;
    /**
     * 添加时间
     */
    private Long addTime;

    private UserDto user;

    ///
    /// 非存储字段

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Long getClubId() {
        return clubId;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty(value = "user")
    public UserDto getUserDto() {
        return user;
    }

    public void setUserDto(UserDto userDto) {
        this.user = userDto;
    }
}
