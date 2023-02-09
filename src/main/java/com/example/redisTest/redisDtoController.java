package com.example.redisTest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("redisTest")
public class redisDtoController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("deduct_stock")
    public String deductStock(){
        String ticketKey = "stock";
        int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get(ticketKey));
        if (stock > 0){
            int realStock = stock - 1;
            stringRedisTemplate.opsForValue().set(ticketKey,realStock+"");
            System.out.println("扣减成功，剩余库存" + realStock);
        } else {
            System.out.println("扣减失败，库存不足");
        }
        return "end";
    }
}
