package com.kkcf.service;

import com.kkcf.pojo.Dept;

import java.util.List;

public interface DeptService {
    List<Dept> list();

    int removeById(Integer id);

    int addDept(Dept dept);

    Dept getById(Integer id);

    int updateDept(Dept dept);
}
