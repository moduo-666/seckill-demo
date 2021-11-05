package com.moduo.seckill;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Wu Zicong
 * @create 2021-11-03 9:47
 */
@SpringBootTest
public class SeckillApplicationTests {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DefaultRedisScript script;

  /** 进来一个线程先占位，当别的线程进来操作时，
   * 发现已经有人占位了，就会放弃或者稍后再试 线程操作执行完成后，
   * 需要调用del指令释放位子 */
  @Test
  public void testLock01() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Boolean isLock = valueOperations.setIfAbsent("k1", "v1");
        if(isLock){
            valueOperations.set("name","xxxx");
            String name = (String)valueOperations.get("name");
            System.out.println(name);
            redisTemplate.delete("k1");
        }else{
            System.out.println("有线程在使用，请稍后");
        }
    }
    /** 为了防止业务执行过程中抛异常或者挂机导致del指定没法调用形成死锁，
     * 可以添加超时时间 */
    @Test
    public void testLock02() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Boolean isLock = valueOperations.setIfAbsent("k1", "v1",5, TimeUnit.SECONDS);
        if(isLock){
            valueOperations.set("name","xxxx");
            String name = (String)valueOperations.get("name");
            System.out.println(name);
            redisTemplate.delete("k1");
        }else{
            System.out.println("有线程在使用，请稍后");
        }
    }
  /**
   * 上面例子，如果业务非常耗时会紊乱。
   * 举例：第一个线程首先获得锁，然后执行业务代码，但是业务代 码耗时8秒，
   *       这样会在第一个线程的任务还未执行成功锁就会被释放，
   *       这时第二个线程会获取到锁开始执行，
   *       在第二个线程开执行了3秒，第一个线程也执行完了，
   *       此时第一个线程会释放锁，
   *       但是注意，他 释放的是 第二个线程的锁，释放之后，第三个线程进来。
   *
   *  解决方案：
   *    尽量避免在获取锁之后，执行耗时操作
   *    将锁的value设置为一个随机字符串，
   *    每次释放锁的时候，都去比较随机字符串是否一致，
   *    如果一致，再去释放，否则不释放。
   *    释放锁时要去查看所得value，比较value是否正确，
   *    释放锁总共分3步，这3步不具备原子性。
   */
  @Test
    public void testLock03(){
      ValueOperations valueOperations = redisTemplate.opsForValue();
      String value = UUID.randomUUID().toString();
      Boolean isLock = valueOperations.setIfAbsent("k1", value, 5, TimeUnit.SECONDS);
      //没人占位
      if(isLock){
          valueOperations.set("name","xxxx");
          String name = (String)valueOperations.get("name");
          System.out.println(name);
          System.out.println(valueOperations.get("k1"));
          //释放锁
          Boolean result = (Boolean)redisTemplate.execute(script, Collections.singletonList("k1"), value);
          System.out.println(result);
      }else{
          System.out.println("有线程在使用，请稍后");
      }
  }
}
