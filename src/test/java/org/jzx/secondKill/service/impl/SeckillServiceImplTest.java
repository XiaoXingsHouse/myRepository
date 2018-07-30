package org.jzx.secondKill.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.jzx.secondKill.dto.Exposer;
import org.jzx.secondKill.dto.SeckillExecution;
import org.jzx.secondKill.entity.SecKill;
import org.jzx.secondKill.exception.SeckillException;
import org.jzx.secondKill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class SeckillServiceImplTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() {
        List<SecKill> list = seckillService.getSeckillList();
        logger.info("list = {}", list);
    }

    @Test
    public void getById() {
        long id = 1001;
        SecKill secKill = seckillService.getById(id);
        logger.info("seckill={}", secKill);
    }

    @Test
    public void exportSeckillUrl() {
        long id = 1001;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        logger.info("exposer={}", exposer);
    }

    @Test
    public void executeSeckill() {
        long id = 1001;
        long phone = 12343299322L;
        String md5 = "1c03bf380af8575af123005fed406238";
        SeckillExecution seckillExecution = seckillService.executeSeckill(id, phone, md5);
        logger.info("result = {}", seckillExecution);
    }

    //完整测试
    @Test
    public void testSeckillLogic() throws Exception{
        long id = 1001;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if(exposer.isExposed()){
            logger.info("exposer = {}", exposer);
            long phone = 15323996654L;
            String md5 = exposer.getMd5();
            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(id, phone, md5);
                logger.info("result = {}", seckillExecution);
            } catch (SeckillException e) {
                e.printStackTrace();
            }
        }else{
            logger.warn("exposer = {}", exposer);
        }
    }
}