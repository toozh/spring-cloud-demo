package com.ifitmix.common.event.domain;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by zhangtao on 2017/4/28.
 */
@Document(collection = "NotifyEventPublish")
public class NotifyEventPublish extends EventPublish {
}
