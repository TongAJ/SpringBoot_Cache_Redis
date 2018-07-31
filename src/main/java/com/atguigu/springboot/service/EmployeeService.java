package com.atguigu.springboot.service;

import com.atguigu.springboot.bean.Employee;
import com.atguigu.springboot.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


/**
 * @CacheConfig
 * cacheNames：统一指定方法中的value
 * keyGenerator：统一指定方法中的keyGenerator
 * cacheManager：统一指定方法中的cacheManager
 *
 *
 * @Caching
 * 可以使用复杂缓存逻辑，结合@Cachable、@CachePut和@CacheEvict一起使用
 * public @interface Caching {
 *     Cacheable[] cacheable() default {};
 *     CachePut[] put() default {};
 *     CacheEvict[] evict() default {};
 * }
 */
@Service
@CacheConfig(cacheNames = "emp",cacheManager = "empCacheManager")
public class EmployeeService {
    @Autowired
    private EmployeeMapper mapper;

    /**
     * 将方法的运行结果进行缓存：以后查相同的数据，就从缓存中获取，而不使用查询数据库
     * CacheManager管理多个Cache组件，对缓存的真正CRUD操作在Cache组件中，每一个缓存有自己的唯一一个名字
     * 属性分析：
     *      cacheNames/value：指定缓存组件的名字
     *      key：可以指定缓存数据使用的key。默认是方法的参数作为key：如/emp/1,则key=1
     *          接收Spel：#id,#a0,#p0,#root.args[0]
     *      keyGenerator：key的生成器，可以指定key的生成策略
     *          key和keyGenerator二选一
     *      cacheManager：指定缓存管理器（Redis或者别的缓存管理器）
     *      cacheResolver：同cacheManager，两者二选一
     *      condition：符合条件才缓存
     *          #id>0
     *      unless：符合条件就不缓存
     *          #result==null
     *      sync：是否使用异步模式
     *
     * 原理：
     *  1：自动配置类：CacheAutoConfiguration
     *  2：默认生效的配置类：SimpleCacheConfiguration matched
     *      1）：【注册一个ConcurrentMapCacheManager】
     *      ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
     *      2）：【缓存数据存放】ConcurrentMap<String, Cache> cacheMap
     *
     * 流程：
     * @Cacheable
     *  1：方法执行之前，根据容器中注册的CacheManager，去查询对应的Cache组件
     *      第一次没有Cache组件，会自动创建
     *  2：Cache组件根据key（默认参数值）去查询缓存map是否有值
     *      key是由keyGenerator生成的，默认使用SimpleKeyGenerator
     *  3：没有缓存数据，则执行目标方法
     *  4：将目标方法返回结果，put进Cache组件的缓存map中
     *
     *  【核心】
     *      1：根据CacheManager生成Cache组件，默认【ConcurrentMapCacheManager】生成【ConcurrentMapCache】
     *      2：key是由keyGenerator生成的，默认【SimpleKeyGenerator】
     *      3：查询，根据key先看缓存，没有，则执行目标方法，然后将返回结果存入缓存，
     *         下次查询，相同的key就直接从cache组件中的map直接获取，并返回cache，不执行目标方法
     *      4：key不能使用#result，因为是先存key，才得到返回值
     *
     * @param id
     * @return
     */
    @Cacheable(cacheNames = {"emp"}/*,keyGenerator = "myKeyGenerator",condition = "#id>1",unless = "#result.id==2"*/)
    public Employee getEmployee(Integer id){
        System.out.println("查询"+id+"号员工");
        Employee employee = mapper.getEmployee(id);
        return employee;
    }

    /**
     * 与@Cacheable不同
     * 流程：
     *  1：先执行目标方法
     *  2：将返回结果存放到缓存中，所以key可以使用#result获取返回结果对象
     *  3：如果要更新查询方法的缓存，需要将key与查询方法的key保持相同
     *
     *
     * @param employee
     * @return
     */
    @CachePut(value = "emp",key = "#result.id")
    public Employee updateEmployee(Employee employee){
        System.out.println("更新"+employee.getdId()+"号员工");
        mapper.updateEmployee(employee);
        return employee;
    }

    /**
     * @CacheEvict
     * 1：allEntries 将缓存中的value=emp的对象全部清除
     * 2：beforeInvocation：默认false--在方法执行后才删除缓存
     *      如果设置true，则无论方法是否执行成功，都先删除缓存
     *
     * @param id
     */
    @CacheEvict(value = "emp"/*,key = "#id" allEntries = true,beforeInvocation = true*/)
    public void deleteEmployee(Integer id){
        System.out.println("删除"+id+"号员工");
//      mapper.deleteEmployee(id);
    }
}
