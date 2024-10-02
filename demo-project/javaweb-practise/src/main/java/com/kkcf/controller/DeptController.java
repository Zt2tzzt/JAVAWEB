package com.kkcf.controller;

import com.kkcf.pojo.Dept;
import com.kkcf.pojo.Result;
import com.kkcf.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Scope("prototype")
@Slf4j
@RestController
@RequestMapping("/depts")
public class DeptController {
    @Autowired
    private DeptService deptService;

    public DeptController() {
        System.out.println("创建了 DeptController");
    }

    /**
     * 此方法用于：查询全部部门信息
     *
     * @return Result
     */
    @GetMapping
    public Result<List<Dept>> list() {
        log.info("查询全部部门信息");

        List<Dept> depts = deptService.list();
        log.info("查询结果：{}", depts);

        return Result.success(depts);
    }

    /**
     * 此方法用于：删除部门信息，根据部门 Id
     *
     * @param id 部门 Id
     * @return Result
     */
    @DeleteMapping("/{id}")
    public Result<Null> removeById(@PathVariable Integer id) throws Exception {
        log.info("删除部门信息，id：{}", id);
        int i = deptService.removeById(id);

        return i > 0 ? Result.success(null)
                : Result.error("删除部门失败", null);
    }

    /**
     * 从方法用于：新增部门信息
     *
     * @param dept 新增部门信息
     * @return Result
     */
    @PostMapping
    public Result<Null> addDept(@RequestBody Dept dept) {
        log.info("新增部门信息：{}", dept);

        int i = deptService.addDept(dept);

        return i > 0 ? Result.success(null)
                : Result.error("新增部门失败", null);
    }

    /**
     * 此方法用于：根据部门 Id 查询部门信息
     *
     * @param id 部门 Id
     * @return Result<Dept>
     */
    @GetMapping("/{id}")
    public Result<Dept> getById(@PathVariable Integer id) {
        log.info("根据 id 查询部门信息，id：{}", id);

        Dept dept = deptService.getById(id);

        return dept != null ? Result.success(dept)
                : Result.error("根据 id 查询部门信息失败", null);
    }

    /**
     * 此方法用于：修改部门信息
     *
     * @param dept 修改部门信息
     * @return Result<Null>
     */
    @PatchMapping
    public Result<Null> updateDept(@RequestBody Dept dept) {
        log.info("修改部门信息：{}", dept);

        int i = deptService.updateDept(dept);

        return i > 0 ? Result.success(null)
                : Result.error("修改部门信息失败", null);
    }
}
