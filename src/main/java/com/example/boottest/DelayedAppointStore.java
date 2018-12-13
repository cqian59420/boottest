package com.example.boottest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class DelayedAppointStore {

    @Resource
    private RedisTemplate redisTemplate;

    private ZSetOperations<String,Integer> appointRecordWithExecTime;


    public DelayedAppointStore() {
//        appointRecordWithExecTime = redisTemplate.opsForZSet();
    }

    public void addAppointByExecTime(String key, Long execTimestamp, Integer appointRecordId) {
        LOGGER.info("addAppointByExecTime key:{},appointRecordId:{},execTimestamp:{}", key,
                execTimestamp, appointRecordId);
        appointRecordWithExecTime = redisTemplate.opsForZSet();
        appointRecordWithExecTime.add(key, appointRecordId, execTimestamp);
    }


    /**
     *
     * @param timePeriod 和当前时间戳差值范围
     * @return
     */
    public Set<Integer> findAppointRecordIdsByTime(String key, Long timePeriod){
        appointRecordWithExecTime = redisTemplate.opsForZSet();
        return appointRecordWithExecTime.range(key, 0, -1);
    }


    //@Scheduled(cron = "0 0/4 * * * ? ")
    public void sche(){
        LOGGER.info("key start");
        findAppointRecordIdsByTime("dqwdwdwa",2L);
    }

}
