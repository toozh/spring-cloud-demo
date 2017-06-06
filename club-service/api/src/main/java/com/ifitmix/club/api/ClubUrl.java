package com.ifitmix.club.api;

/**
 * Created by zhangtao on 2017/5/10.
 */
public interface ClubUrl {

    String SERVICE_NAME = "CLUB-SERVICE";

    String SERVICE_HOSTNAME = "http://CLUB-SERVICE";

    String CLUB_V1 = "v1/club";

    String CLUB_INFO_V1 = "v1/club/{clubId}";

    String CLUB_ACTIVITY_V1 = "v1/club/{clubId}/activity";

    String CLUB_SHARE_V1 = "v1/club/{clubId}/share";

    String CLUB_DYNAMIC_V1 = "v1/club/{clubId}/dynamic";

    String CLUB_MEMBER_V1 = "v1/club/{clubId}/member";

    String CLUB_MESSAGE_V1 = "v1/club/{clubId}/message";

    String CLUB_NOTICE_V1 = "v1/club/{clubId}/notice";

    String CLUB_NOTICE_INFO_V1 = "v1/club/{clubId}/notice/{noticeId}";

    String CLUB_MILE_V1 = "v1/club/{clubId}/mileStat";

    String CLUB_USER_RANK_V1 = "v1/club/{clubId}/userRank";

    String CLUB_USER_CLUB_V1 = "v1/club/userClub/{uid}";



    static String buildUrl(String url) {
        return SERVICE_HOSTNAME + url;
    }

}
