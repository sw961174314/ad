package com.ad.index.AdUnitDistrict;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 地域索引信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitDistrictObject {

    private Long unitId;

    private String province;

    private String city;
}