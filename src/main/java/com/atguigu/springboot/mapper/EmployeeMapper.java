package com.atguigu.springboot.mapper;

import com.atguigu.springboot.bean.Employee;
import org.apache.ibatis.annotations.*;

/**
 * mybatis的mapper类
 */
@Mapper
public interface EmployeeMapper {

    @Select("SELECT * FROM employee where id=#{id}")
    Employee getEmployee(Integer id);

    @Insert("INSERT INTO employee VALUES (#{lastName},#{email},#{gender},#{dId})")
    void addEmployee(Employee employee);

    @Update("UPDATE employee SET lastName=#{lastName},email=#{email},gender=#{gender},d_id=#{dId} WHERE id=#{id}")
    void updateEmployee(Employee employee);

    @Delete("DELETE FROM employee WHERE id=#{id}")
    void deleteEmployee(Integer id);
}
