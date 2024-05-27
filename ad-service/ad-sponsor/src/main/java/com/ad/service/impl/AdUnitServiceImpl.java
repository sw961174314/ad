package com.ad.service.impl;

import com.ad.constant.Constants;
import com.ad.dao.AdPlanRepository;
import com.ad.dao.AdUnitRepository;
import com.ad.entity.AdPlan;
import com.ad.entity.AdUnit;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 推广单元接口实现
 */
@Slf4j
@Service
public class AdUnitServiceImpl implements IAdUnitService {

    @Autowired
    private AdPlanRepository planRepository;

    @Autowired
    private AdUnitRepository unitRepository;

    @Override
    public AdUnitResponse createUnit(AdUnitRequest request) throws AdException {
        if (!request.createValidate()) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        Optional<AdPlan> adPlan = planRepository.findById(request.getPlanId());
        if (!adPlan.isPresent()) {
            throw new AdException(Constants.ErrorMsg.CAN_NOT_FIND_RECORD);
        }

        AdUnit oldAdUnit = unitRepository.findByPlanIdAndUnitName(request.getPlanId(), request.getUnitName());
        if (oldAdUnit != null) {
            throw new AdException(Constants.ErrorMsg.SAME_NAME_UNIT_ERROR);
        }

        AdUnit newAdUnit = unitRepository.save(new AdUnit(request.getPlanId(), request.getUnitName(), request.getPositionType(), request.getBudget()));
        return new AdUnitResponse(newAdUnit.getId(), newAdUnit.getUnitName());
    }

    /**
     * 推广单元关键词创建
     * @param request
     * @return
     * @throws AdException
     */
    @Override
    public AdUnitKeywordResponse createUnitKeyword(AdUnitKeywordRequest request) throws AdException {
        return null;
    }

    /**
     * 推广单元兴趣创建
     * @param request
     * @return
     * @throws AdException
     */
    @Override
    public AdUnitItResponse createUnitIt(AdUnitItRequest request) throws AdException {
        return null;
    }

    /**
     * 推广单元地域创建
     * @param request
     * @return
     * @throws AdException
     */
    @Override
    public AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request) throws AdException {
        return null;
    }
}