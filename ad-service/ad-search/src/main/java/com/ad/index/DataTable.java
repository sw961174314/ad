package com.ad.index;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 索引缓存
 */
@Component
public class DataTable implements ApplicationContextAware, PriorityOrdered {

    private static ApplicationContext applicationContext;

    public static final Map<Class, Object> dataTableMap = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DataTable.applicationContext = applicationContext;
    }

    /**
     * 优先级排序
     * @return
     */
    @Override
    public int getOrder() {
        // 优先初始化
        return PriorityOrdered.HIGHEST_PRECEDENCE;
    }

    /**
     * 通过beanName获取容器中的bean
     * @param beanName
     * @return
     * @param <T>
     */
    @SuppressWarnings("all")
    private static <T> T bean(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }

    /**
     * 获取索引服务类
     * @param clazz
     * @return
     * @param <T>
     */
    @SuppressWarnings("all")
    public static <T> T of(Class<T> clazz) {
        // 获取索引类型
        T instance = (T) dataTableMap.get(clazz);
        if (null != instance) {
            return instance;
        }
        dataTableMap.put(clazz, bean(clazz));
        return (T) dataTableMap.get(clazz);
    }

    /**
     * 通过class类型获取容器中的bean
     * @param clazz
     * @return
     * @param <T>
     */
    @SuppressWarnings("all")
    private static <T> T bean(Class clazz) {
        return (T) applicationContext.getBean(clazz);
    }
}