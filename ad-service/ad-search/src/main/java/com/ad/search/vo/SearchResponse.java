package com.ad.search.vo;

import com.ad.index.Creative.CreativeObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {

    public Map<String, List<Creative>> adSlot2Ads = new HashMap<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Creative {

        // 广告ID
        private Long adId;

        // 广告Url
        private String adUrl;

        // 广告宽度
        private Integer width;

        // 广告高度
        private Integer height;

        // 广告类型
        private Integer type;

        // 广告类型
        private Integer materialType;

        // 展示监测Url
        private List<String> showMonitorUtl = Arrays.asList("www.baidu.com", "www.baidu.com");

        // 点击监测Url
        private List<String> clickMonitorUtl = Arrays.asList("www.baidu.com", "www.baidu.com");
    }

    public static Creative convert(CreativeObject object) {
        Creative creative = new Creative();
        creative.setAdId(object.getAdId());
        creative.setAdUrl(object.getAdUrl());
        creative.setWidth(object.getWidth());
        creative.setHeight(object.getHeight());
        creative.setType(object.getType());
        creative.setMaterialType(object.getMaterialType());
        return creative;
    }
}