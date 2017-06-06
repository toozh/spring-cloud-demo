package com.ifitmix.sports.domain;

/**
 * Created by edward on 2016/9/1.
 *
 * 用户运动心率 (跳绳--跑步)
 */
public class UserHeartRate {

    /**
     * 默认年龄
     */
    public static Integer DEFAULT_AGE = 24;
    /**
     * 默认静息心率
     */
    public static Double DEFAULT_REST_HEART_RATE = 70D;

    /**
     * 运动的时候，作为参考的年龄 (default = 24)
     */
    private Integer currentAge;
    /**
     * HRMax (最大心率) = 220 - 年龄
     */
    private Double hrMax;
    /**
     * 运动的时候，作为作为参考的静息心率(default = 70)
     */
    private Double currentRestHeartRate;

    ///
    ///心率带 --- 心率数据

    /**
     *  平均心率
     */
    private Double heartRateAvg;
    /**
     * 最大心率
     */
    private Double maxHeartRate;
    /**
     * 最小心率
     */
    private Double minHeartRate;
    /**
     * VO2max 最大摄氧量
     */
    private Double voMax;
    /**
     * 本次运动 燃脂运动 花费的时长
     */
    private Long consumptionSpendTime;
    /**
     * 心率文件
     */
    private String heartRateFileLink;

    ///=====================================心率区间数=========================================
    /**
     * 有氧区间(数)
     */
    private Integer aerobicNum;
    /**
     * 无氧区间(数)
     */
    private Integer anaerobicNum;
    /**
     * 热身区间(数)
     */
    private Integer warmNum;
    /**
     * 燃脂区间(数)
     */
    private Integer fatBurningNum;
    /**
     * 最大区间(数)
     */
    private Integer maxNum;
    /**
     * 最后一次更新的时间
     */
    private Long lastUpdated;


    public Double getHeartRateAvg() {
        return heartRateAvg;
    }

    public void setHeartRateAvg(Double heartRateAvg) {
        this.heartRateAvg = heartRateAvg;
    }

    public Double getMaxHeartRate() {
        return maxHeartRate;
    }

    public void setMaxHeartRate(Double maxHeartRate) {
        this.maxHeartRate = maxHeartRate;
    }

    public Double getMinHeartRate() {
        return minHeartRate;
    }

    public void setMinHeartRate(Double minHeartRate) {
        this.minHeartRate = minHeartRate;
    }

    public Long getConsumptionSpendTime() {
        return consumptionSpendTime;
    }

    public void setConsumptionSpendTime(Long consumptionSpendTime) {
        this.consumptionSpendTime = consumptionSpendTime;
    }

    public String getHeartRateFileLink() {
        return heartRateFileLink;
    }

    public void setHeartRateFileLink(String heartRateFileLink) {
        this.heartRateFileLink = heartRateFileLink;
    }

    public Integer getAerobicNum() {
        return aerobicNum;
    }

    public void setAerobicNum(Integer aerobicNum) {
        this.aerobicNum = aerobicNum;
    }

    public Integer getAnaerobicNum() {
        return anaerobicNum;
    }

    public void setAnaerobicNum(Integer anaerobicNum) {
        this.anaerobicNum = anaerobicNum;
    }

    public Integer getWarmNum() {
        return warmNum;
    }

    public void setWarmNum(Integer warmNum) {
        this.warmNum = warmNum;
    }

    public Integer getFatBurningNum() {
        return fatBurningNum;
    }

    public void setFatBurningNum(Integer fatBurningNum) {
        this.fatBurningNum = fatBurningNum;
    }

    public Integer getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(Integer maxNum) {
        this.maxNum = maxNum;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getCurrentAge() {
        return currentAge;
    }

    public void setCurrentAge(Integer currentAge) {
        this.currentAge = currentAge;
    }

    public Double getCurrentRestHeartRate() {
        return currentRestHeartRate;
    }

    public void setCurrentRestHeartRate(Double currentRestHeartRate) {
        this.currentRestHeartRate = currentRestHeartRate;
    }
    
    public Double getVoMax() {
        return voMax;
    }

    public void setVoMax(Double voMax) {
        this.voMax = voMax;
    }

    public Double getHrMax() {
        return hrMax;
    }

    public void setHrMax(Double hrMax) {
        this.hrMax = hrMax;
    }
}
