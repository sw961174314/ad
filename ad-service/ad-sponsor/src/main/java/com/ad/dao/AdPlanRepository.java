package com.ad.dao;

import com.ad.entity.AdPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 计划操作
 */
public interface AdPlanRepository extends JpaRepository<AdPlan, Long> {

    // 计划查找
    AdPlan findByIdAndUserId(Long id, Long userId);

    // 计划查找
    List<AdPlan> findAllByIdInAndUserId(List<Long> ids, Long userId);

    // 计划查找
    AdPlan findByUserIdAndPlanName(Long userId, String planName);

    // 计划查找
    List<AdPlan> findAllByPlanStatus(Integer status);
}
