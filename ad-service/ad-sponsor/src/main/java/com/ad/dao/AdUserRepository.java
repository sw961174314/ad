package com.ad.dao;

import com.ad.entity.AdUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户操作
 */
public interface AdUserRepository extends JpaRepository<AdUser, Long> {

    AdUser findByUsername(String username);
}
