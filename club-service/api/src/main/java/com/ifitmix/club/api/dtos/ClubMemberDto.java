package com.ifitmix.club.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ifitmix.user.api.dtos.UserDto;

/**
 * Created by zhangtao on 2017/5/18.
 */
public class ClubMemberDto {

    private Long id;

    /**
     * 用户编号
     */
    private Integer uid;
    /**
     * 俱乐部编号
     */
    private Long clubId;
    /**
     * 成员名称
     */
    private String name;
    /**
     * 0、创建者  1、成员
     */
    private Integer type;
    /**
     * 状态
     *  0、正常 1、无效
     */
    private Integer status;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
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
