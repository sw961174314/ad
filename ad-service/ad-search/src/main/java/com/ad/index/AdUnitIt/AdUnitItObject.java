package com.ad.index.AdUnitIt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 兴趣索引信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitItObject {

    private Long unitId;

    private String itTag;
}