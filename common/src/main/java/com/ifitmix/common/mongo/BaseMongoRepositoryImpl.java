package com.ifitmix.common.mongo;


import com.ifitmix.utils.ConvertUtil;
import com.ifitmix.utils.ReflectionUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangtao on 2017/4/6.
 * mongo db 基础接口 实现类
 */
public abstract class BaseMongoRepositoryImpl<T, ID extends Serializable> {

    protected abstract MongoRepository<T, ID> getMongoRepository();

    /**
     * insert or update
     * @param entity
     * @return
     */
    public T save(T entity) {
        ID id = (ID) ReflectionUtil.getValueByFieldName(entity, "id");
        if(id != null) {
            T current = getMongoRepository().findOne(id);
            entity = (T) ConvertUtil.mergeBean(entity, current);
        }
        return getMongoRepository().save(entity);
    }

    /**
     * batch insert or update
     * @param list
     * @return
     */
    public List<T> save(List<T> list) {
        for (T entity : list) {
            save(entity);
        }
        return list;
    }

}
