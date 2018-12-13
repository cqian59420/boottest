package com.example.boottest.repository;

import com.example.boottest.entity.Articles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Stream;

public interface ArticlesRepository extends JpaRepository<Articles,Long> {

    List<Articles> findByTitleAndCategory(String title, String category);

    Stream<Articles> findByTitle(String title);

}
