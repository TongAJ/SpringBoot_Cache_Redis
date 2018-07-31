package com.atguigu.springboot.service;

import com.atguigu.springboot.bean.Department;
import com.atguigu.springboot.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 使用@CacheConfig(cacheManager = "deptCacheManager")
 * 定义该service下的所有方法使用指定的CacheManager
 */
@Service
@CacheConfig(cacheManager = "deptCacheManager")
public class DeptService {

    @Autowired
    private DepartmentMapper mapper;

    @Cacheable(value="dept")
    public Department getDeptById(Integer id){
        System.out.println("查询"+id+"号部门");
        return mapper.getDeptById(id);
    }

}
