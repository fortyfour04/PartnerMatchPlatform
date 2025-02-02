package com.forty.huoban.once;

import com.forty.huoban.mapper.UserMapper;
import com.forty.huoban.model.domain.User;
import com.forty.huoban.service.UserService;
import io.reactivex.rxjava3.core.Completable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author: FortyFour
 * @description: 插入用户数据 单次使用类
 * @time: 2025/1/24 13:50
 * @version:
 */
@Component
public class InsertUsers {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    /**
     * 单条循环插入示例数据
     * 编写定时任务方式
     * 5416 ms / 1000 lines
     */
//    @Scheduled(fixedRate = Long.MAX_VALUE) // 设置任务执行间隔为长整型上限，实现假的单次执行
    public void doInsertUsers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUMB = 1000;
//        CompletableFuture<List> userFuture = CompletableFuture.supplyAsync(()->)
//        for (int i = 0; i < INSERT_NUMB; i++) {
//            User user = new User();
//            user.setUsername("Fake User");
//            user.setUserAccount("fakers");
//            user.setUserPassword("");
//            user.setGender(0);
//            user.setProfile("I'm faker");
//            user.setUserRole(0);
//            user.setAvatarUrl("https://pic4.zhimg.com/v2-5d11c5897785bb012a909307fb2213c7_r.jpg");
//            user.setUserStatus(1);
//            user.setEmail("asdad@qq.com");
//            user.setPhone("143445533");
//            user.setTags("[\"男\", \"研究生\", \"后端\"]");
//            userMapper.insert(user);
//        }
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }


}
