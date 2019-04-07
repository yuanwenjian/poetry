package com.yuanwj.poetry.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "poetry_location")
@Data
public class PoetryLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String url;

    @Column(name = "create_date")
    private Date createDate;
}
