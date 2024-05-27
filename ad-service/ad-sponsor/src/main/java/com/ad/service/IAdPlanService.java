package com.ad.service;

import com.ad.entity.AdPlan;
import com.ad.exception.AdException;
import com.ad.vo.AdPlan.AdPlanGetRequest;
import com.ad.vo.AdPlan.AdPlanRequest;
import com.ad.vo.AdPlan.AdPlanResponse;

import java.util.List;

/**
 * 推广计划服务功能
 */
public interface IAdPlanService {

    // 推广计划创建
    AdPlanResponse createAdPlan(AdPlanRequest request) throws AdException;

    // 推广计划获取
    List<AdPlan> getAdPlanByIds(AdPlanGetRequest request) throws AdException;

    // 推广计划更新
    AdPlanResponse updateAdPlan(AdPlanRequest request) throws AdException;

    // 推广计划删除
    void deleteAdPlan(AdPlanRequest request) throws AdException;
}