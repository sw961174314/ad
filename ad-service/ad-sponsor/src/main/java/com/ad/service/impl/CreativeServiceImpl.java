package com.ad.service.impl;

import com.ad.dao.CreativeRepository;
import com.ad.entity.Creative;
import com.ad.service.ICreativeService;
import com.ad.vo.Creative.CreativeRequest;
import com.ad.vo.Creative.CreativeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 创意接口实现
 */
@Slf4j
@Service
public class CreativeServiceImpl implements ICreativeService {

    @Autowired
    private CreativeRepository creativeRepository;

    /**
     * 创意创建
     * @param request
     * @return
     */
    @Override
    public CreativeResponse createCreative(CreativeRequest request) {
        Creative creative = creativeRepository.save(request.converToEntity());
        return new CreativeResponse(creative.getId(), creative.getName());
    }
}