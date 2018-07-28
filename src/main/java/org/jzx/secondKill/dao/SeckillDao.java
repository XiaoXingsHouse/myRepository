package org.jzx.secondKill.dao;

import org.apache.ibatis.annotations.Param;
import org.jzx.secondKill.entity.SecKill;

import java.util.Date;
import java.util.List;

public interface SeckillDao {
    //秒杀操作之一——减库存
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    //根据ID查找商品
    SecKill queryById(long seckillId);

    //列出所有商品
    List<SecKill> queryAll(@Param("offset") int offset, @Param("limit") int limit);
}
