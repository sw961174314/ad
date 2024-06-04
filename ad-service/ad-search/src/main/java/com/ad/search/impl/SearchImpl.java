package com.ad.search.impl;

import com.ad.index.AdUnit.AdUnitIndex;
import com.ad.index.AdUnit.AdUnitObject;
import com.ad.index.AdUnitDistrict.AdUnitDistrictIndex;
import com.ad.index.AdUnitIt.AdUnitItIndex;
import com.ad.index.AdUnitKeyword.AdUnitKeywordIndex;
import com.ad.index.CommonStatus;
import com.ad.index.Creative.CreativeIndex;
import com.ad.index.Creative.CreativeObject;
import com.ad.index.CreativeUnit.CreativeUnitIndex;
import com.ad.index.DataTable;
import com.ad.search.ISearch;
import com.ad.search.vo.SearchRequest;
import com.ad.search.vo.SearchResponse;
import com.ad.search.vo.feature.DistrictFeature;
import com.ad.search.vo.feature.FeatureRelation;
import com.ad.search.vo.feature.ItFeature;
import com.ad.search.vo.feature.KeywordFeature;
import com.ad.search.vo.media.AdSlot;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class SearchImpl implements ISearch {

    /**
     * 根据匹配信息实现对推广单元的筛选
     * @param request
     * @return
     */
    @Override
    public SearchResponse fetchAds(SearchRequest request) {
        // 请求的广告位信息
        List<AdSlot> adSlots = request.getRequestInfo().getAdSlots();

        // 三个限制Feature
        KeywordFeature keywordFeature = request.getFeatureInfo().getKeywordFeature();
        ItFeature itFeature = request.getFeatureInfo().getItFeature();
        DistrictFeature districtFeature = request.getFeatureInfo().getDistrictFeature();

        FeatureRelation relation = request.getFeatureInfo().getRelation();

        // 构造响应对象
        SearchResponse response = new SearchResponse();
        Map<String, List<SearchResponse.Creative>> adSlot2Ads = response.getAdSlot2Ads();
        for (AdSlot adSlot : adSlots) {
            Set<Long> targetUnitIdSet;

            // 根据流量类型获取初始AdUnit(初过滤)
            Set<Long> adUnitIdSet = DataTable.of(AdUnitIndex.class).match(adSlot.getPositionType());

            if (relation == FeatureRelation.AND) {
                filterKeywordFeature(adUnitIdSet,keywordFeature);
                filterItTagFeature(adUnitIdSet,itFeature);
                filterDistrictFeature(adUnitIdSet,districtFeature);
                targetUnitIdSet = adUnitIdSet;
            } else {
                targetUnitIdSet = getORRelationUnitIds(adUnitIdSet, keywordFeature, districtFeature, itFeature);
            }
            List<AdUnitObject> unitObjects = DataTable.of(AdUnitIndex.class).fetch(targetUnitIdSet);
            filterAdUnitAndPlanStatus(unitObjects, CommonStatus.VALID);

            List<Long> adIds = DataTable.of(CreativeUnitIndex.class).selectAds(unitObjects);
            List<CreativeObject> creatives = DataTable.of(CreativeIndex.class).fetch(adIds);

            // 通过AdSlot实现对CreativeObject的过滤
            filterCreativeByAdSlot(creatives, adSlot.getWidth(), adSlot.getHeight(), adSlot.getType());

            adSlot2Ads.put(adSlot.getAdSlotCode(), buildCreativeResponse(creatives));
        }
        log.info("fetchAds: {}-{}", JSON.toJSONString(request), JSON.toJSONString(response));
        return response;
    }

    private void filterKeywordFeature(Collection<Long> adUnitIds, KeywordFeature keywordFeature) {
        if (CollectionUtils.isEmpty(adUnitIds)) {
            return;
        }
        if (CollectionUtils.isNotEmpty(keywordFeature.getKeywords())) {
            CollectionUtils.filter(adUnitIds, adUnitId -> DataTable.of(AdUnitKeywordIndex.class).match(adUnitId,keywordFeature.getKeywords()));
        }
    }

    private void filterItTagFeature(Collection<Long> adUnitIds, ItFeature itFeature) {
        if (CollectionUtils.isEmpty(adUnitIds)) {
            return;
        }
        if (CollectionUtils.isNotEmpty(itFeature.getIts())) {
            CollectionUtils.filter(adUnitIds, adUnitId -> DataTable.of(AdUnitItIndex.class).match(adUnitId,itFeature.getIts()));
        }
    }

    private void filterDistrictFeature(Collection<Long> adUnitIds, DistrictFeature districtFeature) {
        if (CollectionUtils.isEmpty(adUnitIds)) {
            return;
        }
        if (CollectionUtils.isNotEmpty(districtFeature.getDistricts())) {
            CollectionUtils.filter(adUnitIds, adUnitId -> DataTable.of(AdUnitDistrictIndex.class).match(adUnitId,districtFeature.getDistricts()));
        }
    }

    private Set<Long> getORRelationUnitIds(Set<Long> adUnitIdSet, KeywordFeature keywordFeature, DistrictFeature districtFeature, ItFeature itFeature) {

        if (CollectionUtils.isEmpty(adUnitIdSet)) {
            return Collections.emptySet();
        }

        Set<Long> keywordUnitIdSet = new HashSet<>(adUnitIdSet);
        Set<Long> districtUnitIdSet = new HashSet<>(adUnitIdSet);
        Set<Long> itUnitIdSet = new HashSet<>(adUnitIdSet);

        filterKeywordFeature(keywordUnitIdSet, keywordFeature);
        filterDistrictFeature(districtUnitIdSet, districtFeature);
        filterItTagFeature(itUnitIdSet, itFeature);

        // 取三个集合的并集
        return new HashSet<>(CollectionUtils.union(CollectionUtils.union(keywordUnitIdSet, districtUnitIdSet), itUnitIdSet));
    }

    /**
     * 根据推广单元和推广计划的状态进行过滤
     * @param unitObjects
     * @param status
     */
    private void filterAdUnitAndPlanStatus(List<AdUnitObject> unitObjects, CommonStatus status) {
        if (CollectionUtils.isEmpty(unitObjects)) {
            return;
        }
        CollectionUtils.filter(unitObjects, object -> object.getUnitStatus().equals(status.getStatus()) && object.getAdPlanObject().getPlanStatus().equals(status.getStatus()));
    }

    /**
     * 根据创意内容进行过滤
     * @param creatives
     * @param width
     * @param height
     * @param type
     */
    private void filterCreativeByAdSlot(List<CreativeObject> creatives, Integer width, Integer height, List<Integer> type) {
        if (CollectionUtils.isEmpty(creatives)) {
            return;
        }
        CollectionUtils.filter(creatives, creative -> creative.getAuditStatus().equals(CommonStatus.VALID.getStatus()) && creative.getWidth().equals(width) && creative.getHeight().equals(height) && type.contains(creative.getType()));
    }

    /**
     * 在列表中随机获取一个Creative进行展示
     * @param creatives
     * @return
     */
    private List<SearchResponse.Creative> buildCreativeResponse(List<CreativeObject> creatives) {
        if (CollectionUtils.isEmpty(creatives)) {
            return Collections.emptyList();
        }
        // 随机获取
        CreativeObject randomObject = creatives.get(Math.abs(new Random().nextInt()) % creatives.size());
        return Collections.singletonList(SearchResponse.convert(randomObject));
    }
}