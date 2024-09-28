package com.kkcf.mapper;

import com.kkcf.pojo.DeptLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeptLogMapper {
    @Insert("INSERT INTO dept_log(create_time, description) VALUES (#{createTime}, #{description})")
    int insert(DeptLog deptLog);
}
