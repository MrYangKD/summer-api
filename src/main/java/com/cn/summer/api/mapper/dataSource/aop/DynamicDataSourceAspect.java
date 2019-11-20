package com.cn.summer.api.mapper.dataSource.aop;

import ch.qos.logback.classic.Logger;
import com.cn.summer.api.common.entity.ResCodeMessage;
import com.cn.summer.api.common.util.AopUtils;
import com.cn.summer.api.common.util.HashUtils;
import com.cn.summer.api.common.util.JobAppUtil;
import com.cn.summer.api.exception.BusinessException;
import com.cn.summer.api.mapper.dataSource.DynamicDataSourceContextHolder;
import com.cn.summer.api.mapper.dataSource.annotation.TargetDataSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * AOP切面实现动态数据源切换,分片规则按运单号做hash.
 * 保证该AOP在@Transactional之前执行
 * @author YangYK
 * @create: 2019-11-17 14:55
 * @since 1.0
 */
@Component
@Aspect
@Order(-10)
public class DynamicDataSourceAspect {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(DynamicDataSourceAspect.class);

    @Value("${front.datasource.subdivision.number}")
    private int frontDataSourceSubdivisionNumber;

    private long selectSubdivisionStart;
    private long insertStart;

    /**
     * 动态切换数据源.
     * @param joinPoint 切点
     * @param targetDataSource 自定义注解
     * @return int
     * @throws Throwable e
     */
    @Around("@annotation(targetDataSource)")
    public int changeDataSource(final ProceedingJoinPoint joinPoint, final TargetDataSource targetDataSource) throws Throwable{
        selectSubdivisionStart = System.currentTimeMillis();
        String dataSourceType = targetDataSource.value();
        if(JobAppUtil.isNotNull(dataSourceType)){
            if(DynamicDataSourceContextHolder.containsDataSource(dataSourceType)){
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceType);
                LOGGER.info("---------------选择数据库分片" + dataSourceType);
                try {
                    return (int) joinPoint.proceed();
                } catch (final Throwable throwable){
                    if (throwable.getMessage() != null && throwable.getMessage().indexOf("Data too long for column") > -1) {
                        String errorMessage = throwable.getMessage().substring(throwable.getMessage().lastIndexOf("Data too long for column"));
                        throw new BusinessException(ResCodeMessage.RES_CODE_6005.getCode(), ResCodeMessage.RES_CODE_6005.getMessage() + " :  " +errorMessage);
                    } else {
                        throw new BusinessException(ResCodeMessage.RES_CODE_6008.getCode(), ResCodeMessage.RES_CODE_6008.getMessage() + " :  " +throwable.getMessage());
                    }
                }
            }
            else {
                LOGGER.error("数据源" + dataSourceType + "不存在");
            }
        } else {
            String name = (String) AopUtils.getParamValueByName(joinPoint,"name");
            if(JobAppUtil.isNotNull(name)){
                // 名字做hash,设置数据源
                int index = HashUtils.hashSplit(name,frontDataSourceSubdivisionNumber);
                LOGGER.info("--------------选择分片db" + index);
                dataSourceType="db" + index;
                if (DynamicDataSourceContextHolder.containsDataSource(dataSourceType)){
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceType);
                    try {
                        return (int) joinPoint.proceed();
                    } catch (final Throwable throwable){
                        if (throwable.getMessage() != null && throwable.getMessage().indexOf("Data too long for column") > -1) {
                            String errorMessage = throwable.getMessage().substring(throwable.getMessage().lastIndexOf("Data too long for column"));
                            throw new BusinessException(ResCodeMessage.RES_CODE_6005.getCode(), ResCodeMessage.RES_CODE_6005.getMessage() + " :  " +errorMessage);
                        } else {
                            throw new BusinessException(ResCodeMessage.RES_CODE_6008.getCode(), ResCodeMessage.RES_CODE_6008.getMessage() + " :  " +throwable.getMessage());
                        }
                    }
                } else {
                    LOGGER.error("数据源" + dataSourceType + "不存在");
                }
            } else {
                LOGGER.error("waybillNo分片字段不能为空!");
            }
        }
        return 0;
    }


    /**
     * 日志打印.
     * @param point 切点
     * @param targetDataSource 自定义注解
     */
    @Before("@annotation(targetDataSource)")
    private void logBefore(final JoinPoint point, final TargetDataSource targetDataSource){
        LOGGER.info("选择数据库分片耗时: " + (System.currentTimeMillis() - selectSubdivisionStart) + "ms");
        insertStart = System.currentTimeMillis();
    }

    public void restoreDataSource(final JoinPoint point, final TargetDataSource targetDataSource){
        LOGGER.info("插入数据库耗时: " + (System.currentTimeMillis() - this.insertStart + "ms"));
        DynamicDataSourceContextHolder.clearDataSourceType();
    }

}
