package com.forty.huoban.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author: FortyFour
 * @description: jedis test
 * @time: 2025/2/13 13:37
 * @version:
 */
@SpringBootTest
@Slf4j
public class JedisTest {

    @Test
    public void TestJedis() {

        //1、生成一个jedis对象，这个对象负责和指定Redis节点进行通信
        Jedis jedis = new Jedis("localhost", 6379);
        //带密码需要执行认证方法，这里我的Redis没有设密码就不用管
        //jedis.auth("123456");

        //2、jedis存入数据
        jedis.set("hello", "world");
        //3、jedis获取数据
        String value = jedis.get("hello");

        log.info("从redis中获取数据{}", value);
        jedis.close();
    }
}
