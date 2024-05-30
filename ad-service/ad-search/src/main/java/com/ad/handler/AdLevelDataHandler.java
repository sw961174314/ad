package com.ad.handler;

import com.ad.dump.table.*;
import com.ad.index.AdPlan.AdPlanIndex;
import com.ad.index.AdPlan.AdPlanObject;
import com.ad.index.AdUnit.AdUnitIndex;
import com.ad.index.AdUnit.AdUnitObject;
import com.ad.index.AdUnitDistrict.AdUnitDistrictIndex;
import com.ad.index.AdUnitIt.AdUnitItIndex;
import com.ad.index.AdUnitKeyword.AdUnitKeywordIndex;
import com.ad.index.Creative.CreativeIndex;
import com.ad.index.Creative.CreativeObject;
import com.ad.index.CreativeUnit.CreativeUnitIndex;
import com.ad.index.CreativeUnit.CreativeUnitObject;
import com.ad.index.DataTable;
import com.ad.index.IndexAware;
import com.ad.mysql.constant.OpType;
import com.ad.utils.CommonUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 索引处理
 * 1.索引之间存在着层级的划分 也就是依赖关系的划分
 * 2.加载全量索引其实是增量索引"添加"的一种特殊实现
 */
@Slf4j
public class AdLevelDataHandler {

    /**
     * 实现对索引增删改的过程
     * @param index
     * @param key
     * @param value
     * @param type
     * @param <K>
     * @param <V>
     */
    private static <K, V> void handlerBinlogEvent(IndexAware<K, V> index, K key, V value, OpType type) {
        switch (type) {
            case ADD:
                index.add(key, value);
                break;
            case UPDATE:
                index.update(key, value);
                break;
            case DELETE:
                index.delete(key, value);
                break;
            default:
                break;
        }
    }

    /**
     * 第二层级索引操作
     * @param planTable
     * @param type
     */
    public static void handleLevel2(AdPlanTable planTable, OpType type) {
        AdPlanObject planObject = new AdPlanObject(planTable.getId(), planTable.getUserId(), planTable.getPlanStatus(), planTable.getStartDate(), planTable.getEndDate());
        handlerBinlogEvent(DataTable.of(AdPlanIndex.class), planObject.getPlanId(), planObject, type);
    }

    /**
     * 第二层级索引操作
     * @param creativeTable
     * @param type
     */
    public static void handleLevel2(AdCreativeTable creativeTable, OpType type) {
        CreativeObject creativeObject = new CreativeObject(creativeTable.getAdId(), creativeTable.getName(), creativeTable.getType(), creativeTable.getMaterialType(), creativeTable.getHeight(), creativeTable.getWidth(), creativeTable.getAuditStatus(), creativeTable.getAdUrl());
        handlerBinlogEvent(DataTable.of(CreativeIndex.class), creativeObject.getAdId(), creativeObject, type);
    }

    /**
     * 第三层级索引操作(AdUnit跟AdPlan相关 AdUnit依赖于AdPlan)
     * @param unitTable
     * @param type
     */
    public static void handleLevel3(AdUnitTable unitTable, OpType type) {
        AdPlanObject adPlanObject = DataTable.of(AdPlanIndex.class).get(unitTable.getPlanId());
        if (null == adPlanObject) {
            log.error("handleLevel3 found AdPlanObject error: {}", unitTable.getPlanId());
            return;
        }

        AdUnitObject unitObject = new AdUnitObject(unitTable.getUnitId(), unitTable.getUnitStatus(), unitTable.getPositionType(), unitTable.getPlanId(), adPlanObject);
        handlerBinlogEvent(DataTable.of(AdUnitIndex.class), unitTable.getUnitId(), unitObject, type);
    }

    /**
     * 第三层级索引操作(AdCreativeUnit跟AdUnit和Creative相关 AdCreativeUnit依赖于AdUnit和Creative)
     * @param creativeUnitTable
     * @param type
     */
    public static void handleLevel3(AdCreativeUnitTable creativeUnitTable, OpType type) {
        if (type == OpType.UPDATE) {
            log.error("CreativeUnitIndex not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(creativeUnitTable.getUnitId());
        CreativeObject creativeObject = DataTable.of(CreativeIndex.class).get(creativeUnitTable.getAdId());
        if (null == unitObject || null == creativeObject) {
            log.error("AdCreativeUnitTable index error: {}", JSON.toJSONString(creativeUnitTable));
            return;
        }

        CreativeUnitObject creativeUnitObject = new CreativeUnitObject(creativeUnitTable.getAdId(), unitObject.getUnitId());
        handlerBinlogEvent(DataTable.of(CreativeUnitIndex.class), CommonUtils.stringConcat(creativeUnitObject.getAdId().toString(),creativeUnitObject.getUnitId().toString()), creativeUnitObject, type);
    }

    /**
     * 第四层级索引操作(AdUnitKeyword和AdUnit相关 AdUnitKeyword依赖于AdUnit)
     * @param unitKeywordTable
     * @param type
     */
    public static void handleLevel4(AdUnitKeywordTable unitKeywordTable, OpType type) {
        if (type == OpType.UPDATE){
            log.error("keyword index can not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitKeywordTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitItTable index error: {}", unitKeywordTable.getUnitId());
            return;
        }
        Set<Long> value = new HashSet<>(Collections.singleton(unitKeywordTable.getUnitId()));
        handlerBinlogEvent(DataTable.of(AdUnitKeywordIndex.class), unitKeywordTable.getKeyword(), value, type);
    }

    /**
     * 第四层级索引操作(AdUnitIt和AdUnit相关 AdUnitIt依赖于AdUnit)
     * @param unitItTable
     * @param type
     */
    public static void handleLevel4(AdUnitItTable unitItTable, OpType type) {
        if (type == OpType.UPDATE){
            log.error("it index can not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitItTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitItTable index error: {}", unitItTable.getUnitId());
            return;
        }
        Set<Long> value = new HashSet<>(Collections.singleton(unitItTable.getUnitId()));
        handlerBinlogEvent(DataTable.of(AdUnitItIndex.class), unitItTable.getItTag(), value, type);
    }

    /**
     * 第四层级索引操作(AdUnitDistrict和AdUnit相关 AdUnitDistrict依赖于AdUnit)
     * @param unitDistrictTable
     * @param type
     */
    public static void handleLevel4(AdUnitDistrictTable unitDistrictTable, OpType type) {
        if (type == OpType.UPDATE){
            log.error("district index can not support update");
            return;
        }
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitDistrictTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitDistrictTable index error: {}", unitDistrictTable.getUnitId());
            return;
        }

        String key = CommonUtils.stringConcat(unitDistrictTable.getProvince(), unitDistrictTable.getCity());
        Set<Long> value = new HashSet<>(Collections.singleton(unitDistrictTable.getUnitId()));
        handlerBinlogEvent(DataTable.of(AdUnitDistrictIndex.class), key, value, type);
    }
}