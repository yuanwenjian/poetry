package com.yuanwj.poetry.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "poetry")
@Data
public class Poetry  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String content;

    private String author;

    private String categories;

    @Column(name = "create_date")
    private Date createDate;


}
