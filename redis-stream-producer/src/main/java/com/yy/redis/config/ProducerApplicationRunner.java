package com.yy.redis.config;

import com.yy.redis.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.stream.StreamInfo;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author ywl
 * @Date 2021/6/21 16:51
 * @Description 判断消费者组 是否存在
 */
@Configuration
public class ProducerApplicationRunner implements ApplicationRunner {
    static final Logger logger = LoggerFactory.getLogger(ProducerApplicationRunner.class);

    @Value("${redis.group}")
    private String groups;

    @Value("${redis.streamKey}")
    private String streamKey;

    @Autowired
    private RedisUtil redisUtil;


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        StreamOperations<String, Object, Object> opsForStream = this.stringRedisTemplate.opsForStream();
        try {
            StreamInfo.XInfoGroups info = opsForStream.groups(streamKey);
            logger.info("消费者组存在，无须创建。");
        } catch (Exception e) {
            // 如果出现异常，则证明 没有创建 消费者组，
            String group1 = redisUtil.createGroup(streamKey, groups);
            logger.info("创建消费者组成功，对应id:{}", group1.toString());
        }

    }
}
