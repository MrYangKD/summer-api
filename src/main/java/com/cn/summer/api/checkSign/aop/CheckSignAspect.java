package com.cn.summer.api.checkSign.aop;

import ch.qos.logback.classic.Logger;
import com.cn.summer.api.checkSign.annotation.CheckSign;
import com.cn.summer.api.common.cache.CacheInfo;
import com.cn.summer.api.common.entity.ResCodeMessage;
import com.cn.summer.api.common.entity.ResModel;
import com.cn.summer.api.common.util.AopUtils;
import com.cn.summer.api.common.util.JobAppUtil;
import com.cn.summer.api.common.util.RSAUtils;
import com.cn.summer.api.exception.BusinessException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * .
 * 验签处理.
 * @author YangYK
 * @since 1.0
 */
@Aspect
@Component
@Order(-100)
public class CheckSignAspect {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(CheckSignAspect.class);
    private static final char EQUAL_SYMNOL = '=';
    private static final char AND_SYMNOL = '&';
    private long totalCostStart;


    /**
     * 验签.
     * @param joinPoint 切点
     * @param checkSign 自定义注解
     * @return
     */
    @ResponseBody
    @Around("@annotation(checkSign)")
    public ResModel doAround(final ProceedingJoinPoint joinPoint, final CheckSign checkSign){
        totalCostStart = System.currentTimeMillis();

        //获取所有参数
        Map<String, Object> params = AopUtils.getParams(joinPoint);
        LOGGER.debug("请求参数: " + params);
        boolean checkResult;

        try {
            checkResult = checkSign(params);
        } catch (final BusinessException be){
            LOGGER.error(be.getCode()+" : "+be.getMessage(), be);
            return new ResModel(be.getCode(), be.getMessage());
        }

        if (checkResult){
            try {
                return (ResModel) joinPoint.proceed();
            } catch (Throwable throwable) {
                LOGGER.error(throwable.getMessage(), throwable.getCause());
            }
        }else {
            LOGGER.error(ResCodeMessage.RES_CODE_6004.getCode(), ResCodeMessage.RES_CODE_6004.getMessage());
            //返回一个验签失败的实体;
            return new ResModel(ResCodeMessage.RES_CODE_6004);
        }

        return null;
    }
    /**
     * 验签耗时日志打印.
     * @param joinPoint 切点
     * @param checkSign 自定义注解
     */
    @Before("@annotation(checkSign)")
    public void logBefore(final JoinPoint joinPoint, final CheckSign checkSign){
        LOGGER.debug("验签耗时:" + (System.currentTimeMillis() - totalCostStart) + "ms");
    }


    /**
     * 接口处理总耗时日志打印.
     * @param joinPoint 切点
     * @param checkSign 自定义注解
     */
    @AfterReturning("@annotation(checkSign)")
    public void logAfterReturning(final JoinPoint joinPoint, final CheckSign checkSign){
        LOGGER.debug("数据写入总耗时:" + (System.currentTimeMillis() - totalCostStart) + "ms");
    }


    /**
     * 签名验证.
     * @param params params
     * @return boolean
     */
    private boolean checkSign(final Map<String, Object> params) throws BusinessException {
        String content = (String) params.get("content");
        String type = (String) params.get("type");
        String charset = (String) params.get("charset");
        String appId = (String) params.get("appId");
        String version = (String) params.get("version");
        String sign = (String) params.get("sign");

        String publicKey = CacheInfo.getPublicKey(appId);
        LOGGER.info(publicKey);

        if (JobAppUtil.isNull(content)){
            throw new BusinessException(ResCodeMessage.RES_CODE_6005.getCode(), ResCodeMessage.RES_CODE_6005.getMessage() + " : content插入内容不能为空");
        }
        if (JobAppUtil.isNull(type)) {
            throw new BusinessException(ResCodeMessage.RES_CODE_6005.getCode(),ResCodeMessage.RES_CODE_6005.getMessage() + " : type业务类型不能为空");
        }
        if (JobAppUtil.isNull(charset)) {
            throw new BusinessException(ResCodeMessage.RES_CODE_6005.getCode(), ResCodeMessage.RES_CODE_6005.getMessage() + " : charset字符集不能为空");
        }
        if (JobAppUtil.isNull(appId)) {
            throw new BusinessException(ResCodeMessage.RES_CODE_6005.getCode(), ResCodeMessage.RES_CODE_6005.getMessage() + " : appId接入应用ID不能为空");
        }
        if (JobAppUtil.isNull(version)) {
            throw new BusinessException(ResCodeMessage.RES_CODE_6005.getCode(), ResCodeMessage.RES_CODE_6005.getMessage() + " : version版本号不能为空");
        }
        if (JobAppUtil.isNull(sign)) {
            throw new BusinessException(ResCodeMessage.RES_CODE_6005.getCode(), ResCodeMessage.RES_CODE_6005.getMessage() + " : sign验签不能为空");
        }
        if (JobAppUtil.isNull(publicKey)) {
            throw new BusinessException(ResCodeMessage.RES_CODE_6006.getCode(), ResCodeMessage.RES_CODE_6006.getMessage());
        }

        String signStr = getSignStr(params);
        return RSAUtils.verify(signStr, sign, publicKey, charset);
    }

    /**
     * 获取待验签的字符串.
     * @param params params
     * @return signStr
     */
    private String getSignStr(final Map<String,Object> params){
        StringBuilder signStr = new StringBuilder();
        params.entrySet().stream()
                .filter(e -> e.getValue() != null && !"".equals(e.getValue()) && !"sign".equals(e.getKey()))
                .sorted(Map.Entry.<String, Object>comparingByKey())
                .forEachOrdered(e -> signStr.append(e.getKey()).append(EQUAL_SYMNOL).append(e.getValue()).append(AND_SYMNOL));

        signStr.deleteCharAt(signStr.length()-1);
        return signStr.toString();
    }

}
