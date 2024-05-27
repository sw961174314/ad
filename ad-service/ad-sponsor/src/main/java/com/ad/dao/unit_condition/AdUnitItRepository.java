package com.ad.dao.unit_condition;

import com.ad.entity.AdUnit;
import com.ad.entity.unit_condition.AdUnitKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 推广单元操作
 */
public interface AdUnitItRepository extends JpaRepository<AdUnit, Long> {

}