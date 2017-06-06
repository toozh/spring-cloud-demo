package com.ifitmix.common.mongo;


import org.apache.commons.collections.map.HashedMap;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by zhangtao on 2017/4/7.
 * mongo db 生成主键
 */
public class SaveMongoEventListener extends AbstractMongoEventListener<Object> {

    /**
     * 集合 - 自增序列集合
     */
    private static final String COLLECTION_ID_SEQENCE = "IdSeqence";
    /**
     * 集合 - 自增序列集合默认更新Update
     */
    private static final Update COLLECTION_ID_SEQENCE_DEFAULT_UPDATE = new Update().inc("value", 1);
    /**
     * 查找并修改参数 - 返回新值
     */
    protected static final FindAndModifyOptions FIND_AND_MODIFY_OPTIONS_RETURN_NEW = new FindAndModifyOptions().returnNew(true);


    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public void onBeforeConvert(final BeforeConvertEvent event) {
        if(event.getSource() != null) {
            ReflectionUtils.doWithFields(event.getSource().getClass(), new ReflectionUtils.FieldCallback() {
                public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                    ReflectionUtils.makeAccessible(field);
                    if (field.isAnnotationPresent(GeneratedValue.class)) {
                        Object obj = field.get(event.getSource());
                        if(null == field.get(event.getSource())) {
                            //设置自增ID
                            field.set(event.getSource(), getNextId(event.getSource().getClass().getName()));
                        }
                    }
                }
            });
        }
    }
    /**
     * 获取下一个自增ID
     * @author yinjihuan
     * @param collName  集合名
     * @return
     */
    private Long getNextId(String collName) {
        Query query = new Query(Criteria.where("_id").is(collName));
        Map<String, Object> result = mongoTemplate.findAndModify(
                query,
                COLLECTION_ID_SEQENCE_DEFAULT_UPDATE,
                FIND_AND_MODIFY_OPTIONS_RETURN_NEW,
                HashedMap.class,
                COLLECTION_ID_SEQENCE);
        if(result == null) {
            result = new HashedMap();
            result.put("_id", collName);
            result.put("value", 1L);
            mongoTemplate.insert(result, COLLECTION_ID_SEQENCE);
        }
        return (Long) result.get("value");
    }
}
