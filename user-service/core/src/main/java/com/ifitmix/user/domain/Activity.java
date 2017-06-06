package com.ifitmix.user.domain;

import com.google.common.collect.ImmutableMap;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by Sin on 2016/2/16.
 *
 * app 活动信息
 */
@Document(collection = "Activity")
public class Activity {

    /**
     * 普通赛事
     */
    public static final int TYPE_COMMON = 0;
    /**
     * 积分赛事
     */
    public static final int TYPE_INTEGRAL = 1;
    /**
     * 第三方赛事/外部赛事 link url
     */
    public static final int TYPE_OUTER = 2;
    /**
     * 标准赛事/包含完整的报名信息
     */
    public static final int TYPE_NORMAL = 3;

    /**
     * 状态/正常
     */
    public static final int STATUS_NORMAL = 0;
    /**
     * 状态/无效
     */
    public static final int STATUS_NOT_NORMAL = 1;

    /**
     * 发布状态/发布
     */
    public static final int RELEASE_STATUS_RELEASE = 0;
    /**
     * 发布状态/等待发布
     */
    public static final int RELEASE_STATUS_WAIT_RELEASE = 1;
    /**
     * 发布状态/结束
     */
    public static final int RELEASE_STATUS_END = 2;
    /**
     * 发布状态/错误
     */
    public static final int RELEASE_STATUS_ERROR = 3;
    /**
     * 所有的报名信息模版
     */
    public final static ImmutableMap<Integer, String> SIGN_UP_MODE_ALL = ImmutableMap.<Integer, String>builder()
            .put(1, "姓名")
            .put(2, "性别")
            .put(3, "衣服尺码")
            .put(4, "备注")
            .put(5, "证件类型")
            .put(6, "证件号码")
            .put(7, "邮箱")
            .put(8, "紧急联系人姓名")
            .put(9, "紧急联系人电话")
            .put(10, "血型")
            .put(11, "参赛金额")
            .put(12, "支付方式")
            .put(13, "条款")
            .put(14, "联系人名称")
            .put(15, "跑团名称")
            .put(16, "跑团口号").build();
    @Id
    private Integer id;

    /**
     * 活动主题 名称
     */
    private String themeName;
    /**
     * 主题图片
     */
    private String themeImage;
    /**
     * 报名注册人数
     */
    private Integer registerCount;
    /**
     * 活动内容(说明:编码后入库,使用需解码)
     * URLDecoder.decode(activityContent,"UTF-8") 解码
     * URLEncoder.encode(activityContent,"UTF-8") 编码
     */
    private String activityContent;
    /**
     * 赛事类型
     * 0、普通赛事(默认) {@link Activity#TYPE_COMMON}
     * 1、积分赛事 {@link Activity#TYPE_INTEGRAL}
     * 2、第三方赛事
     * 3、标准赛事(严格的报名检测)
     */
    private Integer type;

    ///
    /// 时间

    /**
     * 活动开始时间 (开始)
     */
    private Long activityBeginTime;
    /**
     * 活动结束时间 (结束)
     */
    private Long activityEndTime;
    /**
     * 报名时间 (开始)
     */
    private Long signUpBeginTime;
    /**
     * 报名时间 (结束)
     */
    private Long signUpEndTime;
    /**
     * 活动组信息
     */
    /**
     * 报名信息模版
     * 1、姓名
     * 2、性别
     * 3、衣服尺码
     * 4、备注
     * 5、证件类型
     * 6、证件号码
     * 7、邮箱
     * 8、紧急联系人姓名
     * 9、紧急联系人电话
     * 10、血型
     * 11、参赛金额
     * 12、支付方式
     * 13、条款
     * 14、联系人名称
     * 15、跑团名称
     * 16、跑团口号
     */
    private List<Integer> signUpMode;

    ///
    /// 人数

    /**
     * 虚假成员
     */
    private Integer activityFalseNumber;
    /**
     * 活动最大人数
     */
    private Integer activityMaxNumber;
    /**
     * 活动 置顶 排序方式
     */
    private Integer sort;
    /**
     * 活动描述
     */
    private String desc;
    /**
     * 报名金额
     */
    private Double activityMoney;
    /**
     * 活动地址
     */
    private String url;
    /**
     * 积分赛事标签 城市(1-200)，区域(201-400)
     * 1、深圳市
     *
     * 201、上海市
     * 202、河南省
     */
    private Integer cityTarget;
    /**
     * 赛事限定的 最大的报名组人数(标准赛事才有)
     */
    private Integer maxGroupNum;
    /**
     * 赛事性别检查
     */
    private String checkSex;
    /**
     * 发布状态
     * 0、发布 1、待发布 2、活动结束 3、活动异常(临时可以控制处理异常)
     */
    private Integer releaseStatus;
    /**
     * 状态
     * 0、正常 1、无效
     */
    private Integer status;
    /**
     * 添加时间
     */
    private Long addTime;
    /**
     * 浏览量
     */
    private Long viewCount;
    /**
     * 第三方赛事编号
     */
    private String outActivityId;
    /**
     * 渠道号
     */
    private Integer channel;
    /**
     * 关注点--标签##用于app展示
     */
    private String focusTag;

    ///
    /// 非存储字段

    /**
     * 活动开始 还有多少天
     */
    @Transient
    private Integer startDiffHour;
    /**
     * 活动开始 还有多少天
     */
    @Transient
    private Integer endDiffHour;

    /**
     * 是否 注册
     */
    @Transient
    private boolean register;

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getThemeImage() {
        return themeImage;
    }

    public void setThemeImage(String themeImage) {
        this.themeImage = themeImage;
    }

    public Integer getRegisterCount() {
        return registerCount;
    }

    public void setRegisterCount(Integer registerCount) {
        this.registerCount = registerCount;
    }

    public Long getActivityBeginTime() {
        return activityBeginTime;
    }

    public void setActivityBeginTime(Long activityBeginTime) {
        this.activityBeginTime = activityBeginTime;
    }

    public Long getActivityEndTime() {
        return activityEndTime;
    }

    public void setActivityEndTime(Long activityEndTime) {
        this.activityEndTime = activityEndTime;
    }

    public Long getSignUpBeginTime() {
        return signUpBeginTime;
    }

    public void setSignUpBeginTime(Long signUpBeginTime) {
        this.signUpBeginTime = signUpBeginTime;
    }

    public Long getSignUpEndTime() {
        return signUpEndTime;
    }

    public void setSignUpEndTime(Long signUpEndTime) {
        this.signUpEndTime = signUpEndTime;
    }

    public Integer getActivityFalseNumber() {
        return activityFalseNumber;
    }

    public void setActivityFalseNumber(Integer activityFalseNumber) {
        this.activityFalseNumber = activityFalseNumber;
    }

    public Integer getActivityMaxNumber() {
        return activityMaxNumber;
    }

    public void setActivityMaxNumber(Integer activityMaxNumber) {
        this.activityMaxNumber = activityMaxNumber;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Double getActivityMoney() {
        return activityMoney;
    }

    public void setActivityMoney(Double activityMoney) {
        this.activityMoney = activityMoney;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getReleaseStatus() {
        return releaseStatus;
    }

    public void setReleaseStatus(Integer releaseStatus) {
        this.releaseStatus = releaseStatus;
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

    public Integer getStartDiffHour() {
        return startDiffHour;
    }

    public void setStartDiffHour(Integer startDiffHour) {
        this.startDiffHour = startDiffHour;
    }

    public Integer getEndDiffHour() {
        return endDiffHour;
    }

    public void setEndDiffHour(Integer endDiffHour) {
        this.endDiffHour = endDiffHour;
    }

    public boolean isRegister() {
        return register;
    }

    public void setRegister(boolean register) {
        this.register = register;
    }

    public String getActivityContent() {
        return activityContent;
    }

    public void setActivityContent(String activityContent) {
        this.activityContent = activityContent;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<Integer> getSignUpMode() {
        return signUpMode;
    }

    public void setSignUpMode(List<Integer> signUpMode) {
        this.signUpMode = signUpMode;
    }

    public Integer getCityTarget() {
        return cityTarget;
    }

    public void setCityTarget(Integer cityTarget) {
        this.cityTarget = cityTarget;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getMaxGroupNum() {
        return maxGroupNum;
    }

    public void setMaxGroupNum(Integer maxGroupNum) {
        this.maxGroupNum = maxGroupNum;
    }

    public String getCheckSex() {
        return checkSex;
    }

    public void setCheckSex(String checkSex) {
        this.checkSex = checkSex;
    }

    public String getOutActivityId() {
        return outActivityId;
    }

    public void setOutActivityId(String outActivityId) {
        this.outActivityId = outActivityId;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public String getFocusTag() {
        return focusTag;
    }

    public void setFocusTag(String focusTag) {
        this.focusTag = focusTag;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
