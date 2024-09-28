package com.kkcf.service.impl;

import com.kkcf.mapper.DeptLogMapper;
import com.kkcf.pojo.DeptLog;
import com.kkcf.service.DeptLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeptLogServiceImpl implements DeptLogService {
    @Autowired
    private DeptLogMapper deptLogMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public int add(DeptLog deptLog) {
        return deptLogMapper.insert(deptLog);
    }
}
