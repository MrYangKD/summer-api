package com.cn.summer.api.service;

import com.cn.summer.api.common.domain.Student;

/**
 * .
 * studentservice.
 * @author YangYK
 * @create: 2019-11-17 14:27
 * @since 1.0
 */
public interface StudentService {

    /**
     * 新增数据.
     * @param name name
     * @param student student
     * @return int
     */
    int saveData(String name, Student student);

}
