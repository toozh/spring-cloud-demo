package com.ifitmix.user.service;

import com.ifitmix.common.domain.PageInfo;
import com.ifitmix.user.api.dtos.UserDto;
import com.ifitmix.user.dao.UserRepository;
import com.ifitmix.user.domain.User;
import com.ifitmix.user.utils.DtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhangtao on 2017/3/13.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoOperations mongoOperations;

    public User getUserById(Integer id) {
        return userRepository.findOne(id);
    }

    public PageInfo<UserDto> findUserByPage(PageInfo<UserDto> page) {
        Criteria criteria = new Criteria();
        LinkedHashMap<String, Object> filter = page.getFilter();

        if(filter.containsKey("uids")) {
            Collection<Integer> uids = Arrays.stream(filter.get("uids").toString().split(",")).map(uid -> { return Integer.parseInt(uid);}).collect(Collectors.toList());
            criteria.and("id").in(uids);
        }


        Query query = new Query(criteria);
        page.setTotal(mongoOperations.count(query, User.class));

        if(page.getSize() != PageInfo.NO_PAGE) {
            query.skip(page.getIndex()).limit(page.getSize());
        }

        List<User> userList = mongoOperations.find(query, User.class);
        List<UserDto> results = userList.stream().map(userRun -> { return DtoUtils.convertToUserDto(userRun);}).collect(Collectors.toList());
        page.setResult(results);

        return page;
    }
}
