INSERT INTO `mu_user` (`id`, `login`, `password_hash`, `first_name`, `last_name`, `email`, `activated`, `lang_key`, `activation_key`, `reset_key`, `created_by`, `created_date`, `reset_date`, `last_modified_by`, `last_modified_date`) VALUES
(1, 'system', '$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG', 'System', 'System', 'system@localhost', '0', NULL, NULL, NULL, 'system', '2016-09-16 02:26:30', NULL, 'admin', '2017-08-31 13:12:12');

INSERT INTO `mu_order` (`id`, `price`, `status`, `ship_time`, `user_id`, `address`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `confirm_time`, `content`, `amount`, `message`) VALUES
(1, '111.00', 0, '2018-12-16 11:12:05', 1, 1, '', '2018-12-16 19:43:54', 'admin', '2018-12-16 19:44:05', NULL, '{"product":{"category":1,"createdBy":"","id":2,"intro":"string2","name":"string2","price":1.11,"status":1},"title":"1","url":"http://oiqvdjk3s.bkt.clouddn.com/kuNziglJ_test.txt","userId":5,"wid":24}', 0, NULL);

INSERT INTO `mu_order` (`id`, `price`, `status`, `ship_time`, `user_id`, `address`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `confirm_time`, `content`, `amount`, `message`) VALUES
(99, '111.00', 0, '2018-12-16 11:12:05', 1, 1, '', '2018-12-16 19:43:54', 'admin', '2018-12-16 19:44:05', NULL, '{"product":{"category":1,"createdBy":"","id":2,"intro":"string2","name":"string2","price":1.11,"status":1},"title":"1","url":"http://oiqvdjk3s.bkt.clouddn.com/kuNziglJ_test.txt","userId":5,"wid":24}', 0, NULL);


INSERT INTO `mu_product` (`id`, `category`, `name`, `intro`, `price`, `status`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `preview_pic`, `preview_video`) VALUES
(1, 0, 'string2', 'string2', '1.02', 0, '', '2018-12-17 21:00:07', 'admin', '2018-12-17 21:00:11', NULL, NULL);


INSERT INTO `mu_address` (`id`, `city_name`, `country_name`, `detail_info`, `national_code`, `postal_code`, `province_name`, `tel_number`, `user_id`, `user_name`) VALUES
(1, '中文', NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL);

INSERT INTO `mu_work_list` (`id`, `title`, `cover`, `content`, `url`, `user_id`, `status`, `pv`, `collect_num`, `created_by`, `created_date`, `reset_date`, `last_modified_by`, `last_modified_date`, `machine_num`, `category`) VALUES
(1, 'aaaaaaa', 'https://gw.alicdn.com/tps/TB1fcMYNVXXXXXqXVXXXXXXXXXX-750-750.png', 'string555', '//audio.musixise.com/Vwcz7dNc_output.mid', 12, 0, NULL, 1, '', '2019-04-13 04:58:33', NULL, 'admin', '2019-03-24 12:38:46', 35, 1);


INSERT INTO `mu_user_bind` (`bid`, `open_id`, `login`, `provider`, `access_token`, `expires_in`, `refresh_token`, `unionid`, `created_date`, `last_modified_by`, `last_modified_date`, `created_by`, `user_id`) VALUES
(4, 'oazyg56Zj4X6HViD9h0jB2BTOHJQ', 'wechat_ocAsI1L1M7L-tp7OFnFMqbUAItPs', 'wechat', 'I1SALi8f_Z17bsVOm68luthtsEnSuK0iSaTwA3vxRU1IrW63XnaS5hYmb1REU9frNAiRmBc7g9awKsL0dEQNPw', 0, '''''', '''''', '2016-12-24 10:14:07', 'admin', '2016-12-24 10:14:07', 'admin', 1);


