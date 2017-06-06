package com.ifitmix.club.domain;

import com.ifitmix.common.mongo.GeneratedValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 俱乐部 运动数据汇总
 * <p>
 *     汇总可分为 周 和 月
 *     <strong>
 *         用户的活跃根据 俱乐部活跃来统计影响,
 *
 *         注意：
 *              1、周 和 月不采用 定时器调度来统计信息
 *              2、根据每次添加运动记录来计算
 *              3、本类分为 两种数据 周 和 月的数据，每周 或 每月 的第一条数据 添加本周新数据， 一个月内 或一周内 ，累加数据。
 *     </strong>
 * </p>
 */
@Document(collection = "ClubRanking")
public class ClubRanking {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * 俱乐部编号
     */
    private Long clubId;

    ///
    /// 运动数据

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

    ///
    /// 其他字段

    /**
     * 类型：1、日 2、周 3、月 {@link ClubUserRunStat#type}
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
    /// 非存储字段



    public Long getClubId() {
        return clubId;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
