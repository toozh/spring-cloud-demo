package com.ifitmix.user.dao;

import com.ifitmix.user.domain.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by zhangtao on 2017/3/14.
 */
public interface ActivityRepository extends MongoRepository<Activity, Integer> {

}
