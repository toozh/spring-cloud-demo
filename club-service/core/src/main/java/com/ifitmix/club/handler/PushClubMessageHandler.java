package com.ifitmix.club.handler;

import com.google.common.collect.Maps;
import com.ifitmix.base.Constants;
import com.ifitmix.base.api.ResponseEntity;
import com.ifitmix.club.api.event.PushClubMessage;
import com.ifitmix.club.dao.NoticeHandleRecordRepository;
import com.ifitmix.club.domain.NoticeHandleRecord;
import com.ifitmix.club.service.NoticeHandleRecordService;
import com.ifitmix.club.service.gateway.UserGateway;
import com.ifitmix.common.domain.PageInfo;
import com.ifitmix.common.event.handler.NotifyEventHandler;
import com.ifitmix.common.spring.ApplicationContextHolder;
import com.ifitmix.common.utils.XingeUtils;
import com.ifitmix.common.utils.umeng.UmengUtils;
import com.ifitmix.user.api.dtos.UserDto;
import com.ifitmix.utils.ReflectionUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangtao on 2017/5/18.
 */
public class PushClubMessageHandler implements NotifyEventHandler<PushClubMessage> {

    private Logger logger = LoggerFactory.getLogger(PushClubMessageHandler.class);

    @Override
    public void notify(PushClubMessage pushClubMessage) {
        logger.info("正在处理 " + pushClubMessage.toString());
        UserGateway userGateway = ApplicationContextHolder.context.getBean(UserGateway.class);

        if(pushClubMessage.getToUserIds() == null || pushClubMessage.getToUserIds().size() < 1) {
            logger.info("处理完成 " + pushClubMessage.toString());
            return;
        }

        Map<String, Object> map = new HashedMap();
        map.put("filter[uids]", StringUtils.join(pushClubMessage.getToUserIds().toArray(), ","));
        map.put("size", PageInfo.NO_PAGE);
        ResponseEntity<PageInfo<UserDto>> responseEntity = userGateway.findUser(map);
        NoticeHandleRecordService noticeHandleRecordService = ApplicationContextHolder.context.getBean(NoticeHandleRecordService.class);
        List<NoticeHandleRecord> list = new ArrayList<>();

        responseEntity.getResults().getResult().stream().forEach(userDto -> {
            NoticeHandleRecord noticeHandleRecord = new NoticeHandleRecord();
            noticeHandleRecord.setEventId(pushClubMessage.getId());
            noticeHandleRecord.setEventType(pushClubMessage.EVENT_TYPE.toString());
            noticeHandleRecord.setToTargetId(String.valueOf(userDto.getId()));
            noticeHandleRecord.setHandleStatus(NoticeHandleRecord.SEND_STATUS_0);
            noticeHandleRecord.setHandleTime(System.currentTimeMillis());
            noticeHandleRecord.setHandleNum(0);
            noticeHandleRecord.setNoticeType(Constants.MSG_TYPE_PUSH);
            list.add(noticeHandleRecord);
        });

        noticeHandleRecordService.save(list);
        Map<String, NoticeHandleRecord> noticeHandleRecordMap = Maps.uniqueIndex(list, noticeHandleRecord -> noticeHandleRecord.getToTargetId());

        HashMap<String, Object> customMap = ReflectionUtil.beanToMap(pushClubMessage.getPushBody());



        responseEntity.getResults().getResult().stream().forEach(userDto -> {
            try {

                // 信鸽推送
                if (!StringUtils.isEmpty(userDto.getDeviceTokenXG())) {
                    String result = "";
                    if (Constants.TERMINAL_ANDROID == userDto.getTerminal()) {
                        result = XingeUtils.getInstance().pushSingleDeviceAndroid(
                                userDto.getDeviceTokenXG(),
                                pushClubMessage.getPushBody().getTitle(),
                                pushClubMessage.getPushBody().getContent(),
                                customMap);
                    } else if (Constants.TERMINAL_IOS == userDto.getTerminal()) {
                        result = XingeUtils.getInstance().pushSingleDeviceIOS(
                                userDto.getDeviceTokenXG(),
                                pushClubMessage.getPushBody().getTitle(),
                                customMap);
                    } else {
                        result = NoticeHandleRecord.HANDLE_CODE_ERROR_1;
                    }

                    NoticeHandleRecord noticeHandleRecord = noticeHandleRecordMap.get(String.valueOf(userDto.getId()));
                    noticeHandleRecord.setHandleStatus(NoticeHandleRecord.SEND_STATUS_1);
                    noticeHandleRecordService.save(noticeHandleRecord);
                    return;
                }
                // 没有设备号这不推送
                if (StringUtils.isEmpty(userDto.getDeviceToken())) {
                    return;
                }

                //友盟推送
                String code = null;
                if (Constants.TERMINAL_ANDROID == userDto.getTerminal()) {
                    code = UmengUtils.getInstance().androidUnicast(
                            userDto.getDeviceToken(),
                            pushClubMessage.getPushBody().getTitle(),
                            pushClubMessage.getPushBody().getContent(),
                            customMap) + "";
                } else if(Constants.TERMINAL_IOS == userDto.getTerminal()) {
                    code = UmengUtils.getInstance().iosUnicast(
                            userDto.getDeviceToken(),
                            pushClubMessage.getPushBody().getAlert(),
                            1,
                            customMap) + "";
                } else {
                    code = NoticeHandleRecord.HANDLE_CODE_ERROR_1;
                }

                NoticeHandleRecord noticeHandleRecord = noticeHandleRecordMap.get(String.valueOf(userDto.getId()));
                noticeHandleRecord.setHandleCode(code);
                noticeHandleRecord.setHandleStatus(NoticeHandleRecord.SEND_STATUS_1);
                noticeHandleRecordService.save(noticeHandleRecord);

            } catch (Exception e) {
                e.printStackTrace();
//                logger.error("处理事件：" + pushClubMessage.toString() + " 出错, " + e.getStackTrace().toString());
            }

        });

        logger.info("处理完成 " + pushClubMessage.toString());
    }
}
