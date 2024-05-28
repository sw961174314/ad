package com.ad.service.impl;

import com.ad.constant.Constants;
import com.ad.dao.AdPlanRepository;
import com.ad.dao.AdUnitRepository;
import com.ad.dao.CreativeRepository;
import com.ad.dao.unit_condition.AdUnitDistrictRepository;
import com.ad.dao.unit_condition.AdUnitItRepository;
import com.ad.dao.unit_condition.AdUnitKeywordRepository;
import com.ad.dao.unit_condition.CreativeUnitRepository;
import com.ad.entity.AdPlan;
import com.ad.entity.AdUnit;
import com.ad.entity.unit_condition.AdUnitDistrict;
import com.ad.entity.unit_condition.AdUnitIt;
import com.ad.entity.unit_condition.AdUnitKeyword;
import com.ad.entity.unit_condition.CreativeUnit;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private AdUnitKeywordRepository unitKeywordRepository;

    @Autowired
    private AdUnitItRepository unitItRepository;

    @Autowired
    private AdUnitDistrictRepository unitDistrictRepository;

    @Autowired
    private CreativeRepository creativeRepository;

    @Autowired
    private CreativeUnitRepository creativeUnitRepository;

    /**
     * 推广单元创建
     * @param request
     * @return
     * @throws AdException
     */
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
        List<Long> unitIds = request.getUnitKeywords().stream().map(AdUnitKeywordRequest.UnitKeyword::getUnitId).collect(Collectors.toList());
        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        List<Long> ids = Collections.emptyList();
        List<AdUnitKeyword> unitKeywords = new ArrayList<>();
        // 将request中的UnitKeyword的unitId和keyword存储到unitKeywords中
        if (!CollectionUtils.isEmpty(request.getUnitKeywords())) {
            request.getUnitKeywords().forEach(i -> unitKeywords.add(new AdUnitKeyword(i.getUnitId(), i.getKeyword())));
            // 将unitKeywordRepository.saveAll(unitKeywords)得到的主键存储到ids中
            ids = unitKeywordRepository.saveAll(unitKeywords).stream().map(AdUnitKeyword::getId).collect(Collectors.toList());
        }

        return new AdUnitKeywordResponse(ids);
    }

    /**
     * 推广单元兴趣创建
     * @param request
     * @return
     * @throws AdException
     */
    @Override
    public AdUnitItResponse createUnitIt(AdUnitItRequest request) throws AdException {
        List<Long> unitIds = request.getUnitIts().stream().map(AdUnitItRequest.UnitIt::getUnitId).collect(Collectors.toList());
        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        List<Long> ids = Collections.emptyList();
        List<AdUnitIt> unitIts = new ArrayList<>();
        // 将request中的UnitIt的unitId和itTag存储到unitIts中
        if (!CollectionUtils.isEmpty(request.getUnitIts())) {
            request.getUnitIts().forEach(i -> unitIts.add(new AdUnitIt(i.getUnitId(), i.getItTag())));
            // unitItRepository.saveAll(unitIts)得到的主键存储到ids中
            ids = unitItRepository.saveAll(unitIts).stream().map(AdUnitIt::getId).collect(Collectors.toList());
        }

        return new AdUnitItResponse(ids);
    }

    /**
     * 推广单元地域创建
     * @param request
     * @return
     * @throws AdException
     */
    @Override
    public AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request) throws AdException {
        List<Long> unitIds = request.getUnitDistricts().stream().map(AdUnitDistrictRequest.UnitDistrict::getUnitId).collect(Collectors.toList());
        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        List<Long> ids = Collections.emptyList();
        List<AdUnitDistrict> unitDistricts = new ArrayList<>();
        // 将request中的UnitDistrict的unitId province city存储到unitDistricts中
        if (!CollectionUtils.isEmpty(request.getUnitDistricts())) {
            request.getUnitDistricts().forEach(i -> unitDistricts.add(new AdUnitDistrict(i.getUnitId(), i.getProvince(), i.getCity())));
            // unitDistrictRepository.saveAll(unitDistricts)得到的主键存储到ids中
            ids = unitDistrictRepository.saveAll(unitDistricts).stream().map(AdUnitDistrict::getId).collect(Collectors.toList());
        }

        return new AdUnitDistrictResponse(ids);
    }

    /**
     * 创意与推广单元创建
     * @param request
     * @return
     * @throws AdException
     */
    @Override
    public CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request) throws AdException {
        List<Long> unitIds = request.getUnitItems().stream().map(CreativeUnitRequest.CreativeUnitItem::getUnitId).collect(Collectors.toList());
        List<Long> creativeIds = request.getUnitItems().stream().map(CreativeUnitRequest.CreativeUnitItem::getCreativeId).collect(Collectors.toList());
        if (!(isRelatedUnitExist(unitIds) && isRelatedCreativeExist(creativeIds))) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        List<Long> ids = Collections.emptyList();
        List<CreativeUnit> creativeUnits = new ArrayList<>();
        request.getUnitItems().forEach(i -> creativeUnits.add(new CreativeUnit(i.getCreativeId(), i.getUnitId())));
        ids = creativeUnitRepository.saveAll(creativeUnits).stream().map(CreativeUnit::getId).collect(Collectors.toList());

        return new CreativeUnitResponse(ids);
    }

    /**
     * 判断推广单元是否存在
     * @param unitIds
     * @return
     */
    private boolean isRelatedUnitExist(List<Long> unitIds) {
        if (CollectionUtils.isEmpty(unitIds)) {
            return false;
        }

        return unitRepository.findAllById(unitIds).size() == new HashSet<>(unitIds).size();
    }

    /**
     * 判断创意是否存在
     * @param creativeIds
     * @return
     */
    private boolean isRelatedCreativeExist(List<Long> creativeIds) {
        if (CollectionUtils.isEmpty(creativeIds)) {
            return false;
        }

        return creativeRepository.findAllById(creativeIds).size() == new HashSet<>(creativeIds).size();
    }
}