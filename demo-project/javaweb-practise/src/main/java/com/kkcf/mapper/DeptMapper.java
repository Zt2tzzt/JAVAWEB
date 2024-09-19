package com.kkcf.mapper;

import com.kkcf.pojo.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeptMapper {
    @Select("SELECT id, name, create_time, update_time FROM dept")
    List<Dept> selectDepts();

    @Delete("DELETE FROM dept WHERE id = #{id}")
    int deleteDeptById(Integer id);

    @Insert("INSERT INTO dept(name, create_time, update_time) VALUES (#{name}, #{createTime}, #{updateTime});")
    int insertDept(Dept dept);

    @Select("SELECT id, name, create_time, update_time FROM dept WHERE id = #{id}")
    Dept selectDeptById(Integer id);

    @Update("UPDATE dept SET name = #{name}, update_time = #{updateTime} WHERE id = #{id}")
    int updateDept(Dept dept);
}
