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
  `id` int(11) IDENTITY PRIMARY KEY,
  `price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '价格',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '订单状态 0:未付款, 1:支付中, 2:等待发货 3:等待收货 4:订单完成',
  `ship_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `address` int(11) NOT NULL DEFAULT '0' COMMENT '送货地址',
  `created_by` varchar(50) NOT NULL,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `product_type` int(11) NOT NULL DEFAULT '0',
  `confirm_time` datetime DEFAULT NULL,
  `content` text,
  `amount` bigint(20) NOT NULL,
  `message` varchar(255) DEFAULT NULL
) COMMENT='订单表';

CREATE TABLE `mu_product` (
  `id` bigint(20) IDENTITY PRIMARY KEY,
  `category` int(11) NOT NULL DEFAULT '0',
  `name` varchar(200) NOT NULL DEFAULT '' COMMENT '商品名称',
  `intro` text COMMENT '商品描述',
  `price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '商品价格',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态0=下架，1=上架',
  `created_by` varchar(50) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `preview_pic` varchar(255) DEFAULT NULL,
  `preview_video` varchar(255) DEFAULT NULL
) COMMENT='产品表';

CREATE TABLE `mu_address` (
  `id` bigint(20) NOT NULL,
  `city_name` varchar(255) DEFAULT NULL,
  `country_name` varchar(255) DEFAULT NULL,
  `detail_info` varchar(255) DEFAULT NULL,
  `national_code` varchar(255) DEFAULT NULL,
  `postal_code` varchar(255) DEFAULT NULL,
  `province_name` varchar(255) DEFAULT NULL,
  `tel_number` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  `user_name` varchar(255) DEFAULT NULL
) ;


CREATE TABLE `mu_work_list` (
  `id` bigint(20) IDENTITY PRIMARY KEY,
  `title` varchar(200)  NOT NULL,
  `cover` varchar(200)  DEFAULT '',
  `content` text  NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `status` tinyint(3) UNSIGNED DEFAULT '0' COMMENT '0=正常，1=私有，2=删除',
  `pv` int(11) DEFAULT '0',
  `collect_num` int(11) DEFAULT '0' COMMENT '收藏次数',
  `created_by` varchar(50) NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT null ,
  `reset_date` timestamp NULL DEFAULT NULL,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  `machine_num` int(11) DEFAULT '0',
  `category` int(11) DEFAULT NULL
) ;

CREATE TABLE `mu_user_bind` (
  `bid` int(11) NOT NULL,
  `open_id` varchar(128) NOT NULL,
  `login` varchar(100) NOT NULL,
  `provider` varchar(200) NOT NULL,
  `access_token` varchar(255) NOT NULL DEFAULT '''''',
  `expires_in` int(11) NOT NULL DEFAULT '0',
  `refresh_token` varchar(255) NOT NULL DEFAULT '''''',
  `unionid` varchar(100) NOT NULL DEFAULT '''''',
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `last_modified_by` varchar(50) NOT NULL,
  `last_modified_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `created_by` varchar(50) NOT NULL,
  `user_id` bigint(20) NOT NULL DEFAULT '0'
) COMMENT='用户绑定表';

CREATE TABLE `mu_purchase_lit` (
  `id` int(11) IDENTITY PRIMARY KEY,
  `created_by` varchar(50)  NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` varchar(50)  DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `order_id` bigint(20) NOT NULL,
  `pid` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `wid` bigint(20) NOT NULL
) ;