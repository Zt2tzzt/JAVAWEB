package com.kkcf.mapper;

import com.kkcf.pojo.Emp;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface EmpMapper {
    /*@Select("SELECT id, username, password, name, gender, image, job, entrydate, dept_id, create_time, update_time FROM emp LIMIT #{offset}, #{limit}")
    List<Emp> listWithPagination(int offset, int limit);

    @Select("SELECT COUNT(*) FROM emp;")
    Long listCount();*/

    List<Emp> list(
            String name,
            int gender,
            LocalDate startDate,
            LocalDate endDate
    );

    int deleteByIds(int[] ids);

    @Insert("INSERT INTO emp(username, password, name, gender, image, job, entrydate, dept_id, create_time, update_time) VALUES (#{username}, #{password}, #{name}, #{gender}, #{image}, #{job}, #{entrydate}, #{deptId}, #{createTime}, #{updateTime});")
    int insertEmp(Emp emp);

    @Select("SELECT id, username, password, name, gender, image, job, entrydate, dept_id, create_time, update_time FROM emp WHERE id = #{id};")
    Emp selectById(int id);

    int updateEmp(Emp emp);

    @Select("SELECT id FROM emp WHERE username = #{username} AND password = #{password}")
    Emp selectByLogin(Emp emp);
}
