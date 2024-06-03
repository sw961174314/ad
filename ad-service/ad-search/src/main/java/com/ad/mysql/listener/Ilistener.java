package com.ad.mysql.listener;

import com.ad.mysql.dto.BinlogRowData;

/**
 * 监听处理方法
 */
public interface Ilistener {

    void register();

    void onEvent(BinlogRowData eventData);
}