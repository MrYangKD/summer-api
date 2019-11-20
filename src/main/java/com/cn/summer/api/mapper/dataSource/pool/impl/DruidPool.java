package com.cn.summer.api.mapper.dataSource.pool.impl;

import ch.qos.logback.classic.Logger;
import com.cn.summer.api.mapper.dataSource.DynamicDataSourceRegister;
import com.cn.summer.api.mapper.dataSource.config.DruidJdbcConfig;
import com.cn.summer.api.mapper.dataSource.config.JdbcConfig;
import com.cn.summer.api.mapper.dataSource.pool.Pool;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * .
 *
 * @author YangYK
 * @since 1.0
 */
public class DruidPool extends DynamicDataSourceRegister implements Pool {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(DruidPool.class);

    private final ConcurrentMap<String, DataSource> dataSources = new ConcurrentHashMap<>();

    private Class<?> druidDataSource;{
        try {
            druidDataSource = Class.forName("com.alibaba.druid.pool.DruidDataSource");
        } catch(final ClassNotFoundException e) {
            LOGGER.warn("Unload class [ com.alibaba.druid.pool.DruidDataSource ]");
        }
    }
    @Override
    public void closeAndClear() {
        dataSources.forEach((envId, dataSource) -> {
            try {
                druidDataSource.getMethod("close").invoke(dataSource);
            } catch(final Exception e) {

            }
        });

        dataSources.clear();

    }

    /**
     * druid初始化参数
     * @param configs
     */
    public DruidPool(final Collection<JdbcConfig> configs){
        Assert.notNull(configs);
        Assert.notEmpty(configs);
        if (dataSources != null && !dataSources.isEmpty()) {
            closeAndClear();
        }
        List<DruidJdbcConfig> druidJdbcConfigs = new ArrayList<>();
        configs.forEach(config -> druidJdbcConfigs.add((DruidJdbcConfig) config));
        for (DruidJdbcConfig config : druidJdbcConfigs) {
            DataSource dataSource;
            try {
                dataSource = (DataSource) druidDataSource.newInstance();
            } catch (final Exception e) {
                throw new RuntimeException("Instance Constructor Exception: " + druidDataSource.getName());
            }

            try {
                druidDataSource.getMethod("setDriverClassName", String.class).invoke(dataSource, config.getDriver());
                druidDataSource.getMethod("setUrl", String.class).invoke(dataSource, config.getUrl());
                druidDataSource.getMethod("setUsername", String.class).invoke(dataSource, config.getUserName());
                druidDataSource.getMethod("setPassword", String.class).invoke(dataSource, config.getPasswd());

                setDruidParams(dataSource, config);
            } catch(final Exception e) {
                throw new IllegalArgumentException("设置参数异常: " + e.getMessage());
            }

            dataSources.put(config.getEnvironmentId(), dataSource);
        }


    }
    private void setDruidParams(final DataSource dataSource, final DruidJdbcConfig config) throws Exception{

        if (config.getInitialSize() != null){
            druidDataSource.getMethod("setInitialSize", int.class).invoke(dataSource, config.getInitialSize());
        }
        if (config.getMaxActive() != null){
            druidDataSource.getMethod("setMaxActive", int.class).invoke(dataSource, config.getMaxActive());
        }
        if (config.getMaxIdle() != null){
            druidDataSource.getMethod("setMaxIdle", int.class).invoke(dataSource, config.getMaxIdle());
        }
        if (config.getMinIdle() != null){
            druidDataSource.getMethod("setMinIdle", int.class).invoke(dataSource, config.getMinIdle());
        }
        if (config.getMaxWait() != null){
            druidDataSource.getMethod("setMaxWait", long.class).invoke(dataSource, config.getMaxWait());
        }
        if (config.getRemoveAbandoned() != null){
            druidDataSource.getMethod("setRemoveAbandoned", boolean.class).invoke(dataSource, config.getRemoveAbandoned());
        }
        if (config.getRemoveAbandonedTimeout() != null){
            druidDataSource.getMethod("setRemoveAbandonedTimeout", int.class).invoke(dataSource, config.getRemoveAbandonedTimeout());
        }
        if (config.getTimeBetweenEvictionRunsMillis() != null){
            druidDataSource.getMethod("setTimeBetweenEvictionRunsMillis", long.class).invoke(dataSource, config.getTimeBetweenEvictionRunsMillis());
        }
        if (config.getMinEvictableIdleTimeMillis() != null){
            druidDataSource.getMethod("setMinEvictableIdleTimeMillis", long.class).invoke(dataSource, config.getMinEvictableIdleTimeMillis());
        }
        if (config.getValidationQuery() != null){
            druidDataSource.getMethod("setValidationQuery", String.class).invoke(dataSource, config.getValidationQuery());
        }
        if (config.getTestWhileIdle() != null){
            druidDataSource.getMethod("setTestWhileIdle", boolean.class).invoke(dataSource, config.getTestWhileIdle());
        }
        if (config.getTestOnBorrow() != null){
            druidDataSource.getMethod("setTestOnBorrow", boolean.class).invoke(dataSource, config.getTestOnBorrow());
        }
        if (config.getTestOnReturn() != null){
            druidDataSource.getMethod("setTestOnReturn", boolean.class).invoke(dataSource, config.getTestOnReturn());
        }
        if (config.getPoolPreparedStatements() != null){
            druidDataSource.getMethod("setPoolPreparedStatements", boolean.class).invoke(dataSource, config.getPoolPreparedStatements());
        }
        if (config.getMaxPoolPreparedStatementPerConnectionSize() != null){
            druidDataSource.getMethod("setMaxPoolPreparedStatementPerConnectionSize", int.class).invoke(dataSource, config.getMaxPoolPreparedStatementPerConnectionSize());
        }
        if (config.getFilters() != null){
            druidDataSource.getMethod("setFilters", String.class).invoke(dataSource, config.getFilters());
        }
    }

    @Override
    public DataSource getPool(final String envId) {
        return dataSources.get(envId);
    }

    @Override
    public ConcurrentMap<String, DataSource> getDataSources() {
        return dataSources;
    }
}
