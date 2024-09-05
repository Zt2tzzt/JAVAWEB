package com.kkcf.controller;

import com.kkcf.pojo.Emp;
import com.kkcf.pojo.Result;
import com.kkcf.utils.XmlParserUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class EmpController {
    @RequestMapping("/listEmp")
    public Result<List<Emp>> listEmp() {
        // 加载并解析 emp.xml 文件
        ClassLoader classLoader = this.getClass().getClassLoader(); // 获取类加载器
        String file = Objects.requireNonNull(classLoader.getResource("emp.xml")).getFile();
        System.out.println("file: " + file);
        List<Emp> empList = XmlParserUtils.parse(file, Emp.class);

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

        return Result.success(empList);
    }
}
