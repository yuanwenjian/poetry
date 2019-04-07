package com.yuanwj.poetry.model;

import lombok.Data;

import java.util.List;

@Data
public class SpiderResult<T> {
    private List<T> entity;

    private List<SpiderRequest> requests;


}
