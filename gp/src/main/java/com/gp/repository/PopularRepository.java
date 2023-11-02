package com.gp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gp.entity.PopularSearchEntity;

@Repository
public interface PopularRepository extends JpaRepository<PopularSearchEntity, String> {

}
