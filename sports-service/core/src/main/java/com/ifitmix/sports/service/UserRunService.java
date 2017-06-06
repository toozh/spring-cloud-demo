package com.ifitmix.sports.service;

import com.ifitmix.base.constants.CodeConstants;
import com.ifitmix.base.exception.AppBusinessException;
import com.ifitmix.common.client.AliyunCenterClient;
import com.ifitmix.common.constants.FileConstants;
import com.ifitmix.common.domain.PageInfo;
import com.ifitmix.common.mongo.BaseMongoRepositoryImpl;
import com.ifitmix.sports.api.dtos.UserRunDto;
import com.ifitmix.sports.dao.UserRunRepository;
import com.ifitmix.sports.domain.UserRun;
import com.ifitmix.sports.utils.DtoUtils;
import com.ifitmix.utils.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zhangtao on 2017/3/9.
 */
@Service
public class UserRunService extends BaseMongoRepositoryImpl<UserRun, Long> {

    @Autowired
    private UserRunRepository userRunRepository;

    @Autowired
    private MongoOperations mongoOperations;


    public UserRun findUserRunByStartTimeAndUid(Long startTime, Integer uid) {
        return userRunRepository.findUserRunByStartTimeAndUid(startTime, uid);
    }

    /**
     * 添加运动记录
     * @param userRunDto
     * @param fileMap
     * @return
     */
    public UserRun addUserRun(UserRunDto userRunDto, Map<String, MultipartFile> fileMap) {
        UserRun userRun = new UserRun();
        ConvertUtil.mergeBean(userRunDto, userRun);
        String userRunZipUrl = null;
        if(fileMap.containsKey("userRunZip")) {
            MultipartFile userRunZip = fileMap.get("userRunZip");
            if(userRunZip != null) {
                userRunZipUrl = AliyunCenterClient.putFile(FileConstants.FILE_TYPE_RUN_FILE_ZIP, userRunZip);
            }
        } else {
            throw new AppBusinessException(CodeConstants.USER_RUN_STEP_DETAIL_NOT_UPLOAD, "运动文件为空");
        }

        // 判断是否 重复上传
        UserRun beforeUserRun = userRunRepository.findUserRunByStartTimeAndUid(userRun.getStartTime(), userRun.getUid());
        if(beforeUserRun != null) {
            return beforeUserRun;
        }
        userRun.setZipUrl(userRunZipUrl);

        return userRunRepository.insert(userRun);
    }

    @Override
    protected MongoRepository<UserRun, Long> getMongoRepository() {
        return userRunRepository;
    }

    /**
     * 获取 UserRun page
     * @param pageInfo
     * @return
     */
    public PageInfo<UserRunDto> findUserRunByPage(PageInfo<UserRunDto> pageInfo) {
        Criteria criteria = new Criteria();
        LinkedHashMap<String, Object> filter = pageInfo.getFilter();
        if(filter.containsKey("uid")) {
            criteria.and("uid").is(filter.get("uid"));
        }
        if(filter.containsKey("uids")) {
            Collection<Integer> uids = Arrays.stream(filter.get("uids").toString().split(",")).map(uid -> { return Integer.parseInt(uid);}).collect(Collectors.toList());
            criteria.and("uid").in(uids);
        }
        if(filter.containsKey("types")) {
            Collection<Integer> types = Arrays.stream(filter.get("types").toString().split(",")).map(type -> { return Integer.parseInt(type);}).collect(Collectors.toList());
            criteria.and("type").in(types);
        }
        if(filter.containsKey("beginTime") && filter.containsKey("endTime")) {
            criteria.and("startTime").gte(Long.valueOf(filter.get("beginTime").toString()))
                    .lte(Long.valueOf(filter.get("endTime").toString()));
        }

        Query query = new Query(criteria);
        pageInfo.setTotal(mongoOperations.count(query, UserRun.class));

        if(pageInfo.getSize() != PageInfo.NO_PAGE) {
            query.skip(pageInfo.getIndex()).limit(pageInfo.getSize());
        }

        List<UserRun> userRunList = mongoOperations.find(query, UserRun.class);
        List<UserRunDto> results = userRunList.stream().map(userRun -> { return DtoUtils.convertToUserRunDto(userRun);}).collect(Collectors.toList());
        pageInfo.setResult(results);

        return pageInfo;
    }
}
