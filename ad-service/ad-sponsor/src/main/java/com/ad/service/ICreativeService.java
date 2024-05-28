package com.ad.service;

import com.ad.vo.Creative.CreativeRequest;
import com.ad.vo.Creative.CreativeResponse;

/**
 * 创意服务功能
 */
public interface ICreativeService {

    // 创意创建
    CreativeResponse createCreative(CreativeRequest request);
}