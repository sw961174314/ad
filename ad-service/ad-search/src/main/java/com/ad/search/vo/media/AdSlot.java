package com.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 广告位信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdSlot {

    // 广告位编码
    private String adSlotCode;

    // 广告位流量类型
    private Integer positionType;

    // 广告位宽度
    private Integer width;

    // 广告位高度
    private Integer height;

    // 广告物料类型
    private List<Integer> type;

    // 广告位出价
    private Integer minCpm;
}