package com.atguigu.springboot;

import com.atguigu.springboot.bean.Employee;
import com.atguigu.springboot.mapper.EmployeeMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootCacheApplicationTests {
    /**
     * 当redis的starter被引入
     * 可以使用两个Template:RedisTemplate----StringRedisTemplate
     * StringRedisTemplate：k-v都是string的RedisTemplate
     *      stringRedisTemplate.opsForValue()---操作string
     *      stringRedisTemplate.opsForList()---操作List
     *      stringRedisTemplate.opsForSet()---操作Set
     *      stringRedisTemplate.opsForHash()---操作Hash
     *      stringRedisTemplate.opsForZSet()---操作ZSet
     *
     * RedisTemplate：k-v都是object的RedisTemplate
     *      redisTemplate.opsForValue()---操作string
     *      redisTemplate.opsForList()---操作List
     *      redisTemplate.opsForSet()---操作Set
     *      redisTemplate.opsForHash()---操作Hash
     *      redisTemplate.opsForZSet()---操作ZSet
     *
     */

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate employeeRedisTemplate;

    @Autowired
    private EmployeeMapper mapper;

    @Test
    public void testStringRedisTemplate(){
        /*stringRedisTemplate.opsForValue().append("msg","hello");*/
        String msg = stringRedisTemplate.opsForValue().get("msg");
        System.out.println("msg = " + msg);
    }

    @Test
    public void testRedisTemplate(){
        Employee employee = mapper.getEmployee(1);
        /*
        redisTemplate存放对象是，对象必须实现序列化接口Serializable
        默认使用jdk的序列化规则，但是生成的内容不合适
        emp-01===>\xAC\xED\x00\x05t\x00\x06emp-01
        推荐自己创建自己类的序列化生成器
        private RedisSerializer<?> defaultSerializer;
        */
        redisTemplate.opsForSet().add("emp-01", employee);
    }

    @Test
    public void testEmpRedisTemplate(){
        Employee employee = mapper.getEmployee(1);
        /*
        RedisTemplate<Object,Employee> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //设置Json序列化工具为RedisTemplate的默认序列化工具
        Jackson2JsonRedisSerializer redisSerializer = new Jackson2JsonRedisSerializer(Employee.class);
        redisTemplate.setDefaultSerializer(redisSerializer);
        */
        employeeRedisTemplate.opsForSet().add("emp-01", employee);
    }

    @Test
    public void contextLoads() {
        /*Employee newEmployee = new Employee();
        newEmployee.setLastName("AJ");
        newEmployee.setGender(0);
        newEmployee.setEmail("AJ@atguigu.com");
        mapper.addEmployee(newEmployee);*/
        Employee employee = mapper.getEmployee(1);
        System.out.println("employee = " + employee);
    }

}
