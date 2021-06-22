package com.yy.redis.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author ywl
 * @Date 2021/6/21 15:31
 * @Description 接收 消息 并进行处理
 */
@Component
public class StreamMessageListener implements StreamListener<String, MapRecord<String, String, String>> {
    static final Logger LOGGER = LoggerFactory.getLogger(StreamMessageListener.class);

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    @Value("${redis.streamKey}")
    private String streamKey;

    @Value("${redis.group}")
    private String group;

    @Override
    public void onMessage(MapRecord<String, String, String> message) {

        // 消息ID
        RecordId messageId = message.getId();

        // 消息的key和value
        Map<String, String> body = message.getValue();

        LOGGER.info("stream message。messageId={}, stream={}, body={}", messageId, message.getStream(), body);

        // 通过RedisTemplate手动确认消息
        this.stringRedisTemplate.opsForStream().acknowledge(group, message);
    }
}