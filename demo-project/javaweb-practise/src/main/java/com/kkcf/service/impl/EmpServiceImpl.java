package com.kkcf.service.impl;

import com.kkcf.mapper.EmpMapper;
import com.kkcf.pojo.Emp;
import com.kkcf.pojo.EmpPageBean;
import com.kkcf.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    private EmpMapper empMapper;

    @Override
    public EmpPageBean listWithPageAndCount(Integer page, Integer pageSize) {
        int offset = (page - 1) * pageSize;
        List<Emp> rows = empMapper.listWithPagination(offset, pageSize);

        Long total = empMapper.listCount();

        return new EmpPageBean(total, rows);
    }
}
