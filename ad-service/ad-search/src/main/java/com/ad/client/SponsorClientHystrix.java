package com.ad.client;

import com.ad.client.vo.AdPlan;
import com.ad.client.vo.AdPlanGetRequest;
import com.ad.vo.CommonResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Hystrix断路器
 */
@Component
public class SponsorClientHystrix implements SponsorClient{

    @Override
    public CommonResponse<List<AdPlan>> getAdPlans(AdPlanGetRequest request) {
        return new CommonResponse<>(-1, "eureka-client-ad-sponsor error");
    }
}