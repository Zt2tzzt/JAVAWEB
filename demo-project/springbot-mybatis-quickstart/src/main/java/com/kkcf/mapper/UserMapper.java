package com.kkcf.mapper;

import com.kkcf.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper // 在程序运行时，MyBatis 框架会自动生成该接口的实现类对昂（代理对象），并且将该对象，交给 IOC 容器管理。
public interface UserMapper {
    @Select("SELECT * FROM user")
    List<User> listUser();
}
