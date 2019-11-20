package com.cn.summer.api.mapper.dataSource;

import ch.qos.logback.classic.Logger;
import com.cn.summer.api.mapper.dataSource.config.DruidJdbcConfig;
import com.cn.summer.api.mapper.dataSource.config.JdbcConfig;
import com.cn.summer.api.mapper.dataSource.pool.Pool;
import com.cn.summer.api.mapper.dataSource.pool.impl.DruidPool;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .
 * 动态数据源注册器
 * @author YangYK
 * @since 1.0
 */
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar,EnvironmentAware{
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(DynamicDataSourceRegister.class);

    /** 数据源集合*/
    private Map<String, DataSource> customDataSource = Maps.newHashMap();

    private Pool pool;
    /**
     * 初始化多数据源.
     * @param environment environment
     */
    @Override
    public void setEnvironment(final Environment environment) {
        try {
            initCustomDataSources(environment);
        } catch (final Exception e) {
            LOGGER.error("DynamicDataSourceRegister - setEnvironment() error", e);
        }
    }

    /**
     * 多数据源注册.
     * @param annotationMetadata
     * @param beanDefinitionRegistry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
        //添加多数据源
        targetDataSources.putAll(customDataSource);
        for (String key : customDataSource.keySet()){
            DynamicDataSourceContextHolder.setDataSourceId(key);
        }
        //创建DynamicDataSouce
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        mpv.addPropertyValue("targetDataSources", targetDataSources);
        //注册 - BeanDefinitionRegistry
        beanDefinitionRegistry.registerBeanDefinition("dataSource", beanDefinition);
        LOGGER.info("Dynamic DataSource Registry");

    }

    /**
     * 初始化多数据源.
     * @param env env
     */
    private void initCustomDataSources(final Environment env) throws Exception {

        List<JdbcConfig> configs = Lists.newArrayList();
        // 读取配置文件获取多数据源
        String dsPrefixs = env.getProperty("front.datasource.names");
        String poolType = env.getProperty("front.datasource.type");

        JdbcConfig jdbcConfig;

        switch (poolType){
            case "DRUID":
                for (String dsPrefix : dsPrefixs.split(",")){
                    jdbcConfig = new DruidJdbcConfig(env,dsPrefix,
                            env.getProperty("front.datasource." + dsPrefix + ".driverClassName"),
                            env.getProperty("front.datasource." + dsPrefix + ".url"),
                            env.getProperty("front.datasource." + dsPrefix + ".username"),
                            env.getProperty("front.datasource." + dsPrefix + ".password"));
                    configs.add(jdbcConfig);
                }
                pool = new DruidPool(configs);
                break;
            default:
                throw new Exception("无效的PoolType");
        }
        customDataSource.putAll(pool.getDataSources());
    }
}
