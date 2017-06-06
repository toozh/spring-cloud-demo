package com.ifitmix.club.handler;

import com.ifitmix.club.api.event.PushClubNotice;
import com.ifitmix.common.event.handler.NotifyEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangtao on 2017/5/18.
 */
public class PushClubNoticeHandler implements NotifyEventHandler<PushClubNotice> {

    private Logger logger = LoggerFactory.getLogger(PushClubNoticeHandler.class);

    @Override
    public void notify(PushClubNotice event) {
        logger.info("正在处理 " + event.toString());
    }
}
