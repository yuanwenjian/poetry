package com.yuanwj.poetry.repository;

import com.yuanwj.poetry.entity.PoetryLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PoetryLocationRepository extends JpaRepository<PoetryLocation,Long> {
}
