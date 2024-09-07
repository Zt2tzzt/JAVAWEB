package com.kkcf.dao.impl;

import com.kkcf.dao.EmpDao;
import com.kkcf.pojo.Emp;
import com.kkcf.utils.XmlParserUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository("daoA")
public class EmpDaoA implements EmpDao {
    @Override
    public List<Emp> listEmp() {
        ClassLoader classLoader = this.getClass().getClassLoader(); // 获取类加载器
        String file = Objects.requireNonNull(classLoader.getResource("emp.xml")).getFile();
        System.out.println("file: " + file);
        return XmlParserUtils.parse(file, Emp.class);
    }
}
