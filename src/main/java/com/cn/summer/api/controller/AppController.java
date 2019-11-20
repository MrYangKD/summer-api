package com.cn.summer.api.controller;

import ch.qos.logback.classic.Logger;
import com.alibaba.fastjson.JSON;
import com.cn.summer.api.checkSign.annotation.CheckSign;
import com.cn.summer.api.common.domain.PersonDto;
import com.cn.summer.api.common.domain.Student;
import com.cn.summer.api.common.entity.ResCodeMessage;
import com.cn.summer.api.common.entity.ResModel;
import com.cn.summer.api.common.util.RSAUtils;
import com.cn.summer.api.factory.BizClassFactory;
import com.cn.summer.api.factory.ServiceFactory;
import io.swagger.annotations.*;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * .
 * 数据接入接口.
 * @author YangYK
 * @since 1.0
 */
@Api(description = "接入API")
@RestController
@RequestMapping("/record")
public class AppController {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(AppController.class);

    @ApiOperation(value = "数据写入", notes="数据写入")
    @RequestMapping(value = "/dataWrite", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型  例:stuent,teacher,worker...", required = true, dataType = "String"),
            @ApiImplicitParam(name = "content", value = "json字符串,写入前置库数据", required = true, dataType = "String"),
            @ApiImplicitParam(name = "appId", value = "接入系统ID,在前置库申请的应用名称  例: front-api-test", required = true, dataType = "String"),
            @ApiImplicitParam(name = "charset", value = "验签参数编码字符集  例: UTF-8", required = true, dataType = "String"),
            @ApiImplicitParam(name = "timestamp", value = "当前时间戳格式 yyyy-MM-dd HH:mm:ss.SSS  例: 2019-08-05 10:00:00.100", required = true, dataType = "String"),
            @ApiImplicitParam(name = "signType", value = "验签类型,目前只支持RSA的验签方式  例: RSA", required = true, dataType = "String"),
            @ApiImplicitParam(name = "version", value = "版本号  所有服务器端用户版本号: 1.0", required = true, dataType = "String"),
            @ApiImplicitParam(name = "sign", value = "验签内容", required = true, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 6000, message = "SUCCESS"),
            @ApiResponse(code = 6001, message = "ERROR：json串格式有误"),
            @ApiResponse(code = 6002, message = "ERROR：保存异常, 请重试"),
            @ApiResponse(code = 6003, message = "ERROR：系统异常"),
            @ApiResponse(code = 6004, message = "ERROR：数据验签不通过,非法数据来源"),
            @ApiResponse(code = 6005, message = "ERROR：请求参数异常"),
            @ApiResponse(code = 6006, message = "ERROR：未找到相应渠道的公钥"),
            @ApiResponse(code = 6008, message = "ERROR：数据库异常")
    })
    @CheckSign
    public ResModel dataWrite(@RequestParam(name="type", required = false) final String type, @RequestParam(name="content", required = false) final String content,
                              @RequestParam(name="appId", required = false) final String appId, @RequestParam(name="charset", required = false) final String charset,
                              @RequestParam(name="timestamp", required = false) final String timestamp, @RequestParam(name="signType", required = false) final String signType,
                              @RequestParam(name="version", required = false) final String version, @RequestParam(name="sign", required = false) final String sign){

        PersonDto dto;
        try {
            dto = JSON.parseObject(content, BizClassFactory.getBizClass(type));
        } catch (final Exception e){
            LOGGER.error("content json 转换失败", e);
            return new ResModel(ResCodeMessage.RES_CODE_6001);
        }
        try {
            ServiceFactory.getService(type).dealRecord(dto);
        } catch (Exception e){
            LOGGER.error("系统异常",e);
            return new ResModel(ResCodeMessage.RES_CODE_6003);
        }
        return new ResModel(ResCodeMessage.RES_CODE_6000);
    }



    /**
     *  统一入口 proKey.
     * @param type 业务类型
     * @param content 内容josn字符串
     * @param appId 接入系统ID
     * @param charset 字符集
     * @param timestamp 时间戳
     * @param signType 验签类型
     * @param version 版本号
     * @param privateKey 私钥
     * @return string
     */
    @ApiOperation(value = "生成签名接口", notes="生成签名接口")
    @RequestMapping(value="/proKey", method= RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型  例:stuent,teacher,worker...", required = true, dataType = "String"),
            @ApiImplicitParam(name = "content", value = "json字符串,写入前置库数据", required = true, dataType = "String"),
            @ApiImplicitParam(name = "appId", value = "接入系统ID,在前置库申请的应用名称  例: front-api-test", required = true, dataType = "String"),
            @ApiImplicitParam(name = "charset", value = "验签参数编码字符集  例: UTF-8", required = true, dataType = "String"),
            @ApiImplicitParam(name = "timestamp", value = "当前时间戳格式 yyyy-MM-dd HH:mm:ss.SSS  例: 2019-08-05 10:00:00.100", required = true, dataType = "String"),
            @ApiImplicitParam(name = "signType", value = "验签类型,目前只支持RSA的验签方式  例: RSA", required = true, dataType = "String"),
            @ApiImplicitParam(name = "version", value = "版本号  所有服务器端用户版本号: 1.0", required = true, dataType = "String"),
            @ApiImplicitParam(name = "privateKey", value = "私钥", required = true, dataType = "String")
    })
    public String proKey(@RequestParam(name="type", required = false) final String type, @RequestParam(name="content", required = false) final String content,
                         @RequestParam(name="appId", required = false) final String appId, @RequestParam(name="charset", required = false) final String charset,
                         @RequestParam(name="timestamp", required = false) final String timestamp, @RequestParam(name="signType", required = false) final String signType,
                         @RequestParam(name="version", required = false) final String version, @RequestParam(name="privateKey", required = false) final String privateKey){

        String signt = "";
            try {
                Map<String, Object> resParams = new HashMap<String, Object>();
                resParams.put("content", content); //json字符串,写入前置库数据
                resParams.put("type", type); //业务类型 student,teacher,worker..
                resParams.put("appId", appId);  //在前置库申请的应用名称
                resParams.put("charset", charset); //验签参数编码字符集
                resParams.put("signType", signType); //目前只支持RSA的验签方式
                resParams.put("timestamp", timestamp); //当前时间戳格式 yyyy-MM-dd HH:mm:ss.SSS
                resParams.put("version", version); //版本号,所有的服务器端用户版本号1.0
                String signStr = RSAUtils.getSignStr(resParams);
                signt = RSAUtils.sign(signStr, privateKey, charset);
            } catch (final Exception e) {
                LOGGER.error("content json 转换失败", e);
                signt = "警告,警告,参数输入错误!!";
            }
        return signt;
    }

}
