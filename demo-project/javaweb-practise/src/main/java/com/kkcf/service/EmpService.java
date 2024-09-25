package com.kkcf.service;

import com.kkcf.pojo.Emp;
import com.kkcf.pojo.EmpPageBean;

import java.time.LocalDate;

public interface EmpService {
    EmpPageBean listWithPageAndCount(
            int page,
            int pageSize,
            String name,
            int gender,
            LocalDate startDate,
            LocalDate endDate);

    int removeByIds(int[] ids);

    int addEmp(Emp emp);

    Emp empById(int id);

    int modifyEmp(Emp emp);

    Emp loginEmp(Emp emp);
}
