package com.ad.constant;

import lombok.Getter;

/**
 * 创意类型枚举
 */
@Getter
public enum CreativeType {

    IMAGE(1,"图片"),
    VIDEO(2, "视频"),
    TEXT(3, "文本");

    // 类型
    private Integer type;
    // 描述信息
    private String desc;

    CreativeType(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}