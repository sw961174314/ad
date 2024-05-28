package com.ad.service;

import com.ad.exception.AdException;
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

/**
 * 推广单元服务功能
 */
public interface IAdUnitService {

    // 推广单元创建
    AdUnitResponse createUnit(AdUnitRequest request) throws AdException;

    // 推广单元关键词创建
    AdUnitKeywordResponse createUnitKeyword(AdUnitKeywordRequest request) throws AdException;

    // 推广单元兴趣创建
    AdUnitItResponse createUnitIt(AdUnitItRequest request) throws AdException;

    // 推广单元地域创建
    AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request) throws AdException;

    // 创意与推广单元创建
    CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request) throws AdException;
}