package com.ifitmix.club.utils;

import com.ifitmix.club.api.dtos.ClubMemberDto;
import com.ifitmix.club.api.dtos.ClubMessageDto;
import com.ifitmix.club.api.dtos.ClubNoticeDto;
import com.ifitmix.club.domain.ClubMember;
import com.ifitmix.club.domain.ClubMessage;
import com.ifitmix.club.domain.ClubNotice;
import com.ifitmix.common.client.AliyunCenterClient;
import com.ifitmix.utils.ConvertUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by zhangtao on 2017/5/18.
 */
public class DtoUtils {

    public static ClubMemberDto convertToClubMemberDto(ClubMember clubMember) {
        if(clubMember == null) {
            return new ClubMemberDto();
        }

        return (ClubMemberDto) ConvertUtil.convertBeanTo(clubMember, ClubMemberDto.class);

    }

    public static ClubMessageDto convertToClubMessageDto(ClubMessage clubMessage) {
        if(clubMessage == null) {
            return new ClubMessageDto();
        }
        ClubMessageDto clubMessageDto = (ClubMessageDto) ConvertUtil.convertBeanTo(clubMessage, ClubMessageDto.class);
        if(!StringUtils.isEmpty(clubMessageDto.getFileLink())) {
            clubMessageDto.setFileLink(AliyunCenterClient.buildUrl(clubMessageDto.getFileLink()));
        }
        return clubMessageDto;
    }

    public static ClubNoticeDto convertToClubNoticeDto(ClubNotice clubNotice) {
        if(clubNotice == null) {
            return new ClubNoticeDto();
        }
        ClubNoticeDto clubNoticeDto = (ClubNoticeDto) ConvertUtil.convertBeanTo(clubNotice, ClubNoticeDto.class);
        if(!StringUtils.isEmpty(clubNoticeDto.getBackImageUrl())) {
            clubNoticeDto.setBackImageUrl(AliyunCenterClient.buildUrl(clubNoticeDto.getBackImageUrl()));
        }
        return clubNoticeDto;
    }
}
