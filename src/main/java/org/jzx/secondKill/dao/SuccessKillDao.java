package org.jzx.secondKill.dao;

import org.apache.ibatis.annotations.Param;
import org.jzx.secondKill.entity.SuccessKilled;

import java.util.Date;

public interface SuccessKillDao {

    //秒杀成功后记录商品、顾客信息
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone, @Param("createTime")Date date);

    //根据商品ID、手机号获得秒杀信息
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);
}
