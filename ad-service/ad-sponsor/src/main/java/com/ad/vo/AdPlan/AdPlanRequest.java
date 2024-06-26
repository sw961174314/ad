package com.ad.vo.AdPlan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

/**
 * 推广计划请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdPlanRequest {

    private Long id;

    private Long userId;

    private String planName;

    private String startDate;

    private String endDate;

    public boolean createValidate() {
        return userId != null && !StringUtils.isEmpty(planName) && !StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate);
    }

    public boolean updateValidate() {
        return id != null && userId != null;
    }

    public boolean deleteValidate() {
        return id != null && userId != null;
    }
}