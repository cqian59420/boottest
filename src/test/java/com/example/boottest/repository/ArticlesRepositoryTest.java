package com.example.boottest.repository;

import com.example.boottest.entity.Articles;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticlesRepositoryTest {

    @Autowired
    private ArticlesRepository articlesRepository;

    @Autowired
    private JavaMailSender javaMailSender;


    @Test
    public void save(){
        articlesRepository.deleteAll();
//        articlesRepository.save(new Articles("java concurrent", "java"));
//        articlesRepository.save(new Articles("performance c++", "c++"));
    }

    @Test
    @Transactional
    public void findByTitleAndCategory() {
        articlesRepository.save(new Articles("java concurrent", "java","13234566"));
        articlesRepository.save(new Articles("performance c++", "c++","13234566"));
        List<Articles> byTitleAndCategory = articlesRepository.findByTitleAndCategory("java concurrent", "java");
        List<Articles> cppJava = articlesRepository.findByTitleAndCategory("java concurrent", "c++");
        Assert.assertEquals(1,byTitleAndCategory.size());
        Assert.assertEquals(0,cppJava.size());
        articlesRepository.deleteAll();
    }

    @Test
    public void findByTitle() {
    }


    @Test
    public void sendMail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("732576229@qq.com");
        message.setTo("732576229@qq.com");
        message.setSubject("主题：简单SpringBoot邮件");
        message.setText("测试简单SpringBoot邮件邮件内容");

        javaMailSender.send(message);

    }
}