package com.atguigu.springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 1：创建数据库-表：spring_cache---employee、department
 * 2：创建model类Employee、Department
 * 3：整合mybatis操作数据库
 *      1：配置数据源信息
 *      2：使用注解版Mybatis
 *          1）：使用@mMapperScan指定要扫描的mapper所在包
 *
 * 4：快速体验缓存
 *      步骤：
 *          1：【@EnableCaching】开启注解的缓存 主配置类
 *          2：【标注缓存注解】
 *          @Cacheable
 *          @CacheEvict
 *          @CachePut
 *
 * Redis缓存
 *  1：docker安装redis镜像
 *      docker run -d -p 6379:6379 --name myredis registry.docker-cn.com/library/redis
 *  2：安装Redis Desktop Manager
 *  3：引入redis的starter
 *  4：配置redis
 *      配置文件中加入:spring.redis.host=10.99.32.228
 *
 * SpringBoot默认使用SimpleCacheConfiguration-->ConcurrentMapManager-->>ConcurrentMapCache
 * 引入redis的starter之后，RedisCacheConfiguration替换SimpleCacheConfiguration成为默认
 * 其中就注入了一个RedisCacheManager的cacheManager
 *     @Bean
 *     public RedisCacheManager cacheManager(RedisTemplate<Object, Object> redisTemplate) {
 *         RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
 *         cacheManager.setUsePrefix(true);
 *         List<String> cacheNames = this.cacheProperties.getCacheNames();
 *         if (!cacheNames.isEmpty()) {
 *             cacheManager.setCacheNames(cacheNames);
 *         }
 *
 *         return (RedisCacheManager)this.customizerInvoker.customize(cacheManager);
 *     }
 *
 * 注入的RedisTemplate是以RedisTemplate<Object, Object> redisTemplate
 * 但并没有修改默认的序列化工具，还是默认的jdk序列化，而且k-v需要改为Object-Employee
 * 所以我们要自己定义一个cacheManager，并修改构造参数redisTemplate使用我们之前自定义的redisTemplate
 *
 *      @Bean
 *     public RedisCacheManager cacheManager(RedisTemplate<Object,Employee> employeeRedisTemplate) {
 *        RedisCacheManager cacheManager = new RedisCacheManager(employeeRedisTemplate);
 *        //设置key的前缀
 *        cacheManager.setUsePrefix(true);
 *        return cacheManager;
 *     }
 *
 *     由于构造函数是使用RedisTemplate<Object,Employee> employeeRedisTemplate
 *     用户查询部门信息是，反序列化就会出现错误，
 *     解决办法：
 *          定义RedisTemplate<Object,Department> deptRedisTemplate
 *          定义DeptCacheManager类型的cacheManager，构造函数参数使用deptRedisTemplate
 *
 */
@MapperScan(basePackages = "springboot.mapper")
@SpringBootApplication
@EnableCaching
public class SpringbootCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootCacheApplication.class, args);
    }
}
