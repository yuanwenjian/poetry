package com.yuanwj.poetry.repository;

import com.yuanwj.poetry.entity.Poetry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PoetryRepository extends JpaRepository<Poetry,Long> {

    Poetry save(Poetry poetry);

    Poetry save(List<Poetry> poetryList);
}
