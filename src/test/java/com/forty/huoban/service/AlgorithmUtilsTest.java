package com.forty.huoban.service;

import com.forty.huoban.model.domain.Tag;
import com.forty.huoban.utils.AlgorithmUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

/**
 * @author: FortyFour
 * @description: 编辑距离算法测试类 最终计算距离越小就越相似
 * @time: 2025/2/13 16:30
 * @version:
 */
@SpringBootTest
public class AlgorithmUtilsTest {

    @Test
    public void testString() {

    }

    @Test
    public void testTagList() {
        List<String> list1 = Arrays.asList("JAVA","Python","大一");
        List<String > list2 = Arrays.asList("JAVA","C++","大一");
        List<String> list3 = Arrays.asList("Go","大一");
        long score1 = AlgorithmUtils.minDistance(list1, list2);
        long score2 = AlgorithmUtils.minDistance(list1, list3);
        long score3 = AlgorithmUtils.minDistance(list2, list3);
        System.out.println(score1);
        System.out.println(score2);
        System.out.println(score3);
    }
}
