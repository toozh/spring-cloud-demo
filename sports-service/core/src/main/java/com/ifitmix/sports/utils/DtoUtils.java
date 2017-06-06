package com.ifitmix.sports.utils;

import com.ifitmix.common.client.AliyunCenterClient;
import com.ifitmix.common.spring.ApplicationContextHolder;
import com.ifitmix.sports.domain.UserRun;
import com.ifitmix.sports.api.dtos.UserRunDto;
import com.ifitmix.utils.ConvertUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhangtao on 2017/3/9.
 */
public class DtoUtils {

    public static UserRunDto convertToUserRunDto(UserRun userRun) {
        if(userRun == null) {
            return new UserRunDto();
        }
        userRun.setDetail(AliyunCenterClient.buildUrl(userRun.getDetail()));
        userRun.setStepDetail(AliyunCenterClient.buildUrl(userRun.getStepDetail()));
        userRun.setZipUrl(AliyunCenterClient.buildUrl(userRun.getZipUrl()));
        UserRunDto userRunDto = (UserRunDto) ConvertUtil.convertBeanTo(userRun, UserRunDto.class);
        return userRunDto;
    }


    public static Page<UserRunDto> convertToUserRunDto(Page<UserRun> userRunPage) {
        Page<UserRunDto> userRunDtoPage = new Page<UserRunDto>() {
            @Override
            public int getTotalPages() {
                return userRunPage.getTotalPages();
            }

            @Override
            public long getTotalElements() {
                return userRunPage.getTotalElements();
            }

            @Override
            public <S> Page<S> map(Converter<? super UserRunDto, ? extends S> converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return userRunPage.getNumber();
            }

            @Override
            public int getSize() {
                return userRunPage.getSize();
            }

            @Override
            public int getNumberOfElements() {
                return userRunPage.getNumberOfElements();
            }

            @Override
            public List<UserRunDto> getContent() {
                return userRunPage.getContent().stream().map(userRun -> {
                    return DtoUtils.convertToUserRunDto(userRun);
                }).collect(Collectors.toList());
            }

            @Override
            public boolean hasContent() {
                return userRunPage.hasContent();
            }

            @Override
            public Sort getSort() {
                return userRunPage.getSort();
            }

            @Override
            public boolean isFirst() {
                return userRunPage.isFirst();
            }

            @Override
            public boolean isLast() {
                return userRunPage.isLast();
            }

            @Override
            public boolean hasNext() {
                return userRunPage.hasNext();
            }

            @Override
            public boolean hasPrevious() {
                return userRunPage.hasPrevious();
            }

            @Override
            public Pageable nextPageable() {
                return userRunPage.nextPageable();
            }

            @Override
            public Pageable previousPageable() {
                return userRunPage.previousPageable();
            }

            @Override
            public Iterator<UserRunDto> iterator() {
                return getContent().iterator();
            }
        };
        return userRunDtoPage;
    }

}
