package com.ifitmix.club.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ifitmix.common.mongo.GeneratedValue;
import com.ifitmix.user.api.dtos.UserDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * 俱乐部 用户运动 统计
 *
 * 统计分为：日、周、月
 *
 * <p>
 *     统计个人运动数据
 * </p>
 */
public class ClubUserRunStatDto {

    private Long id;

    /**
     * 用户编号
     */
    private Integer uid;
    /**
     * 统计类型 1、日 2、周 3、月
     */
    private Integer type;
    /**
     * 添加时间
     */
    private Long addTime;
    /**
     * 添加时间 Str
     */
    private String addTimeStr;

    ///
    /// 数据字段

    /**
     * 运动时间（秒） 总
     */
    private Long runTime;
    /**
     * 距离（里程）m 总
     */
    private Long distance;
    /**
     * 卡路里 总
     */
    private Long calorie;
    /**
     * 步数 总
     */
    private Long step;

    private UserDto user;
    ///
    /// 非存储字段



    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public String getAddTimeStr() {
        return addTimeStr;
    }

    public void setAddTimeStr(String addTimeStr) {
        this.addTimeStr = addTimeStr;
    }

    public Long getRunTime() {
        return runTime;
    }

    public void setRunTime(Long runTime) {
        this.runTime = runTime;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public Long getCalorie() {
        return calorie;
    }

    public void setCalorie(Long calorie) {
        this.calorie = calorie;
    }

    public Long getStep() {
        return step;
    }

    public void setStep(Long step) {
        this.step = step;
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
