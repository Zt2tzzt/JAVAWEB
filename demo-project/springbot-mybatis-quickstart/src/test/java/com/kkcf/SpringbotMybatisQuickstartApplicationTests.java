package com.kkcf;

import com.kkcf.mapper.EmpMapper;
import com.kkcf.mapper.UserMapper;
import com.kkcf.pojo.Emp;
import com.kkcf.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest // Spring Boot 整合单元测试的注解：类中的单元测试方法在运行时，会自动加载 Spring Boot 环菌，并创建 IOC 容器。
class SpringbotMybatisQuickstartApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private EmpMapper empMapper;

    @Test
    public void testListUser() {
        List<User> users = userMapper.listUser();
        users.stream().forEach(System.out::println);
    }

    @Test
    public void testDeleteEmp() {
        int i = empMapper.deleteEmpById(16);
        System.out.println(i);
    }

    @Test
    public void testInsertEmp() {
        Emp emp = new Emp();
        emp.setUsername("tom2");
        emp.setName("汤姆2");
        emp.setGender((short) 1);
        emp.setImage("1.jpb");
        emp.setJob((short) 1);
        emp.setEntrydate(LocalDate.of(2020, 1, 1));
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        emp.setDeptId(1);

        int i = empMapper.insertEmp(emp);
        System.out.println(i);
        System.out.println(emp.getId()); // 19
    }

    @Test
    public void testUpdateEmp() {
        Emp emp = new Emp();
        emp.setId(18);
        emp.setUsername("linghushaoxiao");
        emp.setName("令狐少校");
        emp.setGender((short) 1);
        emp.setImage("2.jpb");
        emp.setEntrydate(LocalDate.of(2021, 1, 1));
        emp.setDeptId(1);
        emp.setUpdateTime(LocalDateTime.now());

        int i = empMapper.updateEmpById(emp);
        System.out.println(i); // 1
    }

    @Test
    public void testSelectEmpById() {
        Emp emp = empMapper.selectEmpById(18);

        System.out.println(emp);
    }

    @Test
    public void testSelectEmpByCondition() {
        //List<Emp> emps = empMapper.selectEmpByCondition("张", (short) 1, LocalDate.of(2010, 1, 1), LocalDate.of(2020, 1, 1));
        List<Emp> emps = empMapper.selectEmpByCondition(null, (short) 1, null, null);

        emps.forEach(System.out::println);
    }

    @Test
    public void testDeleteEmpByIds() {
        int i = empMapper.deleteEmpByIds(List.of(18, 19));

        System.out.println(i);
    }
}
