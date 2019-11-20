package com.cn.summer.api.factory;

import com.cn.summer.api.common.domain.PersonDto;
import com.cn.summer.api.common.domain.Student;
import com.cn.summer.api.common.domain.Teacher;
import com.cn.summer.api.common.entity.LocalConstants;

/**
 * .
 * 业务实体工厂.
 * @author YangYK
 * @create: 2019-11-16 13:52
 * @since 1.0
 */
public class BizClassFactory {
    private BizClassFactory(){}

    public static Class<? extends PersonDto> getBizClass(final String type){
        if (LocalConstants.Kind.STUDENT.equals(type.toUpperCase())){
            return Student.class;
        }
        if(LocalConstants.Kind.TEACHER.equals(type.toUpperCase())){
            return Teacher.class;
        }
        return null;
    }
}
