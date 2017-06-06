package com.ifitmix.club.domain;

import com.ifitmix.common.mongo.GeneratedValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by sin on 2015/11/19.
 * 俱乐部成员
 */
@Document(collection = "ClubMember")
public class ClubMember {

    /**
     * 状态/正常
     */
    public static final Integer STATUS_NORMAL = 0;
    /**
     * 状态/无效
     */
    public static final Integer STATUS_INVALID = 1;
    /**
     * 类型/创建者
     */
    public static final Integer TYPE_CREATOR = 0;
    /**.
     *
     * 类型/成员
     */
    public static final Integer TYPE_MEMBER = 1;

    @Id
    @GeneratedValue
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
}
