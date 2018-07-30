package org.jzx.secondKill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.jzx.secondKill.entity.SuccessKilled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKillDaoTest {

    @Autowired
    private SuccessKillDao successKillDao;

    @Test
    public void insertSuccessKilled() {
        long id = 1001;
        long phone = 13355436763L;
        int insertCount = successKillDao.insertSuccessKilled(id, phone, new Date());
        System.out.println(insertCount);
    }

    @Test
    public void queryByIdWithSeckill() {
        long id = 1001;
        long phone = 15323996654L;
        SuccessKilled successKilled = successKillDao.queryByIdWithSeckill(id, phone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }
}