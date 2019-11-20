package com.cn.summer.api.business.impl;

import ch.qos.logback.classic.Logger;
import com.alibaba.fastjson.JSON;
import com.cn.summer.api.business.BaseBiz;
import com.cn.summer.api.common.domain.PersonDto;
import com.cn.summer.api.common.domain.Student;
import com.cn.summer.api.common.entity.ResCodeMessage;
import com.cn.summer.api.exception.BusinessException;
import com.cn.summer.api.rocketMq.IProducerService;
import com.cn.summer.api.service.StudentService;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * .
 * 学生类业务处理.
 * @author YangYK
 * @create: 2019-11-16 14:42
 * @since 1.0
 */
@Component("studentBiz")
public class StudentBizImpl implements BaseBiz {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(StudentBizImpl.class);

    @Resource
    private StudentService studentService;
    @Resource
    private IProducerService producerService;

    @Override
    public void dealRecord(final PersonDto dto) throws Exception {
        Student student = (Student) dto;

        // 对student进行参数验证

        // 初始化数据,可以进行数据补充,赋值等

        // 插入数据,并对返回值进行判断
        int result;
        result = studentService.saveData(student.getName(),student);

        if (result < 1){
            throw new BusinessException(ResCodeMessage.RES_CODE_6002.getCode(),  ResCodeMessage.RES_CODE_6002.getMessage());
        }

        try {
            // 将结果写入mq中
            producerService.defaultMqProducer(JSON.toJSONString(student));
        } catch (Exception e){
            LOGGER.error("写入mq异常,异常信息为:" + e);
        }
    }
}
