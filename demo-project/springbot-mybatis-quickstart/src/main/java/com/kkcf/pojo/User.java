package com.kkcf.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    // 实体类中的属性，推荐使用包装类型
    private Integer id;
    private String name;
    private Short age;
    private Short gender;
    private String phone;
}
