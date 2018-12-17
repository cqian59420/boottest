package com.example.boottest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

@Component
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;


    public void addToMap(String name, String key, Object value) {
       redisTemplate.opsForHash().put(name,key,value);
    }

    public Map getRedisMap(String name) {
        return redisTemplate.opsForHash().entries(name);
    }

    public Object getRedisMap(String name, String key) {
        return redisTemplate.opsForHash().get(name,key);
    }


    public static final String APPOINTLIMITKEYPREFIX = "limit:appoint:";

    public void addCount(String appointUser) {
        String userAppointLimitKey = APPOINTLIMITKEYPREFIX + LocalDate.now().toString() + ":" + appointUser;

        if (exists(userAppointLimitKey)) {
            incr(userAppointLimitKey,"1");
            if (Long.valueOf ((String) get(userAppointLimitKey)) >5) {
                throw new RuntimeException("对不起，您已超过当日最大预约次数");
            }
        } else {
            set(userAppointLimitKey,"1");
            expire(userAppointLimitKey, 48*3600);
        }

    }

    public boolean exists(final String key) {
        return (Boolean)this.redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                redisTemplate.setKeySerializer(RedisSerializer.string());
                byte[] key_ = redisTemplate.getKeySerializer().serialize(key);
                return Boolean.TRUE.equals(connection.exists(key_)) ? true : false;
            }
        });
    }

    public Long incr(final String key,String numer) {
        return (Long)this.redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                redisTemplate.setKeySerializer(RedisSerializer.string());
                redisTemplate.setValueSerializer(RedisSerializer.string());
                byte[] key_ = redisTemplate.getKeySerializer().serialize(key);
                return connection.incrBy(key_, Long.valueOf(numer));
            }
        });
    }

    public Object get(final String key) {
        return this.redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                redisTemplate.setKeySerializer(RedisSerializer.string());
                redisTemplate.setValueSerializer(RedisSerializer.string());
                byte[] key_ = redisTemplate.getKeySerializer().serialize(key);
                byte[] value_ = connection.get(key_);
                return redisTemplate.getValueSerializer().deserialize(value_);
//                return RedisClient.this.valueSerializer.deserialize(value_);
            }
        });
    }


    public <T> void set(final String key, final T val) {
        this.redisTemplate.execute(new RedisCallback<Void>() {
            @Override
            public Void doInRedis(RedisConnection connection) throws DataAccessException {
                redisTemplate.setKeySerializer(RedisSerializer.string());
                redisTemplate.setValueSerializer(RedisSerializer.string());
                byte[] key_ = redisTemplate.getKeySerializer().serialize(key);
                byte[] value_ = redisTemplate.getValueSerializer().serialize(val);
                connection.setEx(key_, 604800L, value_);
                return null;
            }
        });
    }

    public boolean expire(String key, long expire) {
        return this.setex(key, expire);
    }
    public boolean setex(final String key, final long timeout) {
        return (Boolean)this.redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                redisTemplate.setKeySerializer(RedisSerializer.string());
                byte[] key_ = redisTemplate.getKeySerializer().serialize(key);
                return Boolean.TRUE.equals(connection.expire(key_, timeout)) ? true : false;
            }
        });
    }


}
