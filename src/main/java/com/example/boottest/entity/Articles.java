package com.example.boottest.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Articles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long article_id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String category;
    @Column(nullable = false,length = 13)
    private String isbn;

    public Articles(String title, String category,String isbn) {
        this.title = title;
        this.category = category;
        this.isbn = isbn;
    }
}
