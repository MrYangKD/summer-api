package com.cn.summer.api.mapper.dataSource.pool;

import javax.sql.DataSource;
import java.util.concurrent.ConcurrentMap;

/**
 * .
 *
 * @author YangYK
 * @since 1.0
 */
public interface Pool {
    /**
     * 清理连接池.
     */
    void closeAndClear();

    /**
     * 获取dataSource.
     * @param envId envId
     * @return DataSource
     */
    DataSource getPool(String envId);

    /**
     * 获取数据源集合.
     * @return ConcurrentMap
     */
    ConcurrentMap<String, DataSource> getDataSources();
}
