package com.kkcf.controller;

import com.kkcf.pojo.Address;
import com.kkcf.pojo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ResponseController {
    @RequestMapping("/hi")
    public Result<String> hi() {
        return Result.success("hi");
    }

    @RequestMapping("/addr")
    public Result<Address> addr() {
        return Result.success(new Address("广东省", "深圳市"));
    }

    @RequestMapping("/listAddr")
    public Result<List<Address>> listAddr() {
        return Result.success(new ArrayList<Address>(List.of(
                new Address("广东省", "深圳市"),
                new Address("广东省", "广州市"),
                new Address("广东省", "珠海市")
        )));
    }
}
