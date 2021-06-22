package com.yy.redis;

import com.yy.redis.config.ProducerApplicationRunner;
import com.yy.redis.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RedisStreamProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisStreamProducerApplication.class, args);
    }

}