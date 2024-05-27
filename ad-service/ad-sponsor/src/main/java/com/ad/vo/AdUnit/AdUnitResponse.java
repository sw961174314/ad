package com.ad.vo.AdUnit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 推广单元返回
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitResponse {

    private Long id;

    private String unitName;
}