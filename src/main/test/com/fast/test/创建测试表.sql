create table user
(
    id           bigint auto_increment comment '主键' primary key,
    user_type_id bigint       null comment '用户类型',
    user_name    varchar(255) null comment '用户名',
    age          int(10)      null comment '年龄',
    create_time  datetime     null comment '创建时间',
    update_time  datetime     null comment '更新时间',
    deleted      bit          null comment '是否删除'
) comment '用户';


create table user_log
(
    id          bigint auto_increment comment '主键' primary key,
    user_id     bigint       null comment '用户ID',
    log_info    varchar(255) null comment '日志内容',
    create_time datetime     null comment '创建时间',
    update_time datetime     null comment '更新时间',
    deleted     bit          null comment '是否删除'
) comment '用户日志';

create table user_type
(
    id          bigint auto_increment comment '主键' primary key,
    type_name   varchar(255) null comment '用户名',
    create_time datetime     null comment '创建时间',
    update_time datetime     null comment '更新时间',
    deleted     bit          null comment '是否删除'
)  comment '用户类型';

INSERT INTO user
(`id`,`user_type_id`,`user_name`,`age`,`create_time`,`update_time`,`deleted`) VALUES
(1 , 1 , '用户1' , 1 , '2021-09-14 16:46:11' , '2021-09-14 16:46:11' , false ),
(2 , 1 , '用户2' , 2 , '2021-09-14 16:46:11' , '2021-09-14 16:46:11' , false ),
(3 , 2 , '用户3' , 3 , '2021-09-14 16:46:11' , '2021-09-14 16:46:11' , false );

INSERT INTO user_type
(`id`,`type_name`,`create_time`,`update_time`,`deleted`) VALUES
(1 , '类型1' , '2021-09-14 16:46:11' , '2021-09-14 16:46:11' , false ),
(2 , '类型2' , '2021-09-14 16:46:11' , '2021-09-14 16:46:11' , false );

INSERT INTO user_log
(`id`,`user_id`,`log_info`,`create_time`,`update_time`,`deleted`) VALUES
(1 , 1 , '日志1' , '2021-09-14 16:46:11' , '2021-09-14 16:46:11' , false ),
(2 , 1 , '日志2' , '2021-09-14 16:46:11' , '2021-09-14 16:46:11' , false );