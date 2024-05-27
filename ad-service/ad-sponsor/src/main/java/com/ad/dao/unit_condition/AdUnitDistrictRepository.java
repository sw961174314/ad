package com.ad.dao.unit_condition;

import com.ad.entity.AdUnit;
import com.ad.entity.unit_condition.AdUnitDistrict;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 推广单元地域操作
 */
public interface AdUnitDistrictRepository extends JpaRepository<AdUnitDistrict, Long> {

}