package com.kkcf.service;

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
}
