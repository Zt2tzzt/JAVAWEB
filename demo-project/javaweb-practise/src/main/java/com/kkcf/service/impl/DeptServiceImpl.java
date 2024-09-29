package com.kkcf.service.impl;

import com.kkcf.mapper.DeptMapper;
import com.kkcf.mapper.EmpMapper;
import com.kkcf.pojo.Dept;
import com.kkcf.pojo.DeptLog;
import com.kkcf.service.DeptLogService;
import com.kkcf.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private DeptLogService deptLogService;

    @Override
    public List<Dept> list() {
        return deptMapper.selectDepts();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int removeById(Integer id) throws Exception {
        try {
            // 删除部门下的员工
            empMapper.deleteByDeptId(id);

            //int i = 1 / 0;
            if (true) {
                throw new Exception("出错啦……");
            }

            // 删除部门
            deptMapper.deleteDeptById(id);
        } finally {
            DeptLog deptLog = new DeptLog();
            deptLog.setCreateTime(LocalDate.now());
            deptLog.setDescription("执行了解散部门的操作，此次解散的部门是 " + id + " 号部门");

            deptLogService.add(deptLog);
        }

        return 0;
    }

    @Override
    public int addDept(Dept dept) {
        dept.setCreateTime(LocalDate.now());
        dept.setUpdateTime(LocalDate.now());
        return deptMapper.insertDept(dept);
    }

    @Override
    public Dept getById(Integer id) {
        return deptMapper.selectDeptById(id);
    }

    @Override
    public int updateDept(Dept dept) {
        dept.setUpdateTime(LocalDate.now());
        return deptMapper.updateDept(dept);
    }
}
