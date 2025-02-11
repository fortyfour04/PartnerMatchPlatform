package com.forty.huoban.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author: FortyFour
 * @description:
 * @time: 2025/2/11 16:44
 * @version:
 */
@SpringBootTest
public class TeamServiceTest {

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamUserService teamUserService;

    @Test
    public void remove(){

        long teamId = 9;
        teamUserService.removeById(teamId);
    }
}
