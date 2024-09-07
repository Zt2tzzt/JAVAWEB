package com.kkcf.service.impl;

import com.kkcf.dao.EmpDao;
import com.kkcf.pojo.Emp;
import com.kkcf.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpServiceB implements EmpService {
    @Autowired
    public EmpDao empDao;

    @Override
    public List<Emp> listEmp() {
        List<Emp> empList = empDao.listEmp();
        // 对数据进行处理
        empList.forEach(emp -> {
            // 性别
            String gender = emp.getGender();
            String genderStr = switch (gender) {
                case "1" -> "男士";
                case "2" -> "女士";
                default -> "";
            };
            emp.setGender(genderStr);

            // 工作
            String job = emp.getJob();
            String jobStr = switch (job) {
                case "1" -> "讲师";
                case "2" -> "班主任";
                case "3" -> "就业指导";
                default -> "";
            };
            emp.setJob(jobStr);
        });

        return empList;
    }
}
