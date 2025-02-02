package com.forty.huoban.service;

import com.forty.huoban.model.domain.User;
import com.forty.huoban.utils.Result;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author: FortyFour
 * @description:
 * @time: 2024/11/21 14:25
 * @version:
 */
@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void searchByUserTags() {

        List<String> tags = Arrays.asList("JAVA");
        List<User> users = userService.searchByUserTags(tags);
        Assert.assertNotNull(users);

    }
}