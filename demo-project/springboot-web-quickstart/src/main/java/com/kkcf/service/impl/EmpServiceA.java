package com.kkcf.service.impl;

import com.kkcf.dao.EmpDao;
import com.kkcf.pojo.Emp;
import com.kkcf.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // 将当前类，交给 IOC 容器管理，称为 IOC 容器中的 Bean
public class EmpServiceA implements EmpService {
    @Autowired // 运行时，IOC 容器会提供该类的 Bean 对象，并赋值给该变量，这杯称为依赖注入
    public EmpDao empDao;

    @Override
    public List<Emp> listEmp() {
        List<Emp> empList = empDao.listEmp();

        // 对数据进行处理
        empList.forEach(emp -> {
            // 性别
            String gender = emp.getGender();
            String genderStr = switch (gender) {
                case "1" -> "男";
                case "2" -> "女";
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
