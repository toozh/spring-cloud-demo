package com.ifitmix.club.domain;

import com.ifitmix.common.mongo.GeneratedValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 俱乐部动态
 * <p>
 *     动态指 ： 俱乐部成员运动
 *     俱乐部活跃度：按成员每天活跃比例
 *
 *     <strong>
 *         活跃度会影响：用户排行版 {@link ClubRanking}
 *     </strong>
 * </p>
 */
@Document(collection = "ClubDynamic")
public class ClubDynamic {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * 俱乐部 {@link Club#id}
     */
    private Long clubId;
    /**
     * 用户编号
     */
    private Integer uid;
    /**
     * 运动编号
     */
    private Long runId;

    ///
    /// 数据字段

    /**
     * 运动时间（秒）
     */
    private Long runTime;
    /**
     * 距离（里程）m
     */
    private Long distance;
    /**
     * 卡路里
     */
    private Long calorie;
    /**
     * 跑步模式，1、室外 2、室内
     */
    private Integer model;
    /**
     * 添加时间
     */
    private Long addTime;

    ///
    /// 非存储字段


    public Long getClubId() {
        return clubId;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
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

    public Integer getModel() {
        return model;
    }

    public void setModel(Integer model) {
        this.model = model;
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
}
