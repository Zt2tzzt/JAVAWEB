package com.kkcf.service;

import com.kkcf.pojo.EmpPageBean;

public interface EmpService {
    EmpPageBean listWithPageAndCount(Integer page, Integer pageSize);
}
