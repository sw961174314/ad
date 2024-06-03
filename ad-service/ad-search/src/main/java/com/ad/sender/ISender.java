package com.ad.sender;

import com.ad.mysql.dto.MySqlRowData;

/**
 * 投递增量数据的实现接口
 */
public interface ISender {

    void sender(MySqlRowData rowData);
}