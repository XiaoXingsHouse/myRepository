CREATE DATABASE seckill;

use seckill;

CREATE TABLE seckill(
  `seckill_id` bigint NOT NULL AUTO_INCREMENT COMMENT '上屏库存id',
  `name` varchar(120) NOT NULL COMMENT '商品名称',
  `number` int NOT NULL COMMENT '库存数量',
  `start_time` timestamp NOT NULL COMMNET '秒杀开始时间',
  `end_time` timestamp NOT NULL COMMENT '秒杀结束时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMMENT '创建时间',
  PRIMARY KEY (seckill_id),
  key idx_start_time(start_time),
  key idx_end_time(end_time),
  key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表'

insert into
  seckill(name, number, start_time, end_time)
values
  ('1000元秒杀iphone6', 342, '2018-07-26 00:00:00', '2018-07-27 00:00:00'),
  ('500元秒杀ipad4', 23, '2018-07-26 00:00:00', '2018-07-27 00:00:00'),
  ('300元秒杀红米5', 235, '2018-07-26 00:00:00', '2018-07-27 00:00:00'),
  ('200元秒杀kindle', 135, '2018-07-26 00:00:00', '2018-07-27 00:00:00');


create table success_killed(
  `seckill_id` bigint NOT NULL COMMENT '秒杀商品id',
  `user_phone` bigint NOT NULL COMMENT '用户手机号',
  `state` tinyint NOT NULL DEFAULT -1 COMMENT '状态 -1：无效 0：成功 1：已付款',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  PRIMARY KEY (seckill_id, user_phone),
  key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表'