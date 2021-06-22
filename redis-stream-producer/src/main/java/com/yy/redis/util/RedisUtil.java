package com.yy.redis.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author ywl
 * @Date 2021/6/21 15:12
 * @Description
 */
@Component
public class RedisUtil {
    static final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 向流中追加记录，若流不存在，则创建
     *
     * @param record    记录类型为Map<String,String>
     * @param streamKey
     * @return 追加消息的RecordId
     */
    public RecordId xadd(Map<String, String> record, String streamKey) {
        try {
            StringRecord stringRecord = StreamRecords.string(record).withStreamKey(streamKey);
            // 刚追加记录的记录ID
            RecordId recordId = this.stringRedisTemplate.opsForStream().add(stringRecord);
            LOGGER.info(recordId.getValue());
            return recordId;
        } catch (Exception e) {
            LOGGER.error("xadd error：" + e.getMessage(), e);
            return null;
        }
    }

    /**
     * 流消息消费确认
     *
     * @param groupName
     * @param record
     * @return 成功确认的消息数
     */
    public Long xack(String groupName, Record record) {
        try {
            return this.stringRedisTemplate.opsForStream().acknowledge(groupName, record);
        } catch (Exception e) {
            LOGGER.error("xack error：" + e.getMessage(), e);
            return 0L;
        }
    }

    /**
     * 创建消费者 组
     *
     * @param streamKey redis key
     * @param group     消费者组
     * @return
     */
    public String createGroup(String streamKey, String group) {
        String group1 = this.stringRedisTemplate.opsForStream().createGroup(streamKey, group);
        return group1;
    }

    /**
     * 查看流和消费者组信息
     *
     * @param streamKey redis key
     * @return
     */
    public StreamInfo.XInfoStream infoStream(String streamKey) {
        return this.stringRedisTemplate.opsForStream().info(streamKey);
    }

    /**
     * 查看消费者组信息
     *
     * @param streamKey redis key
     * @return
     */
    public Object[] infoGroups(String streamKey) {
        return this.stringRedisTemplate.opsForStream().groups(streamKey).stream().toArray();
    }

    /**
     * 消费者信息
     *
     * @param streamKey redis key
     * @param group     消费者组
     * @return
     */
    public List<StreamInfo.XInfoConsumer> infoConsumers(String streamKey, String group) {
        return this.stringRedisTemplate.opsForStream().consumers(streamKey, group).stream().collect(Collectors.toList());
    }

    /**
     * 判断是否存在消费者组
     *
     * @param streamKey redis key
     * @return
     */
    public boolean isExitGroup(String streamKey) {
        StreamInfo.XInfoGroups info = this.stringRedisTemplate.opsForStream().groups(streamKey);
        return info.isEmpty();
    }

}
