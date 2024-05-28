package com.ad.controller;

import com.ad.entity.AdPlan;
import com.ad.exception.AdException;
import com.ad.service.IAdPlanService;
import com.ad.vo.AdPlan.AdPlanGetRequest;
import com.ad.vo.AdPlan.AdPlanRequest;
import com.ad.vo.AdPlan.AdPlanResponse;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 推广计划接口
 */
@Slf4j
@RestController
public class AdPlanOpController {

    @Autowired
    private IAdPlanService adPlanService;

    /**
     * 推广计划创建
     * @param request
     * @return
     * @throws AdException
     */
    @PostMapping("/create/adPlan")
    public AdPlanResponse createAdPlan(@RequestBody AdPlanRequest request) throws AdException {
        log.info("ad-sponsor: createAdPlan -> {}", JSON.toJSONString(request));
        return adPlanService.createAdPlan(request);
    }

    /**
     * 推广计划查询
     * @param request
     * @return
     * @throws AdException
     */
    @PostMapping("/get/adPlan")
    public List<AdPlan> getAdPlanByIds(AdPlanGetRequest request) throws AdException {
        log.info("ad-sponsor: getAdPlanByIds -> {}", JSON.toJSONString(request));
        return adPlanService.getAdPlanByIds(request);
    }

    /**
     * 推广计划更改
     * @param request
     * @return
     * @throws AdException
     */
    public AdPlanResponse updateAdPlan(@RequestBody AdPlanRequest request) throws AdException {
        log.info("ad-sponsor: updateAdPlan -> {}", JSON.toJSONString(request));
        return adPlanService.updateAdPlan(request);
    }

    /**
     * 推广计划删除
     * @param request
     * @throws AdException
     */
    public void deleteAdPlan(@RequestBody AdPlanRequest request) throws AdException {
        log.info("ad-sponsor: deleteAdPlan -> {}", JSON.toJSONString(request));
        adPlanService.deleteAdPlan(request);
    }
}