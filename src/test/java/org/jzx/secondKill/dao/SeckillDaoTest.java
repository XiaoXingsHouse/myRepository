package org.jzx.secondKill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.jzx.secondKill.entity.SecKill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void queryById() {
        long id = 1000;
        SecKill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }

    @Test
    public void queryAll() {
        List<SecKill> seckills = seckillDao.queryAll(0, 10);
        for(SecKill secKill : seckills){
            System.out.println(secKill);
        }
    }

    @Test
    public void reduceNumber() {
        Date killTime = new Date();
        int updateCount = seckillDao.reduceNumber(1000, killTime);
        System.out.println(updateCount);
    }

}