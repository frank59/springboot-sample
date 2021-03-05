

-- ----------------------------
--  Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS t_user;
CREATE TABLE `t_user`
(
    `id`          int(11)      NOT NULL AUTO_INCREMENT,
    `create_time` timestamp    NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp    NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `user_name`   varchar(100) NOT NULL,
    `gender`      int(11)           DEFAULT NULL,
    `age`         int(11)           DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `user_name_UNIQUE` (`user_name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 7
  DEFAULT CHARSET = utf8mb4;
