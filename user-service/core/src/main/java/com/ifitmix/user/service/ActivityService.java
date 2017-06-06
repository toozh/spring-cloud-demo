package com.ifitmix.user.service;


import com.ifitmix.user.dao.ActivityRepository;
import com.ifitmix.user.domain.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhangtao on 2017/3/14.
 */
@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    public Activity getActivityById(Integer id) {
        return activityRepository.findOne(id);
    }

}
