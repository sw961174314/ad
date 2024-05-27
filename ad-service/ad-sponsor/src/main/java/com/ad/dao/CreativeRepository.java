package com.ad.dao;

import com.ad.entity.Creative;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 创意操作
 */
public interface CreativeRepository extends JpaRepository<Creative, Long> {

}
