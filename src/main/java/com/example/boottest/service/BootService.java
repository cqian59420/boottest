package com.example.boottest.service;

import com.example.boottest.DelayedAppointStore;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Set;

@RestController
@Slf4j
public class BootService {

    @Autowired
    DelayedAppointStore delayedAppointStore;

    @PostMapping(path = "/add")
    @ApiOperation(value="增加内容进入redis", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key" ,value= "redisKey", required = true, dataType = "String"),
            @ApiImplicitParam(name = "appointRecordId" ,value= "预约单号", required = true, dataType = "Integer")
    })
    public void add(String key,Integer appointRecordId){
        Long score = System.currentTimeMillis() + 5000;
        delayedAppointStore.addAppointByExecTime(key,score,appointRecordId);
    }


    @GetMapping(path = "/time")
    @ApiOperation(value="查询当前毫秒数", notes="")
    public String envTest(){
        Long score = System.currentTimeMillis()/1000;
        return score + "";
    }

    @PostMapping(path = "/getBykey")
    public Set<Integer> getBykey(String key){
        Long score = System.currentTimeMillis()/1000 + 20000;
        return delayedAppointStore.findAppointRecordIdsByTime(key,score);
    }

    @Scheduled(cron = "0 0/4 * * * ? ")
    public void sche(){
        LOGGER.info("key start");
        delayedAppointStore.findAppointRecordIdsByTime("dqwdwdwa",2L);
    }

}
