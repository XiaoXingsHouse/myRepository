package org.jzx.secondKill.service.impl;

import org.jzx.secondKill.dao.SeckillDao;
import org.jzx.secondKill.dao.SuccessKillDao;
import org.jzx.secondKill.dao.cache.RedisDao;
import org.jzx.secondKill.dto.Exposer;
import org.jzx.secondKill.dto.SeckillExecution;
import org.jzx.secondKill.entity.SecKill;
import org.jzx.secondKill.entity.SuccessKilled;
import org.jzx.secondKill.enums.SeckillStateEnum;
import org.jzx.secondKill.exception.RepeatKillException;
import org.jzx.secondKill.exception.SeckillCloseException;
import org.jzx.secondKill.exception.SeckillException;
import org.jzx.secondKill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKillDao successKillDao;
    @Autowired
    private RedisDao redisDao;

    private final String slat = "fewjfwji*#U$*QU*$IDJDJID*((IU*@&#*34)(FD)+";

    public List<SecKill> getSeckillList() {
        return seckillDao.queryAll(0, 10);
    }

    public SecKill getById(long seckillId) {
        SecKill seckill = redisDao.getSeckill(seckillId);
        if(seckill == null){
            seckill = seckillDao.queryById(seckillId);
            if (seckill != null) {
                redisDao.putSeckill(seckill);
            }
        }
        return seckill;
    }

    public Exposer exportSeckillUrl(long seckillId) {
        //使用redis缓存
        SecKill seckill = redisDao.getSeckill(seckillId);
        if(seckill == null) {
            seckill = seckillDao.queryById(seckillId);
            if (seckill == null) {
                return new Exposer(false, seckillId);
            }else{
                redisDao.putSeckill(seckill);
            }
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if(nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()){
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(long seckillId){
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillCloseException, RepeatKillException, SeckillException {
        if(md5 == null || !md5.equals(getMD5(seckillId))){
            throw new SeckillException("seckill data rewrite!");
        }
        try {
            int updateCount = seckillDao.reduceNumber(seckillId, new Date());
            if (updateCount <= 0) {
                throw new SeckillCloseException("seckill is closed!");
            } else {
                int insertCount = successKillDao.insertSuccessKilled(seckillId, userPhone, new Date());
                if (insertCount <= 0) {
                    throw new RepeatKillException("repeat kill!");
                } else {
                    SuccessKilled successKilled = successKillDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }
        }catch(SeckillCloseException e1) {
            throw e1;
        }catch(RepeatKillException e2) {
            throw e2;
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new SeckillException("seckill inner error: " + e.getMessage());
        }
    }
}
