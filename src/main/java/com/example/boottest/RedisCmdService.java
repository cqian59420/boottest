package com.example.boottest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisCmdService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public static final String APPOINTLIMITKEYPREFIX = "limit:appoint:";

/*    @PostConstruct
    void setSerialKey(){
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.java());
        LOGGER.info("redis Serializer seted");
    }*/

    public void addCount(String appointUser) {
        LOGGER.info("addCount start");
        final String middKey = LocalDate.now().toString() + ":" + appointUser;

        if (exists(middKey)) {
            if (incr(middKey, 1) > 5) {
                throw new RuntimeException("对不起，您已超过当日最大预约次数");
            }
        } else {
            //set(middKey, new Integer(1));
            incr(middKey, 1);
            expire(middKey, 48 * 3600);
        }

    }

    public boolean exists(final String key) {
        return redisTemplate.opsForValue().get(APPOINTLIMITKEYPREFIX+key) != null;
    }

    public Long incr(final String key, long numer) {
        return redisTemplate.opsForValue().increment(APPOINTLIMITKEYPREFIX + key, numer);
    }

    public Object get(final String key) {
        return redisTemplate.opsForValue().get(APPOINTLIMITKEYPREFIX+key);
    }

/*
    public void set(final String key, final long val) {
        redisTemplate.opsForValue().set(APPOINTLIMITKEYPREFIX+key, val);
    }*/

    public boolean expire(String key, long expire) {
        return this.setex(key, expire);
    }

    public boolean setex(final String key, final long timeout) {
        return redisTemplate.expire(APPOINTLIMITKEYPREFIX + key, 50, TimeUnit.SECONDS);
    }


}
