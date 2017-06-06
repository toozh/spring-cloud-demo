package com.ifitmix.user.dao;


import com.ifitmix.user.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by zhangtao on 2017/3/13.
 */
public interface UserRepository extends MongoRepository<User, Integer> {

}
