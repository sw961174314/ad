package com.ad.controller;

import com.ad.annotation.IgnoreResponseAdvice;
import com.ad.client.SponsorClient;
import com.ad.client.vo.AdPlan;
import com.ad.client.vo.AdPlanGetRequest;
import com.ad.vo.CommonResponse;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RestController
public class SearchController {

    private final RestTemplate restTemplate;

    @Autowired
    private SponsorClient sponsorClient;

    @Autowired
    public SearchController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 使用Ribbon调用ad-sponsor中的/ad-sponsor/get/adPlan方法
     * @param request
     * @return
     */
    @IgnoreResponseAdvice
    @SuppressWarnings("all")
    @PostMapping("/getAdPlansByRibbon")
    public CommonResponse<List<AdPlan>> getAdPlansByRibbon(@RequestBody AdPlanGetRequest request) {
        log.info("ad-search: getAdPlansByRibbon -> {}", JSON.toJSONString(request));
        return restTemplate.postForEntity("http://eureka-client-ad-sponsor/ad-sponsor/get/adPlan", request, CommonResponse.class).getBody();
    }

    /**
     * 使用Feign调用ad-sponsor中的/ad-sponsor/get/adPlan方法
     * @param request
     * @return
     */
    @SuppressWarnings("all")
    @PostMapping("/getAdPlansByFeign")
    public CommonResponse<List<AdPlan>> getAdPlansByFeign(@RequestBody AdPlanGetRequest request) {
        log.info("ad-search: getAdPlansByFeign -> {}", JSON.toJSONString(request));
        return sponsorClient.getAdPlans(request);
    }
}