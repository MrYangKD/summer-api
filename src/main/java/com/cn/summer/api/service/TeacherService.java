package com.cn.summer.api.service;

import com.cn.summer.api.common.domain.Teacher;

/**
 * .
 * TeacherService.
 * @author YangYK
 * @create: 2019-11-17 14:30
 * @since 1.0
 */
public interface TeacherService {

    /**
     * 新增数据.
     * @param name name
     * @param student student
     * @return int
     */
    int saveData(String name, Teacher student);

    /**
     * 修改数据.
     * @param name name
     * @param student student
     * @return int
     */
    int updateData(String name, Teacher student);
}
