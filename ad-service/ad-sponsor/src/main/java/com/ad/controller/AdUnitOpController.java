package com.ad.controller;

import com.ad.exception.AdException;
import com.ad.service.IAdUnitService;
import com.ad.vo.AdUnit.AdUnitRequest;
import com.ad.vo.AdUnit.AdUnitResponse;
import com.ad.vo.AdUnitDistrict.AdUnitDistrictRequest;
import com.ad.vo.AdUnitDistrict.AdUnitDistrictResponse;
import com.ad.vo.AdUnitIt.AdUnitItRequest;
import com.ad.vo.AdUnitIt.AdUnitItResponse;
import com.ad.vo.AdUnitKeyword.AdUnitKeywordRequest;
import com.ad.vo.AdUnitKeyword.AdUnitKeywordResponse;
import com.ad.vo.CreativeUnit.CreativeUnitRequest;
import com.ad.vo.CreativeUnit.CreativeUnitResponse;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 推广单元接口
 */
@Slf4j
@RestController
public class AdUnitOpController {

    @Autowired
    private IAdUnitService adUnitService;

    /**
     * 推广单元创建
     * @param request
     * @return
     * @throws AdException
     */
    @PostMapping("/create/adUnit")
    public AdUnitResponse createUnit(@RequestBody AdUnitRequest request) throws AdException {
        log.info("ad-sponsor: createUnit -> {}", JSON.toJSONString(request));
        return adUnitService.createUnit(request);
    }

    /**
     * 推广单元关键词创建
     * @param request
     * @return
     * @throws AdException
     */
    @PostMapping("/create/unitKeyword")
    public AdUnitKeywordResponse createUnitKeyword(@RequestBody AdUnitKeywordRequest request) throws AdException {
        log.info("ad-sponsor: createUnitKeyword -> {}", JSON.toJSONString(request));
        return adUnitService.createUnitKeyword(request);
    }

    /**
     * 推广单元兴趣创建
     * @param request
     * @return
     * @throws AdException
     */
    @PostMapping("/create/unitIt")
    public AdUnitItResponse createUnitIt(@RequestBody AdUnitItRequest request) throws AdException {
        log.info("ad-sponsor: createUnitIt -> {}", JSON.toJSONString(request));
        return adUnitService.createUnitIt(request);
    }

    /**
     * 推广单元地域创建
     * @param request
     * @return
     * @throws AdException
     */
    @PostMapping("/create/unitDistrict")
    public AdUnitDistrictResponse createUnitDistrict(@RequestBody AdUnitDistrictRequest request) throws AdException {
        log.info("ad-sponsor: createUnitIt -> {}", JSON.toJSONString(request));
        return adUnitService.createUnitDistrict(request);
    }

    /**
     * 创意与推广单元创建
     * @param request
     * @return
     * @throws AdException
     */
    @PostMapping("/create/creativeUnit")
    public CreativeUnitResponse createCreativeUnit(@RequestBody CreativeUnitRequest request) throws AdException {
        log.info("ad-sponsor: createUnitIt -> {}", JSON.toJSONString(request));
        return adUnitService.createCreativeUnit(request);
    }
}