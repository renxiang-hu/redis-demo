package com.example;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@SpringBootTest
@Component
public class RedisTest {

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void testSpring(){
        redisTemplate.opsForValue().set("mall","五角场");
        Object mall = redisTemplate.opsForValue().get("mall");
        System.out.println("mall = " + mall);
    }

}
