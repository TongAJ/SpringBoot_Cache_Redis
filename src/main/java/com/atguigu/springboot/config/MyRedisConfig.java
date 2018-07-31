package com.atguigu.springboot.config;

import com.atguigu.springboot.bean.Department;
import com.atguigu.springboot.bean.Employee;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
public class MyRedisConfig {

    /**
     *  RedisAutoConfiguration中的RedisTemplate定义
     *         @Bean
     *         @ConditionalOnMissingBean(
     *             name = {"redisTemplate"}
     *         )
     *         public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
     *             RedisTemplate<Object, Object> template = new RedisTemplate();
     *             template.setConnectionFactory(redisConnectionFactory);
     *             return template;
     *         }
     *
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<Object,Employee> employeeRedisTemplate
            (RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<Object,Employee> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer redisSerializer = new Jackson2JsonRedisSerializer(Employee.class);
        redisTemplate.setDefaultSerializer(redisSerializer);
        return redisTemplate;
    }

    /**
     * 由于这里注入了两个RedisCacheManager
     * 所以需要使用@Primary注解来定义默认注入的RedisCacheManager
     *
     * @param redisTemplate
     * @return
     */
    @Primary
    @Bean
    public RedisCacheManager cacheManager(RedisTemplate<Object, Object> redisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        cacheManager.setUsePrefix(true);
        return cacheManager;
    }

    /**
     * 自定义cacheManager替换RedisCacheManager中默认的cacheManager
     * 并使用我们自定义的redisTemplate作为构造cacheManager的参数
     * 构造cacheManager，用于Employee的cache组件的使用
     * @param employeeRedisTemplate
     * @return
     */
    @Bean
    public RedisCacheManager empCacheManager(RedisTemplate<Object,Employee> employeeRedisTemplate) {
       RedisCacheManager cacheManager = new RedisCacheManager(employeeRedisTemplate);
       //设置key的前缀
       cacheManager.setUsePrefix(true);
       return cacheManager;
    }

    /**
     * 应用于Department的RedisTemplate
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<Object,Department> departmentRedisTemplate
            (RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<Object,Department> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer redisSerializer = new Jackson2JsonRedisSerializer(Department.class);
        redisTemplate.setDefaultSerializer(redisSerializer);
        return redisTemplate;
    }

    /**
     * 构造cacheManager，用于department的cache组件的使用
     * @param departmentRedisTemplate
     * @return
     */
    @Bean
    public RedisCacheManager deptCacheManager(RedisTemplate<Object,Department> departmentRedisTemplate){
        RedisCacheManager cacheManager = new RedisCacheManager(departmentRedisTemplate);
        //设置key的前缀
        cacheManager.setUsePrefix(true);
        return cacheManager;
    }
}
