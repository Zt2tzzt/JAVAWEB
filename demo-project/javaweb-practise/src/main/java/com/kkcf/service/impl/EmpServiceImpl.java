package com.kkcf.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kkcf.mapper.EmpMapper;
import com.kkcf.pojo.Emp;
import com.kkcf.pojo.EmpPageBean;
import com.kkcf.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    private EmpMapper empMapper;

    /*@Override
    public EmpPageBean listWithPageAndCount(Integer page, Integer pageSize) {
        int offset = (page - 1) * pageSize;
        List<Emp> rows = empMapper.listWithPagination(offset, pageSize);

        Long total = empMapper.listCount();

        return new EmpPageBean(total, rows);
    }*/

    @Override
    public EmpPageBean listWithPageAndCount(
            int page,
            int pageSize,
            String name,
            int gender,
            LocalDate startDate,
            LocalDate endDate
    ) {
        // 1.设置分页参数
        PageHelper.startPage(page, pageSize);

        // 2.进行分页查询
        List<Emp> list = empMapper.list(name, gender, startDate, endDate);
        Page<Emp> p = (Page<Emp>) list;

        return new EmpPageBean(p.getTotal(), p.getResult());
    }

    @Override
    public int removeByIds(int[] ids) {
        return empMapper.deleteByIds(ids);
    }

    @Override
    public int addEmp(Emp emp) {
        // 补充基础属性
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());

        return empMapper.insertEmp(emp);
    }

    @Override
    public Emp empById(int id) {

        return empMapper.selectById(id);
    }

    @Override
    public int modifyEmp(Emp emp) {
        // 补充数据
        emp.setUpdateTime(LocalDateTime.now());

        return empMapper.updateEmp(emp);
    }

    @Override
    public Emp loginEmp(Emp emp) {
        return empMapper.selectByLogin(emp);
    }
}
