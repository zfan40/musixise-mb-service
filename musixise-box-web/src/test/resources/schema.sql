CREATE TABLE if not exists `mu_user` (
  `id` bigint(20) NOT NULL,
  `login` varchar(100) NOT NULL,
  `password_hash` varchar(60) DEFAULT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `activated` bit(1) NOT NULL,
  `lang_key` varchar(5) DEFAULT NULL,
  `activation_key` varchar(20) DEFAULT NULL,
  `reset_key` varchar(20) DEFAULT NULL,
  `created_by` varchar(50) NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT NULL ,
  `reset_date` timestamp NULL DEFAULT NULL,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL
);

CREATE TABLE `mu_order` (
  `id` int(11) NOT NULL,
  `price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '价格',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '订单状态 0:未付款, 1:支付中, 2:等待发货 3:等待收货 4:订单完成',
  `ship_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `address` int(11) NOT NULL DEFAULT '0' COMMENT '送货地址',
  `created_by` varchar(50) NOT NULL,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `confirm_time` datetime DEFAULT NULL,
  `content` text,
  `amount` bigint(20) NOT NULL,
  `message` varchar(255) DEFAULT NULL
) COMMENT='订单表';