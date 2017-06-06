package com.ifitmix.common.constants;

/**
 * Created by sin on 2015/4/20.
 */
public class FileConstants {

    ///
    /// 文件类型 1001 ~ 1000

    /**
     * 文件中心/mix 文件
     */
    public static final Integer FILE_TYPE_AUDIO_MIX = 1000;
    /**
     * 文件中心/mix 文件图片
     */
    public static final Integer FILE_TYPE_AUDIO_MIX_IMAGE = 1001;
    /**
     * 文件中心/mix 文件图片
     */
    public static final Integer FILE_TYPE_USER_AVATAR_IMAGE = 1002;
    /**
     * 文件中心/用户运动，轨迹详情
     */
    public static final Integer FILE_TYPE_USER_RUN_DETAIL = 1003;
    /**
     * 文件中心/用户运动 分享图片
     */
    @Deprecated
    public static final Integer FILE_TYPE_USER_SHARE_IMAGE = 1004;
    /**
     * 文件中心/用户直播 聊天语音
     */
    public static final Integer FILE_TYPE_USER_LIVE_VOICE = 1005;
    /**
     * MixAuthor 用户头像
     */
    public static final Integer FILE_TYPE_MIX_AUTHOR_AVATAR = 1006;
    /**
     * MixAuthor Photos
     */
    public static final Integer FILE_TYPE_MIX_AUTHOR_PHOTOS = 1007;
    /**
     * app 其它背景图(app home)
     */
    public static final Integer FILE_TYPE_APP_BACKGROUND_IMAGE = 1008;
    /**
     * 用户 歌曲分组背景图（自定义歌单）
     */
    public static final Integer FILE_TYPE_USER_SONG_GROUP_IMAGE = 1009;

    /**
     * 文件中心/video 文件
     */
    public static final Integer FILE_TYPE_VIDEO = 1010;

    /**
     * 文件中心/video 视频封面
     */
    public static final Integer FILE_TYPE_VIDEO_IMAGE = 1011;

    /**
     * 文件中心/Android 渠道包
     */
    public static final Integer FILE_TYPE_ANDROID_APK = 1012;

    /**
     * 文件中心/彩蛋 图片
     */
    public static final Integer FILE_TYPE_SURPRISE = 1013;

    /**
     * 用户勋章图片
     */
    public static final Integer FILE_TYPE_RUN_LEVEL = 1014;
    /**
     * 用户运动文件压缩包
     */
    public static final Integer FILE_TYPE_RUN_FILE_ZIP = 1015;

    /**
     * app 活动二维码 图片
     */
    public static final Integer FILE_TYPE_APP_ACTIVITY_IMAGE = 2000;
    /**
     * 用户 俱乐部背景图
     */
    public static final Integer FILE_TYPE_USER_CLUB_BACK_IMAGE = 2001;
    /**
     * mix 音乐 专辑（分类） 曲风 场景 背景图
     */
    public static final Integer FILE_TYPE_MIX_DIC_BACK_IMAGE = 2002;
    /**
     * mix 音乐 专辑（分类） 曲风 场景 背景图
     */
    public static final Integer FILE_TYPE_CLUB_NOTICE_IMAGE = 2003;
    /**
     * 文件中心/用户运动，记步文件
     */
    public static final Integer FILE_TYPE_USER_STEP_DETAIL = 2004;
    /**
     * 俱乐部/二维码  //TODO 删除 OSS 资源文件
     */
    @Deprecated
    public static final Integer FILE_TYPE_CLUB_QR_CODE_IMAGE = 2005;
    /**
     * 用户运动分享/资源图片
     */
    public static final Integer FILE_TYPE_USER_RUN_SHARE_RESOURCE_IMAGE = 2006;
    /**
     * 活动/活动主题图片
     */
    public static final Integer FILE_TYPE_ACTIVITY_THEME_IMAGE = 2007;
    /**
     * mix banner/活动图片
     */
    public static final Integer FILE_TYPE_MIX_BANNER_IMAGE = 2008;
    /**
     * mix album/歌曲专辑图
     */
    public static final Integer FILE_TYPE_MIX_ALBUM_IMAGE = 2009;
    /**
     * app 启动闪屏背景图
     */
    public static final Integer FILE_TYPE_START_PAGE_IMAGE = 2010;
    /**
     * app 投放广告链接图
     */
    public static final Integer FILE_TYPE_ADVERT_IMAGE = 2011;
    /**
     * 启动视频
     */
    public static final Integer FILE_TYPE_START_VIDEO = 2012;
    /**
     * 启动视频的图片
     */
    public static final Integer FILE_TYPE_START_VIDEO_IMG = 2013;

    /**
     * 资源文件
     */
    public static final Integer FILE_TYPE_RESOURCE_FILE = 2014;
    /**
     * Icon文件
     */
    public static final Integer FILE_TYPE_RESOURCE_ICON_FILE = 2015;
    /**
     * 封面图片
     */
    public static final Integer FILE_TYPE_RESOURCE_COVER_FILE = 2016;
    /**
     * 运动 -- 心率文件
     */
    public static final Integer FILE_TYPE_HEART_RATE_FILE= 2017;
    /**
     * 俱乐部消息 -- 图片文件
     */
    public static final Integer FILE_TYPE_CLUB_MSG_IMAGE = 2110;
    /**
     * 俱乐部消息 -- 语音文件
     */
    public static final Integer FILE_TYPE_CLUB_MSG_VOICE = 2111;

    ///
    ///跳绳

    /**
     * 跳跃信息文件
     */
    public static final Integer FILE_TYPE_SKIP_ROPE_SKIP_DETAIL = 2100;
    /**
     * 用户跳绳分享图片
     */
    public static final Integer FILE_TYPE_USER_SKIP_ROPE_SHARE = 2101;

    ///
    /// 用户UGC 验证图片
    public static final Integer FILE_TYPE_USER_IDENTIFY = 2200;

    ///
    /// BBS
    public static final Integer FILE_TYPE_BBS_ARTICLE = 2300;
    // BBS / banner
    public static final Integer FILE_TYPE_BBS_BANNER = 2301;
    // BBS / recommend
    public static final Integer FILE_TYPE_BBS_RECOMMEND = 2302;

    //动态图片
    public static final Integer FILE_TYPE_DYNAMIC_IMG = 2400;

    // 商城
    public static final Integer FILE_TYPE_SHOP_BANNER = 2501;


    ///
    /// 文件压缩信息

    /**
     * mix 歌曲专辑图片压缩 分辨率/300 * 300
     */
    public static final Integer FILE_TYPE_AUDIO_MIX_ALBUM_RESOLUTION[] = new Integer[]{200, 200};
    /**
     * mix 歌曲专辑图片压缩 分辨率/300 * 300
     */
    public static final Integer FILE_TYPE_AUDIO_MIX_ALBUM_RESOLUTION2[] = new Integer[]{375 * 2, 337 * 2};
    /**
     * 用户头像 分辨率/300 * 300
     */
    public static final Integer FILE_TYPE_AUDIO_USER_AVATAR_RESOLUTION[] = new Integer[]{200, 200};

}
