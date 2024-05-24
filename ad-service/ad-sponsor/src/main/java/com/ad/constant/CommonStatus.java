package com.ad.constant;

import lombok.Getter;

/**
 * 常量枚举
 */
@Getter
public enum CommonStatus {

    VALID(1,"有效状态"),
    INVALID(0, "无效状态");

    // 状态
    private Integer status;
    // 描述信息
    private String desc;

    CommonStatus(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}