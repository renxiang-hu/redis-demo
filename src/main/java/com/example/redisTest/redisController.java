package com.example.redisTest;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("redis")
public class redisController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private Redisson redisson;

    /**
     * 添加redis
     * @return
     */
    @PostMapping("insert")
    public String insertRedis(){
        String lockKey = "ticket";
        stringRedisTemplate.opsForValue().set(lockKey,"20");
        return "添加成功";
    }

    /**
     * 扣减操作，用了synchronized锁，只针对单机器而言可行，但是多台机器同时运行会出现问题，这时就会用到分布式锁
     * @return
     */
    @PostMapping("operate")
    public String deductTicket(){
        String lockKey = "ticket";
        synchronized (redisController.class){
            if (!StringUtils.isEmpty(stringRedisTemplate.opsForValue().get(lockKey))){
                int ticketCount = Integer.parseInt(stringRedisTemplate.opsForValue().get(lockKey));
                if (ticketCount > 0){
                    int realTicketCount = ticketCount - 1;
                    log.info("扣减成功，剩余票数{}" , realTicketCount);
                    stringRedisTemplate.opsForValue().set(lockKey,realTicketCount+"");
                } else {
                    log.error("扣减失败，余票不足");
                }
            }
        }
      return "end";
    }

    /**
     * 采用多个线程进行模拟，调用的方法中加上synchronized可以解决同步问题
     * @throws InterruptedException
     */
    @PostMapping("threadOperate")
    public void threadOper() throws InterruptedException {
        Thread thread = new Thread(() -> {
            this.deductTicket();
        });
        Thread thread1 = new Thread(() -> {
            this.deductTicket();
        });
        thread.start();
        thread1.start();
    }

    /**
     * 采用redis分布式锁，setNx
     * @return
     */
    @PostMapping("/setNx")
    public String deduceTickets(){
       String lockKey = "ticket";
       try {
           //这里只是举例，真正的value不能为dewo
           Boolean dewo = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "dewo",10,TimeUnit.MINUTES);
           if (Boolean.FALSE.equals(dewo)){
               return "error";
           }
           if (!StringUtils.isEmpty(stringRedisTemplate.opsForValue().get(lockKey))){
               int ticketCount = Integer.parseInt(stringRedisTemplate.opsForValue().get(lockKey));
               if (ticketCount > 0){
                   int realTicketCount = ticketCount - 1;
                   log.info("扣减成功，剩余票数{}" , realTicketCount);
                   stringRedisTemplate.opsForValue().set(lockKey,realTicketCount+"");
               } else {
                   log.error("扣减失败，余票不足");
               }
           }
       } finally {
            stringRedisTemplate.delete(lockKey);
       }
       return "end";
    }

//    @PostMapping("redisson")
//    public String deductTic(){
//        String lockKey = "ticket";
//        RLock lock = redisson.getLock(lockKey);
//        try {
//            lock.lock();
//            if (!StringUtils.isEmpty(stringRedisTemplate.opsForValue().get(lockKey))){
//                int ticketCount = Integer.parseInt(stringRedisTemplate.opsForValue().get(lockKey));
//                if (ticketCount > 0){
//                    int realTicketCount = ticketCount - 1;
//                    log.info("扣减成功，剩余票数{}" , realTicketCount);
//                    stringRedisTemplate.opsForValue().set(lockKey,realTicketCount+"");
//                } else {
//                    log.error("扣减失败，余票不足");
//                }
//            }
//        } finally {
//            lock.unlock();
//        }
//        return "end";
//    }

    @RequestMapping("/deduct_stock")
    public String deductStock(){
        String lockKey = "stock";
        int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
        if (stock > 0){
            int realStock = stock - 1;
            stringRedisTemplate.opsForValue().set(lockKey,realStock+"");
            log.info("扣减成功，剩余库存{}",realStock);
        } else {
            log.error("扣减失败，库存不足");
        }
        return "end";
    }
}
