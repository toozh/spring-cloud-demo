package com.ifitmix.club.api.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ifitmix.user.api.dtos.UserDto;

/**
 *
 * 俱乐部活动
 *
 * <p>
 *     俱乐部创建者，可以为此添加不定期活动
 *     活动名称， 和时间等消息
 *     <strong>
 *         俱乐部活动，不错任何其他操作
 *         只发布 ：活动消息 和 地点 活动时间 和 结束时间
 *     </strong>
 * </p>
 *
 */
public class ClubNoticeDto {

    private Long id;

    /**
     * 创建人
     */
    private Integer uid;
    /**
     * 俱乐部编号
     */
    private Long clubId;
    /**
     * 名称
     */
    private String name;
    /**
     * 内容
     */
    private String content;
    /**
     * 活动地址
     */
    private String address;
    /**
     * 活动开始时间
     */
    private Long beginTime;
    /**
     * 活动结束时间
     */
    private Long endTime;
    /**
     * 描述
     */
    private String desc;
    /**
     * 背景图
     */
    private String backImageUrl;
    /**
     * 修改次数
     */
    private Integer modifyCount;
    /**
     * 添加时间
     */
    private Long addTime;
    /**
     * 状态
     */
    private Integer status;

    private UserDto user;

    ///
    ///.

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBackImageUrl() {
        return backImageUrl;
    }

    public void setBackImageUrl(String backImageUrl) {
        this.backImageUrl = backImageUrl;
    }

    public Integer getModifyCount() {
        return modifyCount;
    }

    public void setModifyCount(Integer modifyCount) {
        this.modifyCount = modifyCount;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
