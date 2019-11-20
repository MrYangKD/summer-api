package com.cn.summer.api.mapper.dao;

import com.cn.summer.api.common.domain.Student;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

/**
 * .
 * student Dao 层
 * @author YangYK
 * @since 1.0
 */

@Component("studentMapper")
@Mapper
public interface StudentMapper {
    @Insert("insert into t_exp_op_student (id,name,age,sex,vocation,hobby,income,isMarry,type,grade,count)" +
            "values(#{dto.id, jdbcType=VARCHAR},#{dto.name, jdbcType=VARCHAR},#{dto.age, jdbcType=VARCHAR}," +
            "#{dto.sex, jdbcType=VARCHAR},#{dto.vocation, jdbcType=VARCHAR},#{dto.hobby, jdbcType=VARCHAR}," +
            "#{dto.income, jdbcType=NUMERIC},#{dto.isMarry, jdbcType=VARCHAR},#{dto.type, jdbcType=VARCHAR}," +
            "#{dto.grade, jdbcType=VARCHAR},#{dto.count, jdbcType=NUMERIC})")
    int saveData(@Param("dto") Student student);
}
