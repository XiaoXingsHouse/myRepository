package org.jzx.secondKill.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.jzx.secondKill.dao.SeckillDao;
import org.jzx.secondKill.entity.SecKill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {
    private long id = 1002;
    @Autowired
    private RedisDao redisDao;
    @Autowired
    private SeckillDao seckillDao;

    @Test
    public  void testSeckill() throws Exception{
        SecKill secKill = redisDao.getSeckill(id);
        if(secKill == null){
            secKill = seckillDao.queryById(id);
            if (secKill != null) {
                String result = redisDao.putSeckill(secKill);
                System.out.println(result);
                System.out.println(redisDao.getSeckill(id));
            }
        }
    }
}