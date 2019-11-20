package com.cn.summer.api.factory;

import com.cn.summer.api.business.BaseBiz;
import com.cn.summer.api.business.impl.StudentBizImpl;
import com.cn.summer.api.business.impl.TeacherBizImpl;
import com.cn.summer.api.common.entity.LocalConstants;
import com.cn.summer.api.common.util.SpringUtils;

/**
 * .
 * 业务实现工厂.
 * @author YangYK
 * @create: 2019-11-16 14:18
 * @since 1.0
 */
public class ServiceFactory {
    private ServiceFactory(){}

    /**
     * 获取service实现.
     * @param type 标识
     * @return AppService
     */
    public static BaseBiz getService(final String type){
        if (LocalConstants.Kind.STUDENT.equalsIgnoreCase(type)){
            return SpringUtils.getBean(StudentBizImpl.class);
        }
        if (LocalConstants.Kind.TEACHER.equalsIgnoreCase(type)){
            return SpringUtils.getBean(TeacherBizImpl.class);
        }
        return null;
    }
}
