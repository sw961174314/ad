package com.ad.service;

import com.ad.Application;
import com.ad.exception.AdException;
import com.ad.vo.AdPlan.AdPlanGetRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

/**
 * 广告投放系统测试用例
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AdPlanServiceTest {

    @Autowired
    private IAdPlanService planService;

    @Test
    public void testGetAdPlan() throws AdException {
        System.out.println(planService.getAdPlanByIds(new AdPlanGetRequest(15L, Collections.singletonList(10L))));
    }
}