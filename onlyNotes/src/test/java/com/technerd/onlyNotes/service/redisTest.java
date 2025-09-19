package com.technerd.onlyNotes.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class redisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Disabled
    @Test
    public void test(){
        redisTemplate.opsForValue().set("name", "abdul rahman");
        Object o = redisTemplate.opsForValue().get("salary");
        int a = 1;
    }
}
