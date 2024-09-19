package com.kkcf.mapper;

import com.kkcf.pojo.Emp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmpMapper {
    @Select("SELECT id, username, password, name, gender, image, job, entrydate, dept_id, create_time, update_time FROM emp LIMIT #{offset}, #{limit}")
    List<Emp> listWithPagination(int offset, int limit);

    @Select("SELECT COUNT(*) FROM emp;")
    Long listCount();
}
