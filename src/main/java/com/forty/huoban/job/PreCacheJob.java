package com.forty.huoban.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forty.huoban.mapper.UserMapper;
import com.forty.huoban.model.domain.User;
import com.forty.huoban.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: FortyFour
 * @description: 缓存预热定时任务
 * @time: 2025/1/27 13:37
 * @version:
 */
@Component
@Slf4j
public class PreCacheJob {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UserService userService;

    @Resource
    private RedissonClient redissonClient;

    /* 重点用户Id白名单: 只对部分重要用户进行缓存预热,暂时以确定的方式编写 */
    private final List<Long> mainUserList = Arrays.asList(1L,2L);

    @Scheduled(cron = "0 0 2 * * *")
    public void preCache() {
        //利用redissonClient获取分布式锁
        RLock lock = redissonClient.getLock("forty:preCacheJob:doPreCache:lock");
        try {
            //设置waitTime = 0 即每个线程每次只抢一次，锁的过期时间为30000ms
            if (lock.tryLock(0,30000L,TimeUnit.MILLISECONDS)) {
                for (Long userId : mainUserList) {
                    //查数据库后存入内存
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    Page<User> userPage = userService.page(new Page<>(1,20), queryWrapper);
                    //用redis查询来优化主页的查询性能
                    ValueOperations valueOperations = redisTemplate.opsForValue();
                    //自定义redis键的名称，要注意不要与已有名称冲突
                    String redisKey = String.format("forty:user:recommend:%s",userId);
                    try {
                        valueOperations.set(redisKey,userPage,30000, TimeUnit.MILLISECONDS);  //1w毫秒
                    } catch (Exception e) {
                        log.error("redis set key error",e);
                    }
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            //无论是否抛出异常最后都要
            lock.unlock();
        }
    }
}
