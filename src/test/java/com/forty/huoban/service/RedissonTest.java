package com.forty.huoban.service;

import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author: FortyFour
 * @description: RedissonTest
 * @time: 2025/2/2 15:26
 * @version:
 */
@SpringBootTest
public class RedissonTest {

    @Resource
    private RedissonClient redisson;

    @Test
    public void redissonTest() {
        RList<String> list = redisson.getList("list");
//        list.add("1");
        System.out.println("list:" + list);
        list.remove("1");
    }

}
