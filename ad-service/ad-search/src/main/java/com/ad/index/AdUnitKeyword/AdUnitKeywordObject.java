package com.ad.index.AdUnitKeyword;

import com.ad.index.AdPlan.AdPlanObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 推广单元索引信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitKeywordObject {

    private Long unitId;

    private String keyword;
}