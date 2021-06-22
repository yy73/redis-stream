package com.yy.redis.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.yy.redis.entity.Msg;
import com.yy.redis.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author ywl
 * @Date 2021/6/21 15:05
 * @Description
 */
@RestController
public class ProducerController {

    @Autowired
    private RedisUtil redisUtil;

    @Value("${redis.name}")
    private String consumer;

    @Value("${redis.group}")
    private String groups;

    @Value("${redis.streamKey}")
    private String streamKey;

    @GetMapping("/demo")
    public String demo() {
        return "hello producer";
    }

    @GetMapping("/xadd")
    public String xadd() {
        List<Msg> list = new ArrayList<>();
        for (int i = 0; i < 5000; i++) {
            Msg msg = new Msg();
            msg.setId("id: " + i + "");
            msg.setName("name: " + i);
            msg.setAge(i);
            String s = JSONUtil.toJsonStr(msg);
            Map<String, String> result = new HashMap<>();
            result.put("data", s);
            RecordId test = redisUtil.xadd(result, streamKey);
//            list.add(msg);
        }

        return "0";
    }

    @PostMapping("/add")
    public String add(@RequestBody List<Msg> list) {
        String s = JSONUtil.toJsonStr(list);
        Map<String, String> result = new HashMap<>();
        result.put("data", s);
        RecordId test = redisUtil.xadd(result, streamKey);
        return test.getValue();
    }

    @GetMapping("/isExitGroup")
    public boolean isExitGroup() {
        return redisUtil.isExitGroup(streamKey);
    }

    @GetMapping("/infoStream")
    public StreamInfo.XInfoStream infoStream() {
        return redisUtil.infoStream(streamKey);
    }

    @GetMapping("/infoGroups")
    public Object[] infoGroups() {
        return redisUtil.infoGroups(streamKey);
    }

    @GetMapping("/infoConsumers")
    public List<StreamInfo.XInfoConsumer> infoConsumers() {
        return redisUtil.infoConsumers(streamKey, groups);
    }
}
