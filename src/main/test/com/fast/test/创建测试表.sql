create table user
(
    id          bigint auto_increment comment '主键' primary key,
    type_id     bigint null comment '用户类型',
    user_name   varchar(255) null comment '用户名',
    age         int(10) null comment '年龄',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '更新时间',
    deleted     bit null comment '是否删除'
) comment '用户';


create table user_log
(
    id          bigint auto_increment comment '主键' primary key,
    user_id     bigint null comment '用户ID',
    log_info    varchar(255) null comment '日志内容',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '更新时间',
    deleted     bit null comment '是否删除'
) comment '用户日志';

create table user_type
(
    id          bigint auto_increment comment '主键' primary key,
    type_name   varchar(255) null comment '用户名',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '更新时间',
    deleted     bit null comment '是否删除'
) comment '用户类型';

create table user_type_info
(
    id           bigint auto_increment comment '主键' primary key,
    user_type_id bigint null comment '用户类型ID',
    type_info    varchar(255) null comment '类型信息',
    create_time  datetime null comment '创建时间',
    update_time  datetime null comment '更新时间',
    deleted      bit null comment '是否删除'
) comment '用户类型信息';

INSERT INTO user (`id`, `type_id`, `user_name`, `age`, `create_time`, `update_time`, `deleted`)
VALUES (1, 1, 'User1', 1, '2021-09-14 16:46:11', '2021-09-14 16:46:11', false),
       (2, 1, 'User2', 2, '2021-09-14 16:46:11', '2021-09-14 16:46:11', false),
       (3, 2, 'User3', 3, '2021-09-14 16:46:11', '2021-09-14 16:46:11', false);

INSERT INTO user_type (`id`, `type_name`, `create_time`, `update_time`, `deleted`)
VALUES (1, 'Type1', '2021-09-14 16:46:11', '2021-09-14 16:46:11', false),
       (2, 'Type2', '2021-09-14 16:46:11', '2021-09-14 16:46:11', false);

INSERT INTO user_type_info (`id`, `user_type_id`, `type_info`, `create_time`, `update_time`, `deleted`)
VALUES (1, 1, 'TypeInfo_1', '2021-10-10 18:15:10', '2021-10-10 18:15:13', 0);

INSERT INTO user_log (`id`, `user_id`, `log_info`, `create_time`, `update_time`, `deleted`)
VALUES (1, 1, 'Log1', '2021-09-14 16:46:11', '2021-09-14 16:46:11', false),
       (2, 1, 'Log2', '2021-09-14 16:46:11', '2021-09-14 16:46:11', false);