package com.kkcf.service.impl;

import com.kkcf.mapper.DeptMapper;
import com.kkcf.pojo.Dept;
import com.kkcf.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptMapper deptMapper;

    @Override
    public List<Dept> list() {
        return deptMapper.selectDepts();
    }

    @Override
    public int removeById(Integer id) {
        return deptMapper.deleteDeptById(id)
    }

    @Override
    public int addDept(Dept dept) {
        dept.setCreateTime(LocalDate.now());
        dept.setUpdateTime(LocalDate.now());
        return deptMapper.insertDept(dept);
    }

    @Override
    public Dept getById(Integer id) {
        return deptMapper.selectDeptById(id);
    }

    @Override
    public int updateDept(Dept dept) {
        dept.setUpdateTime(LocalDate.now());
        return deptMapper.updateDept(dept);
    }
}
