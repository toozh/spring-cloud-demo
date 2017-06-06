package com.ifitmix.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by zhangtao on 2017/4/5.
 * 反射工具类
 */
public class ReflectionUtil {
    /**
     * 根据属性获取 bean 指定列的值
     * @param bean
     * @param fieldName
     * @return
     */
    public static Object getValueByFieldName(Object bean, String fieldName) {
        if (bean == null) {
            return null;
        }
        Object fieldVal = null;
        Class<?> cls = bean.getClass();
        try {
            String fieldGetName = parGetName(fieldName);
            Method fieldGetMet = cls.getMethod(fieldGetName, new Class[]{});
            fieldVal = fieldGetMet.invoke(bean, new Object[]{});
        } catch (Exception e) {

        }
        return fieldVal;
    }

    /**
     * 根据属性设置 bean 指定列的值
     * @param bean
     * @param value
     * @param fieldName
     * @return
     */
    public static void setValueByFieldName(Object bean, Object value, String fieldName) {
        if (bean != null) {
            Object fieldVal = null;
            Class<?> cls = bean.getClass();
            try {
                String fieldSetName = parSetName(fieldName);
                Field field = cls.getDeclaredField(fieldName);
                Method[] methods = cls.getMethods();
                for (Method method : methods) {
                    if (method.getName().equals(fieldSetName)) {
                        method.invoke(bean, value);
                    }
                }
            } catch (Exception e) {

            }
        }
    }

    public static void setValueByFieldName(Object object, String fieldName) {

    }

    /**
     * 拼接某属性的 get方法
     *
     * @param fieldName
     * @return String
     */
    public static String parGetName(String fieldName) {
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
        int startIndex = 0;
        if (fieldName.charAt(0) == '_')
            startIndex = 1;
        return "get"
                + fieldName.substring(startIndex, startIndex + 1).toUpperCase()
                + fieldName.substring(startIndex + 1);
    }

    /**
     * 拼接某属性的 set方法
     *
     * @param fieldName
     * @return String
     */
    public static String parSetName(String fieldName) {
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
        int startIndex = 0;
        if (fieldName.charAt(0) == '_')
            startIndex = 1;
        return "set"
                + fieldName.substring(startIndex, startIndex + 1).toUpperCase()
                + fieldName.substring(startIndex + 1);
    }

    /**
     * 判断是否存在某属性的 get方法
     *
     * @param methods
     * @param fieldGetMet
     * @return boolean
     */
    public static boolean checkGetMet(Method[] methods, String fieldGetMet) {
        for (Method met : methods) {
            if (fieldGetMet.equals(met.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 对象转成MAP
     * @param bean
     * @return
     */
    public static HashMap<String,Object> beanToMap(Object bean) {
        HashMap<String, Object> map = new HashMap<>();
        if(null != bean) {
            Class<?> cls = bean.getClass();
            Method[] methods = cls.getDeclaredMethods();
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                try {
                    String fieldGetName = parGetName(field.getName());
                    if (!checkGetMet(methods, fieldGetName)) {
                        continue;
                    }
                    Method fieldGetMet = cls.getMethod(fieldGetName, new Class[]{});
                    Object fieldVal = fieldGetMet.invoke(bean, new Object[]{});
                    if (fieldVal != null) {
                        map.put(field.getName(), fieldVal);
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
        return map;
    }

    public static boolean isBaseType(String clazz) {
        boolean result = false;
        clazz = clazz.split(" ")[1];
        if(clazz.contains("String") || clazz.contains("Integer") || clazz.contains("Double")
                || clazz.contains("Float") || clazz.contains("Long") || clazz.contains("Short")
                || clazz.contains("int") || clazz.contains("float") || clazz.contains("double")
                || clazz.contains("long") || clazz.contains("short")) {
            result = true;
        }
        return result;
    }
}
