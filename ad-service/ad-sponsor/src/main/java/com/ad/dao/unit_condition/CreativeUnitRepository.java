package com.ad.dao.unit_condition;

import com.ad.entity.unit_condition.AdUnitKeyword;
import com.ad.entity.unit_condition.CreativeUnit;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 创意与推广单元操作
 */
public interface CreativeUnitRepository extends JpaRepository<CreativeUnit, Long> {

}