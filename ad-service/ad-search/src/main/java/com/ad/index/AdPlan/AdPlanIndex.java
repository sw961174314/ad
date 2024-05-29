package com.ad.index.AdPlan;

import com.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 推广计划索引
 */
@Slf4j
@Component
public class AdPlanIndex implements IndexAware<Long, AdPlanObject> {

    private static Map<Long, AdPlanObject> objectMap;

    // 初始化map
    static {
        // 使用ConcurrentHashMap保证线程安全
        objectMap = new ConcurrentHashMap<>();
    }

    @Override
    public AdPlanObject get(Long key) {
        return objectMap.get(key);
    }

    @Override
    public void add(Long key, AdPlanObject value) {
        log.info("AdPlanIndex, before add: {}", objectMap);
        objectMap.put(key, value);
        log.info("AdPlanIndex, after add: {}", objectMap);
    }

    @Override
    public void update(Long key, AdPlanObject value) {
        log.info("AdPlanIndex, before update: {}", objectMap);
        AdPlanObject oldObject = objectMap.get(key);
        if (null == oldObject) {
            objectMap.put(key, value);
        } else {
            oldObject.update(value);
        }
        log.info("AdPlanIndex, after update: {}", objectMap);
    }

    @Override
    public void delete(Long key, AdPlanObject value) {
        log.info("AdPlanIndex, before delete: {}", objectMap);
        objectMap.remove(key);
        log.info("AdPlanIndex, after delete: {}", objectMap);
    }
}