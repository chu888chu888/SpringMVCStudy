package com.chu.model;

/**
 * Created by P70 on 2016/11/9.
 */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
public class ArticleModel {
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id", nullable = false)
    private String id;


    @Column(name="title", unique=true, nullable=false)
    private String title;
}
