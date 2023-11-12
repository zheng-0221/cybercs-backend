package com.zpp.cybercs.util;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zpp
 * @version 1.0
 * @description redis 工具类
 * @date 2023/11/6 19:54:27
 */
public class RedisUtils {

    /**
     * 将对象转化为 hash 存储
     *
     * @param stringRedisTemplate
     * @param key
     * @param object
     * @param timeout
     * @param timeUnit
     */
    public static void toHash(StringRedisTemplate stringRedisTemplate, String key, Object object, long timeout, TimeUnit timeUnit) {
        if (ObjectUtils.isEmpty(object)) {
            return;
        }
        Map<String, Object> objectMap = BeanUtil.beanToMap(object, new HashMap<>(),
                CopyOptions.create().
                        setIgnoreNullValue(true)
                        //.setFieldValueEditor((fieldName,fieldValue) -> fieldValue.toString()));
                        //解决方法：在setFieldValueEditor中也需要判空
                        .setFieldValueEditor((fieldName, fieldValue) -> {
                            if (fieldValue == null) {
                                fieldValue = "";
                            } else {
                                fieldValue = fieldValue.toString();
                            }
                            return fieldValue;
                        }));

        // 存储
        stringRedisTemplate.opsForHash().putAll(key, objectMap);


        // 设置过期时间
        stringRedisTemplate.expire(key, timeout, timeUnit);
    }
}
