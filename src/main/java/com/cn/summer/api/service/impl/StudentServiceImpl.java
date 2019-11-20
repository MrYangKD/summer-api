package com.cn.summer.api.service.impl;

import ch.qos.logback.classic.Logger;
import com.cn.summer.api.common.domain.Student;
import com.cn.summer.api.mapper.dao.StudentMapper;
import com.cn.summer.api.mapper.dataSource.annotation.TargetDataSource;
import com.cn.summer.api.service.StudentService;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * .
 *
 * @author YangYK
 * @create: 2019-11-17 14:31
 * @since 1.0
 */
@Service("studentService")
public class StudentServiceImpl implements StudentService {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(StudentServiceImpl.class);
    @Resource
    private StudentMapper studentMapper;

    @Override
    @TargetDataSource
    public int saveData(String name, Student student) {
        return studentMapper.saveData(student);
    }
}
