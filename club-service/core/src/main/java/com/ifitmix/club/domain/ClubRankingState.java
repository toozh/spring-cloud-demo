package com.ifitmix.club.domain;

import com.ifitmix.common.mongo.GeneratedValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * 俱乐部排行榜
 *
 * <p>
 *     分用户运动数据排行
 * </p>
 */
@Document(collection = "ClubRankingState")
public class ClubRankingState {

    /**
     * 用户类型/日
     */
    public static final int TYPE_DAY = 1;
    /**
     * 用户类型/周
     */
    public static final int TYPE_WEEK = 2;
    /**
     * 用户类型/月
     */
    public static final int TYPE_MONTH = 3;

    @Id
    @GeneratedValue
    private Long id;

    /**
     * 俱乐部编号
     */
    private Long clubId;
    /**
     * 统计类型 1、周 2、月
     */
    private Integer type;
    /**
     * 添加时间
     */
    private Long addTime;

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

    public Long getClubId() {
        return clubId;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
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
}
