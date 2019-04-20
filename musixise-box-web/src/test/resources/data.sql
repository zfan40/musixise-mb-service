INSERT INTO `mu_user` (`id`, `login`, `password_hash`, `first_name`, `last_name`, `email`, `activated`, `lang_key`, `activation_key`, `reset_key`, `created_by`, `created_date`, `reset_date`, `last_modified_by`, `last_modified_date`) VALUES
(1, 'system', '$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG', 'System', 'System', 'system@localhost', '0', NULL, NULL, NULL, 'system', '2016-09-16 02:26:30', NULL, 'admin', '2017-08-31 13:12:12');

INSERT INTO `mu_order` (`id`, `price`, `status`, `ship_time`, `user_id`, `address`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `confirm_time`, `content`, `amount`, `message`) VALUES
(1, '111.00', 0, '2018-12-16 11:12:05', 1, 1, '', '2018-12-16 19:43:54', 'admin', '2018-12-16 19:44:05', NULL, '{"product":{"category":1,"createdBy":"","id":2,"intro":"string2","name":"string2","price":1.11,"status":1},"title":"1","url":"http://oiqvdjk3s.bkt.clouddn.com/kuNziglJ_test.txt","userId":5,"wid":24}', 0, NULL);
