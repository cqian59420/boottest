package com.example.boottest.service;

import com.example.boottest.entity.Articles;
import com.example.boottest.repository.ArticlesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticlesService {

    @Autowired
    private ArticlesRepository articlesRepository;


    List<Articles> findByTitleAndCategory(String title, String category){
        return articlesRepository.findByTitleAndCategory(title, category);
    }

}
