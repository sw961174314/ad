package com.ad.controller;

import com.ad.exception.AdException;
import com.ad.service.ICreativeService;
import com.ad.vo.Creative.CreativeRequest;
import com.ad.vo.Creative.CreativeResponse;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 创意接口
 */
@Slf4j
@RestController
public class CreativeOpController {

    @Autowired
    private ICreativeService creativeService;

    /**
     * 创意创建接口
     * @param request
     * @return
     * @throws AdException
     */
    @PostMapping
    public CreativeResponse createCreative(@RequestBody CreativeRequest request) throws AdException {
        log.info("ad-sponsor: createCreative -> {}", JSON.toJSONString(request));
        return creativeService.createCreative(request);
    }
}