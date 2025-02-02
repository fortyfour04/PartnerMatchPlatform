package com.forty.huoban.service;

import com.forty.huoban.mapper.UserMapper;
import com.forty.huoban.model.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author: FortyFour
 * @description: 数据插入测试类。用户插入单元测试，注意打包时要删掉或忽略，不然打一次包就插入一次
 * @time: 2025/1/24 14:28
 * @version:
 */
@SpringBootTest
public class InsertUsersTest {

    @Resource
    private UserMapper userMapper;
    
    @Resource
    private UserService userService;

    /**
     * saveBatch批量插入数据
     * 1622ms / 1000 lines / batchSize = 100
     * 1408ms / 1000 lines / batchSize = 200
     * 18044ms / 100000 lines / batchSize = 1000
     */
    @Test
    public void doInsertUsers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 100000;
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < INSERT_NUM; i++) {
            User user = getFakeUser();
            userList.add(user);
        }
        userService.saveBatch(userList,200); //每200个数据查询并插入一次数据库
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

    /**
     * 并发执行插入数据
     * 利用 CompletableFuture 并发编程执行插入
     */
    @Test
    public void doCurrentInsertUsers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        int batchSize = 1000;
        int j = 0;
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

    /* 静态方法创建假数据 */
    private static User getFakeUser() {
        User user = new User();
        user.setUsername("Fake User");
        user.setUserAccount("fakers");
        user.setUserPassword("");
        user.setGender(0);
        user.setProfile("I'm faker");
        user.setUserRole(0);
        user.setAvatarUrl("https://pic4.zhimg.com/v2-5d11c5897785bb012a909307fb2213c7_r.jpg");
        user.setUserStatus(1);
        user.setEmail("asdad@qq.com");
        user.setPhone("143445533");
        user.setTags("[\"男\"]");
        return user;
    }

}
