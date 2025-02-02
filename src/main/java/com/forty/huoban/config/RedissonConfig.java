package com.forty.huoban.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: FortyFour
 * @description: Redisson配置
 * @time: 2025/2/2 13:28
 * @version:
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissonConfig {

    private String host;

    private String port;

    @Bean
    public RedissonClient redisson() {
        //1.创建配置信息
        Config config = new Config();
        String redisAddress = String.format("redis://%s:%s", host, port);
        config.useSingleServer().setAddress(redisAddress).setDatabase(0);
        //2。创建示例
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }
}
