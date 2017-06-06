package com.ifitmix.sports.api.dtos;

import com.ifitmix.user.api.dtos.UserDto;

import javax.validation.constraints.NotNull;

/**
 * Created by zhangtao on 2017/3/9.
 */
public class UserRunDto {

    private Long id;
    /**
     *
     */
    private Integer uid;
    /**
     * 运动时间 (秒)
     */
    private Long runTime;
    /**
     * 运动开始时间
     */
    private Long startTime;
    /**
     * 运动结束时间
     */
    private Long endTime;
    /**
     * 运动(m)
     */
    private Long distance;
    /**
     * 步(多少步)
     */
    private Long step;
    /**
     * 卡路里
     */
    private Long calorie;
    /**
     * 燃脂量(运动消耗的脂肪)
     */
    private Double consumeFat;
    /**
     * 起始 经度
     */
    private Double startLng;
    /*
     * 起始 纬度
     */
    private Double startLat;
    /**
     * 结束 经度
     */
    private Double endLng;
    /*
     * 结束 纬度
     */
    private Double endLat;
    /**
     * 运动相关文件
     */
    private String zipUrl;
    /**
     * 运动详细（存的是本地文件，会上传文件服务器）
     */
    private String detail;
    /**
     * 步数详细
     */
    private String stepDetail;
    /**
     * mix BPM值
     */
    private Integer bpm;
    /**
     * 状态 0 无效 1 有效
     */
    private Integer state;
    /**
     * 运动更新时间 （暂时只有 用户删除历史运动记录时使用）
     */
    private Long updateTime;
    /**
     * 运动类型(0,1 跑步、2跳绳之类的)
     */
    private Integer type;
    /**
     * 定位类型，（1、GPRS， 2、流量）
     */
    private Integer locationType;
    /**
     * 跑步模式，1、室外 2、室内
     */
    private Integer model;
    /**
     * bpm 匹
     * 度，（计算后的 匹配度，因为会计算 大于 100% 的用户， 如 ： 130% = 130 % 100 bomMatch = 30% ） （跑步完后，计算的 匹配值）
     */
    private Double bpmMatch;
    /**
     * bpm 匹配度，(用户实际的匹配度) （跑步完后，计算的 匹配值）
     */
    private Double userBpmMatch;
    /**
     * 用户打分， 超越多少用户
     */
    private Double mark;
    /**
     * 添加时间，上传时间
     */
    private Long addTime;

    ///
    ///运动跳绳

    /**
     * 跳绳详细（存的是本地文件，会上传文件服务器）
     */
    private String skipDetail;
    /**
     * 跳跃总个数
     */
    private Long skipNum;

    /**
     * 设备类型
     * 1、IOS，2、安卓
     */
    private Integer deviceType;


    private UserDto userDto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Long getStratTime() {
        return startTime;
    }

    public void setStratTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Long getRunTime() {
        return runTime;
    }

    public void setRunTime(Long runTime) {
        this.runTime = runTime;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getStep() {
        return step;
    }

    public void setStep(Long step) {
        this.step = step;
    }

    public Long getCalorie() {
        return calorie;
    }

    public void setCalorie(Long calorie) {
        this.calorie = calorie;
    }

    public Double getConsumeFat() {
        return consumeFat;
    }

    public void setConsumeFat(Double consumeFat) {
        this.consumeFat = consumeFat;
    }

    public Double getStartLng() {
        return startLng;
    }

    public void setStartLng(Double startLng) {
        this.startLng = startLng;
    }

    public Double getStartLat() {
        return startLat;
    }

    public void setStartLat(Double startLat) {
        this.startLat = startLat;
    }

    public Double getEndLng() {
        return endLng;
    }

    public void setEndLng(Double endLng) {
        this.endLng = endLng;
    }

    public Double getEndLat() {
        return endLat;
    }

    public void setEndLat(Double endLat) {
        this.endLat = endLat;
    }

    public String getZipUrl() {
        return zipUrl;
    }

    public void setZipUrl(String zipUrl) {
        this.zipUrl = zipUrl;
    }

    public String getStepDetail() {
        return stepDetail;
    }

    public void setStepDetail(String stepDetail) {
        this.stepDetail = stepDetail;
    }

    public Integer getBpm() {
        return bpm;
    }

    public void setBpm(Integer bpm) {
        this.bpm = bpm;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getLocationType() {
        return locationType;
    }

    public void setLocationType(Integer locationType) {
        this.locationType = locationType;
    }

    public Integer getModel() {
        return model;
    }

    public void setModel(Integer model) {
        this.model = model;
    }

    public Double getBpmMatch() {
        return bpmMatch;
    }

    public void setBpmMatch(Double bpmMatch) {
        this.bpmMatch = bpmMatch;
    }

    public Double getUserBpmMatch() {
        return userBpmMatch;
    }

    public void setUserBpmMatch(Double userBpmMatch) {
        this.userBpmMatch = userBpmMatch;
    }

    public Double getMark() {
        return mark;
    }

    public void setMark(Double mark) {
        this.mark = mark;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public String getSkipDetail() {
        return skipDetail;
    }

    public void setSkipDetail(String skipDetail) {
        this.skipDetail = skipDetail;
    }

    public Long getSkipNum() {
        return skipNum;
    }

    public void setSkipNum(Long skipNum) {
        this.skipNum = skipNum;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public UserRunDto() {
    }

    public UserRunDto(Long id, Integer uid, Long runTime, Long startTime, Long endTime, Long distance, Long step, Long calorie, Double consumeFat, Double startLng, Double startLat, Double endLng, Double endLat, String zipUrl, String detail, String stepDetail, Integer bpm, Integer state, Long updateTime, Integer type, Integer locationType, Integer model, Double bpmMatch, Double userBpmMatch, Double mark, Long addTime, String skipDetail, Long skipNum, Integer deviceType) {
        this.id = id;
        this.uid = uid;
        this.runTime = runTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.distance = distance;
        this.step = step;
        this.calorie = calorie;
        this.consumeFat = consumeFat;
        this.startLng = startLng;
        this.startLat = startLat;
        this.endLng = endLng;
        this.endLat = endLat;
        this.zipUrl = zipUrl;
        this.detail = detail;
        this.stepDetail = stepDetail;
        this.bpm = bpm;
        this.state = state;
        this.updateTime = updateTime;
        this.type = type;
        this.locationType = locationType;
        this.model = model;
        this.bpmMatch = bpmMatch;
        this.userBpmMatch = userBpmMatch;
        this.mark = mark;
        this.addTime = addTime;
        this.skipDetail = skipDetail;
        this.skipNum = skipNum;
        this.deviceType = deviceType;
    }
}
