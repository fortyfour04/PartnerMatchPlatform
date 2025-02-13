package com.forty.huoban.service;

import com.forty.huoban.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

/**
 * @author: FortyFour
 * @description:
 * @time: 2025/1/26 14:13
 * @version:
 */
@SpringBootTest
public class RedisTest {

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    void test(){
        //创建redis键值对
        ValueOperations valueOperations = redisTemplate.opsForValue();

        //增
        valueOperations.set("forty", "kksk");
        valueOperations.set("naruto", 18);
        valueOperations.set("sasike", 19.2);
        valueOperations.set("sakura", 20);
        User user = new User();
        user.setId(9L);
        user.setUsername("kk");
        valueOperations.set("user", user);

        //查
        Object forty = valueOperations.get("forty");
        Assertions.assertTrue("kksk".equals(forty.toString()));
        Object naruto = valueOperations.get("naruto");
        Assertions.assertTrue(18 == (Integer) naruto);
        Object sasike = valueOperations.get("sasike");
        Assertions.assertTrue(19.2 == (Double) sasike);
        Object sakura = valueOperations.get("sakura");
        Assertions.assertTrue(20 == (Integer) sakura);
        System.out.println(valueOperations.get("user"));

    }
}
