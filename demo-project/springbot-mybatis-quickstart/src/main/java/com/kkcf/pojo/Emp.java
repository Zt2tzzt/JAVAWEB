package com.kkcf.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emp {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private Short gender;
    private String image;
    private Short job;
    private LocalDate entrydate; //LocalDate 类型对应数据表中的 date 类型
    private Integer deptId;
    private LocalDateTime createTime; //LocalDateTime 类型对应数据表中的 datetime 类型
    private LocalDateTime updateTime;
}
