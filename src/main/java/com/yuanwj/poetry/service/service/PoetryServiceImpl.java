package com.yuanwj.poetry.service.service;

import com.yuanwj.poetry.entity.Poetry;
import com.yuanwj.poetry.repository.PoetryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Callable;

public class PoetryServiceImpl implements Callable {

    @Resource
    private PoetryRepository poetryRepository;

    private int page;

    private int size;

    public PoetryServiceImpl() {
    }

    public PoetryServiceImpl(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public List<Poetry> call() throws Exception {
        System.out.println(Thread.currentThread().getName());
        System.out.println(page+"==="+size);
        Page<Poetry> poetryPage = poetryRepository.findAll(new PageRequest(page, size));
        return poetryPage.getContent();
    }


    public static void main(String[] args) {
        System.out.println(-1%10);
    }
}
