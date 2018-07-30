package org.jzx.secondKill.service;

import org.jzx.secondKill.dto.Exposer;
import org.jzx.secondKill.dto.SeckillExecution;
import org.jzx.secondKill.entity.SecKill;
import org.jzx.secondKill.exception.RepeatKillException;
import org.jzx.secondKill.exception.SeckillCloseException;
import org.jzx.secondKill.exception.SeckillException;

import java.util.List;

public interface SeckillService {
    List<SecKill> getSeckillList();

    SecKill getById(long seckillId);

    Exposer exportSeckillUrl(long seckillId);

    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, SeckillCloseException, RepeatKillException;
}
