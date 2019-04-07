package com.yuanwj.poetry.model;

import lombok.Data;

@Data
public class SpiderRequest {
    private String url;

    private SpiderRequest.TYPE TYPE;

    public enum  TYPE{
        LOCATION,POETRY;
    }
}
