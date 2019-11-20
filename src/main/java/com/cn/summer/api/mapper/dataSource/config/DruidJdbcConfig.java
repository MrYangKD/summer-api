package com.cn.summer.api.mapper.dataSource.config;

import com.cn.summer.api.common.util.JobAppUtil;
import lombok.Data;
import org.springframework.core.env.Environment;

/**
 * .
 *
 * @author YangYK
 * @since 1.0
 */
@Data
public class DruidJdbcConfig extends JdbcConfig{
    private Integer initialSize;

    private Integer maxActive;

    private Integer maxIdle;

    private Integer minIdle;

    private Long maxWait;

    private Boolean removeAbandoned;

    private Integer removeAbandonedTimeout;

    private Long timeBetweenEvictionRunsMillis;

    private Long minEvictableIdleTimeMillis;

    private String validationQuery;

    private Boolean testWhileIdle;

    private Boolean testOnBorrow;

    private Boolean testOnReturn;

    private Boolean poolPreparedStatements;

    private Integer maxPoolPreparedStatementPerConnectionSize;

    private String filters;

    /**
     * 无参构造器.
     */
    public DruidJdbcConfig() {
        super();
    }

    /**
     * 初始化druid参数.
     * @param env env
     * @param environmentId environmentId
     * @param driver driver
     * @param url url
     * @param userName userName
     * @param passwd passwd
     */
    public DruidJdbcConfig(final Environment env, final String environmentId, final String driver, final String url, final String userName, final String passwd) {
        super(environmentId, driver, url, userName, passwd);
        this.initialSize = JobAppUtil.isNotNull(env.getProperty("druid.initialSize")) ? Integer.valueOf(env.getProperty("druid.initialSize")) : null;
        this.maxActive = JobAppUtil.isNotNull(env.getProperty("druid.maxActive")) ? Integer.valueOf(env.getProperty("druid.maxActive")) : null;
        this.maxIdle = JobAppUtil.isNotNull(env.getProperty("druid.maxIdle")) ? Integer.valueOf(env.getProperty("druid.maxIdle")) : null;
        this.minIdle = JobAppUtil.isNotNull(env.getProperty("druid.minIdle")) ? Integer.valueOf(env.getProperty("druid.minIdle")) : null;
        this.maxWait = JobAppUtil.isNotNull(env.getProperty("druid.maxWait")) ? Long.valueOf(env.getProperty("druid.maxWait")) : null;
        this.removeAbandoned = JobAppUtil.isNotNull(env.getProperty("druid.removeAbandoned")) ? Boolean.valueOf(env.getProperty("druid.removeAbandoned")) : null;
        this.removeAbandonedTimeout = JobAppUtil.isNotNull(env.getProperty("druid.removeAbandonedTimeout")) ? Integer.valueOf(env.getProperty("druid.removeAbandonedTimeout")) : null;
        this.timeBetweenEvictionRunsMillis = JobAppUtil.isNotNull(env.getProperty("druid.timeBetweenEvictionRunsMillis"))
                ?  Long.valueOf(env.getProperty("druid.timeBetweenEvictionRunsMillis")) : null;
        this.minEvictableIdleTimeMillis = JobAppUtil.isNotNull(env.getProperty("druid.minEvictableIdleTimeMillis")) ? Long.valueOf(env.getProperty("druid.minEvictableIdleTimeMillis")) : null;
        this.validationQuery = env.getProperty("druid.validationQuery");
        this.testWhileIdle = JobAppUtil.isNotNull(env.getProperty("druid.testWhileIdle")) ? Boolean.valueOf(env.getProperty("druid.testWhileIdle")) : null;
        this.testOnBorrow = JobAppUtil.isNotNull(env.getProperty("druid.testOnBorrow")) ? Boolean.valueOf(env.getProperty("druid.testOnBorrow")) : null;
        this.testOnReturn = JobAppUtil.isNotNull(env.getProperty("druid.testOnReturn")) ? Boolean.valueOf(env.getProperty("druid.testOnReturn")) : null;
        this.poolPreparedStatements = JobAppUtil.isNotNull(env.getProperty("druid.poolPreparedStatements")) ? Boolean.valueOf(env.getProperty("druid.poolPreparedStatements")) : null;
        this.maxPoolPreparedStatementPerConnectionSize = JobAppUtil.isNotNull(env.getProperty("druid.maxPoolPreparedStatementPerConnectionSize"))
                ? Integer.valueOf(env.getProperty("druid.maxPoolPreparedStatementPerConnectionSize")) : null;
        this.filters = env.getProperty("druid.filters");
    }


    @Override
    public String toString() {
        return "DruidJdbcConfig{"
                + "initialSize=" + initialSize
                + ", maxActive=" + maxActive
                + ", maxIdle=" + maxIdle
                + ", minIdle=" + minIdle
                + ", maxWait=" + maxWait
                + ", removeAbandoned=" + removeAbandoned
                + ", removeAbandonedTimeout=" + removeAbandonedTimeout
                + ", timeBetweenEvictionRunsMillis=" + timeBetweenEvictionRunsMillis
                + ", minEvictableIdleTimeMillis=" + minEvictableIdleTimeMillis
                + ", validationQuery='" + validationQuery + '\''
                + ", testWhileIdle=" + testWhileIdle
                + ", testOnBorrow=" + testOnBorrow
                + ", testOnReturn=" + testOnReturn
                + ", poolPreparedStatements=" + poolPreparedStatements
                + ", maxPoolPreparedStatementPerConnectionSize=" + maxPoolPreparedStatementPerConnectionSize
                + ", filters='" + filters + '\''
                + '}';
    }
}
