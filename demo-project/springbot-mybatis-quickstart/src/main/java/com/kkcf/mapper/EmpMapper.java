package com.kkcf.mapper;

import com.kkcf.pojo.Emp;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface EmpMapper {
    @Delete("DELETE FROM emp WHERE id = #{id};")
    int deleteEmpById(Integer id);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO emp(username, name, gender, image, job, entrydate, dept_id, create_time, update_time) VALUES (#{username}, #{name}, #{gender}, #{image}, #{job}, #{entrydate}, #{deptId}, #{createTime}, #{updateTime});")
    int insertEmp(Emp emp);

    /*@Update("UPDATE emp SET username = #{username}, name = #{name}, gender = #{gender}, image = #{image}, job = #{job}, entrydate = #{entrydate}, dept_id = #{deptId}, update_time = #{updateTime} WHERE id = #{id};")
    int updateEmpById(Emp emp);*/
    int updateEmpById(Emp emp);

    /*@Results({
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "dept_id", property = "deptId"),
    })
    @Select("SELECT id, username, password, name, gender, image, job, entrydate, dept_id, create_time, update_time FROM emp WHERE id = #{id};")
    Emp selectEmpById(Integer id);*/
    Emp selectEmpById(Integer id);

    /*@Select("SELECT id,\n" +
            "       username,\n" +
            "       password,\n" +
            "       name,\n" +
            "       gender,\n" +
            "       image,\n" +
            "       job,\n" +
            "       entrydate,\n" +
            "       dept_id,\n" +
            "       create_time,\n" +
            "       update_time\n" +
            "FROM emp\n" +
            "WHERE name LIKE CONCAT('%', #{name}, '%')\n" +
            "  AND gender = #{gender}\n" +
            "  AND entrydate BETWEEN #{startDate} AND #{endDate}\n" +
            "ORDER BY entrydate DESC;")
    List<Emp> selectEmpByCondition(@Param("name") String name, @Param("gender") Short gender, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);*/

    List<Emp> selectEmpByCondition(String name, Short gender, LocalDate startDate, LocalDate endDate);

    int deleteEmpByIds(List<Integer> ids);
}
