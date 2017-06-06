package com.ifitmix.utils;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by zhangtao on 2017/4/5.
 */
public class ConvertUtil {

    /**
     * 转换成 目标类型 bean
     * @param bean
     * @param targetClass
     * @return
     */
    public static Object convertBeanTo(Object bean, Class<?> targetClass) {
        Object result = null;
        try {
            result = targetClass.newInstance();

            if(null != bean) {
                Class<?> sourceClass = bean.getClass();
                Field[] sourcefields = sourceClass.getDeclaredFields();
                Field[] targetFields = targetClass.getDeclaredFields();
                for (Field sourcefield : sourcefields) {
                    for (Field targetField: targetFields) {
                        if(sourcefield.getName().equals(targetField.getName())) {
                            Object value = ReflectionUtil.getValueByFieldName(bean, sourcefield.getName());
                            ReflectionUtil.setValueByFieldName(result, value, sourcefield.getName());
                        }
                    }


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 合并两个bean的值
     * 把 from 的值 合并到 to
     * @param from
     * @param to
     * @return to
     */
    public static Object mergeBean(Object from, Object to) {
        if(null != from && null != to) {
            Class<?> fromCls = from.getClass();
            Method[] fromMethods = fromCls.getDeclaredMethods();
            Field[] fromFields = fromCls.getDeclaredFields();

            for(Field field : fromFields) {
                try {
                    String fieldGetName = ReflectionUtil.parGetName(field.getName());
                    if (!ReflectionUtil.checkGetMet(fromMethods, fieldGetName) || field.getName().equals("id")) {
                        continue;
                    }
                    Method fieldGetMet = fromCls.getMethod(fieldGetName, new Class[]{});
                    Object fieldVal = fieldGetMet.invoke(from, new Object[]{});
                    if (null != fieldVal) {
                        ReflectionUtils.doWithFields(to.getClass(), new ReflectionUtils.FieldCallback() {
                            public void doWith(Field toField) throws IllegalArgumentException, IllegalAccessException {
                                if (toField.getName().equals(field.getName())) {
                                    ReflectionUtils.makeAccessible(toField);
                                    toField.set(to, fieldVal);
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
        return to;
    }

}
