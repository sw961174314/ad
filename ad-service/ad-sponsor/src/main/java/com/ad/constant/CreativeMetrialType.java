package com.ad.constant;

import lombok.Getter;

/**
 * 创意物料类型枚举
 */
@Getter
public enum CreativeMetrialType {

    JPG(1,"jpg"),
    BMP(2, "bmp"),
    MP4(3, "mp4"),
    AVI(4, "avi"),
    TXT(5, "txt");

    // 类型
    private Integer type;
    // 描述信息
    private String desc;

    CreativeMetrialType(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}