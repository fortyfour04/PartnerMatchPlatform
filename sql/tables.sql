create database huoban;
use huoban;

# 用户表创建
drop table if exists user;

create table user (
                      id          bigint auto_increment comment 'id'      primary key comment '主键id',
                      username    varchar(256)                            null comment '用户昵称',
                      userAccount varchar(256)                            null comment '用户账号',
                      avatarUrl   varchar(1024)                           null comment '用户头像',
                      profile     varchar(1024)                           null comment '个人简介',
                      gender      tinyint                                 null comment '性别',
                      userPassword varchar(512)                           not null comment '密码',
                      phone       varchar(128) default ''                 not null comment '电话号',
                      email       varchar(512)                            null comment '邮箱',
                      userStatus  int         default 0                   not null comment '状态0-正常',
                      createTime  datetime    default CURRENT_TIMESTAMP   null comment '创建时间',
                      updateTime  datetime    default CURRENT_TIMESTAMP   null on update CURRENT_TIMESTAMP comment '更新时间',
                      isDeleted   int         default 0                   not null comment '是否删除',
                      userRole    int         default 0                   not null comment '用户角色 0-普通用户 1-管理员',
                      tags        varchar(1024)                           null comment '标签json列表'
)   comment '用户';

INSERT INTO user (username, userAccount, avatarUrl, profile, gender, userPassword, phone, email, userStatus, createTime, updateTime, isDeleted, userRole, tags) VALUES
-- 用户 1
('Alice', 'alice123', 'https://example.com/avatars/alice.jpg', 'I love reading and painting.', 0, '', '1234567890', 'alice@example.com', 0, '2023-01-01 10:00:00', '2023-01-01 10:00:00', 0, 0, '["女", "大一", "前端"]'),
-- 用户 2
('Bob', 'bob456', 'https://example.com/avatars/bob.jpg', 'I am a sports enthusiast.', 1, '$2y$12$w2C4O8sR1jE6lB3hF5nD9kG7pT4aQ0uZ2vY1xW3mS5qN7rO8tU1eI2aD6fG4hJ9kL3pM5oI8', '0987654321', 'bob@example.com', 0, '2023-01-02 11:00:00', '2023-01-02 11:00:00', 0, 0, '["男", "大二", "后端"]'),
-- 用户 3
('Charlie', 'charlie789', 'https://example.com/avatars/charlie.jpg', 'I am a music lover.', 0, '$2y$12$w2C4O8sR1jE6lB3hF5nD9kG7pT4aQ0uZ2vY1xW3mS5qN7rO8tU1eI2aD6fG4hJ9kL3pM5oI8', '1122334455', 'charlie@example.com', 0, '2023-01-03 12:00:00', '2023-01-03 12:00', 0, 0, '["女", "大三", "算法"]'),
-- 用户 4
('David', 'david012', 'https://example.com/avatars/david.jpg', 'I enjoy traveling.', 1, '$2y$12$w2C4O8sR1jE6lB3hF5nD9kG7pT4aQ0uZ2vY1xW3mS5qN7rO8tU1eI2aD6fG4hJ9kL3pM5oI8', '2233445566', 'david@example.com', 0, '2023-01-04 13:00:00', '2023-01-04 13:00:00', 0, 0, '["男", "大四", "安卓"]'),
-- 用户 5
('Eve', 'eve345', 'https://example.com/avatars/eve.jpg', 'I am a foodie.', 0, '$2y$12$w2C4O8sR1jE6lB3hF5nD9kG7pT4aQ0uZ2vY1xW3mS5qN7rO8tU1eI2aD6fG4hJ9kL3pM5oI8', '3344556677', 'eve@example.com', 0, '2023-01-05 14:00:00', '2023-01-05 14:00:00', 0, 0, '["女", "研究生", "iOS"]'),
-- 用户 6
('Frank', 'frank678', 'https://example.com/avatars/frank.jpg', 'I am into fitness.', 1, '$2y$12$w2C4O8sR1jE6lB3hF5nD9kG7pT4aQ0uZ2vY1xW3mS5qN7rO8tU1eI2aD6fG4hJ9kL3pM5oI8', '4455667788', 'frank@example.com', 0, '2023-01-06 15:00:00', '2023-01-06 15:00:00', 0, 0, '["男", "大一", "产品经理"]'),
-- 用户 7
('Grace', 'grace901', 'https://example.com/avatars/grace.jpg', 'I like photography.', 0, '$2y$12$w2C4O8sR1jE6lB3hF5nD9kG7pT4aQ0uZ2vY1xW3mS5qN7rO8tU1eI2aD6fG4hJ9kL3pM5oI8', '5566778899', 'grace@example.com', 0, '2023-01-07 16:00:00', '2023-01-07 16:00:00', 0, 0, '["女", "大二", "前端"]'),
-- 用户 8
('Henry', 'henry234', 'https://example.com/avatars/henry.jpg', 'I am a movie buff.', 1, '$2y$12$w2C4O8sR1jE6lB3hF5nD9kG7pT4aQ0uZ2vY1xW3mS5qN7rO8tU1eI2aD6fG4hJ9kL3pM5oI8', '6677889900', 'henry@example.com', 0, '2023-01-08 17:00:00', '2023-01-08 17:00:00', 0, 0, '["男", "大三", "后端"]'),
-- 用户 9
('Ivy', 'ivy567', 'https://example.com/avatars/ivy.jpg', 'I am a gamer.', 0, '$2y$12$w2C4O8sR1jE6lB3hF5nD9kG7pT4aQ0uZ2vY1xW3mS5qN7rO8tU1eI2aD6fG4hJ9kL3pM5oI8', '7788990011', 'ivy@example.com', 0, '2023-01-09 18:00:00', '2023-01-09 18:00:00', 0, 0, '["女", "大四", "算法"]'),
-- 用户 10
('Jack', 'jack890', 'https://example.com/avatars/jack.jpg', 'I love coding.', 1, '$2y$12$w2C4O8sR1jE6lB3hF5nD9kG7pT4aQ0uZ2vY1xW3mS5qN7rO8tU1eI2aD6fG4hJ9kL3pM5oI8', '8899001122', 'jack@example.com', 0, '2023-01-10 19:00:00', '2023-01-10 19:00:00', 0, 0, '["男", "研究生", "安卓"]'),
-- 用户 11
('Kelly', 'kelly123', 'https://example.com/avatars/kelly.jpg', 'I am a writer.', 0, '$2y$12$w2C4O8sR1jE6lB3hF5nD9kG7pT4aQ0uZ2vY1xW3mS5qN7rO8tU1eI2aD6fG4hJ9kL3pM5oI8', '9900112233', 'kelly@example.com', 0, '2023-01-11 20:00:00', '2023-01-11 20:00:00', 0, 0, '["女", "大一", "iOS"]'),
-- 用户 12
('Leo', 'leo456', 'https://example.com/avatars/leo.jpg', 'I am an artist.', 1, '$2y$12$w2C4O8sR1jE6lB3hF5nD9kG7pT4aQ0uZ2vY1xW3mS5qN7rO8tU1eI2aD6fG4hJ9kL3pM5oI8', '0011223344', 'leo@example.com', 0, '2023-01-12 21:00:00', '2023-01-12 21:00:00', 0, 0, '["男", "大二", "产品经理"]'),
-- 用户 13
('Mia', 'mia789', 'https://example.com/avatars/mia.jpg', 'I am a dancer.', 0, '$2y$12$w2C4O8sR1jE6lB3hF5nD9kG7pT4aQ0uZ2vY1xW3mS5qN7rO8tU1eI2aD6fG4hJ9kL3pM5oI8', '1122334455', 'mia@example.com', 0, '2023-01-13 22:00:00', '2023-01-13 22:00:00', 0, 0, '["女", "大三", "前端"]'),
-- 用户 14
('Noah', 'noah012', 'https://example.com/avatars/noah.jpg', 'I am a singer.', 1, '$2y$12$w2C4O8sR1jE6lB3hF5nD9kG7pT4aQ0uZ2vY1xW3mS5qN7rO8tU1eI2aD6fG4hJ9kL3pM5oI8', '2233445566', 'noah@example.com', 0, '2023-01-14 23:00:00', '2023-01-14 23:00:00', 0, 0, '["男", "大四", "后端"]'),
-- 用户 15
('Olivia', 'olivia345', 'https://example.com/avatars/olivia.jpg', 'I am a gardener.', 0, '$2y$12$w2C4O8sR1jE6lB3hF5nD9kG7pT4aQ0uZ2vY1xW3mS5qN7rO8tU1eI2aD6fG4hJ9kL3pM5oI8', '3344556677', 'olivia@example.com', 0, '2023-01-15 08:00:00', '2023-01-15 08:00:00', 0, 0, '["女", "研究生", "算法"]'),
-- 用户 16
('Peter', 'peter678', 'https://example.com/avatars/peter.jpg', 'I am a cyclist.', 1, '$2y$12$w2C4O8sR1jE6lB3hF5nD9kG7pT4aQ0uZ2vY1xW3mS5qN7rO8tU1eI2aD6fG4hJ9kL3pM5oI8', '4455667788', 'peter@example.com', 0, '2023-01-16 09:00:00', '2023-01-16 09:00:00', 0, 0, '["男", "大一", "安卓"]'),
-- 用户 17
('Queenie', 'queenie901', 'https://example.com/avatars/queenie.jpg', 'I am a teacher.', 0, '$2y$12$w2C4O8sR1jE6lB3hF5nD9kG7pT4aQ0uZ2vY1xW3mS5qN7rO8tU1eI2aD6fG4hJ9kL3pM5oI8', '5566778899', 'queenie@example.com', 0, '2023-01-17 10:00:00', '2023-01-17 10:00:00', 0, 0, '["女", "大二", "iOS"]'),
-- 用户 18
('Ryan', 'ryan234', 'https://example.com/avatars/ryan.jpg', 'I am a student.', 1, '$2y$12$w2C4O8sR1jE6lB3hF5nD9kG7pT4aQ0uZ2vY1xW3mS5qN7rO8tU1eI2aD6fG4hJ9kL3pM5oI8', '6677889900', 'ryan@example.com', 0, '2023-01-18 11:00:00', '2023-01-18 11:00:00', 0, 0, '["男", "大三", "产品经理"]'),
-- 用户 19
('Sophia', 'sophia567', 'https://example.com/avatars/sophia.jpg', 'I am a chef.', 0, '$2y$12$w2C4O8sR1jE6lB3hF5nD9kG7pT4aQ0uZ2vY1xW3mS5qN7rO8tU1eI2aD6fG4hJ9kL3pM5oI8', '7788990011', 'sophia@example.com', 0, '2023-01-19 12:00:00', '2023-01-19 12:00:00', 0, 0, '["女", "大四", "前端"]'),
-- 用户 20
('Tom', 'tom890', 'https://example.com/avatars/tom.jpg', 'I am an engineer.', 1, '$2y$12$w2C4O8sR1jE6lB3hF5nD9kG7pT4aQ0uZ2vY1xW3mS5qN7rO8tU1eI2aD6fG4hJ9kL3pM5oI8', '8899001122', 'tom@example.com', 0, '2023-01-20 13:00:00', '2023-01-20 13:00:00', 0, 0, '["男", "研究生", "后端"]');

drop table if exists tags;

create table tag (
                     id          bigint auto_increment                   primary key comment '主键id',
                     tagName     varchar(256)                            null comment '用户昵称',
                     userId      bigint                                  null comment '用户id',
                     parentId    bigint                                  null comment '父标签id',
                     isParent    tinyint                                 null comment '是否为父标签 0-不是 1-是',
                     createTime  datetime    default CURRENT_TIMESTAMP   null comment '创建时间',
                     updateTime  datetime    default CURRENT_TIMESTAMP   null on update CURRENT_TIMESTAMP comment '更新时间',
                     isDeleted   int         default 0                   not null comment '是否删除'
)   comment '标签';

alter table user add column tags varchar(1024) null comment '标签列表';


drop table if exists team;

create table team (
                      id          bigint auto_increment                   primary key comment '队伍id',
                      userId      bigint                                  null comment '创建人id',
                      teamName    varchar(256)                            not null comment '队伍名称',
                      description varchar(1024)                           null comment '队伍介绍',
                      maxNum      int                                     null comment '队伍最大人数',
                      expireTime  datetime                                null comment '队伍过期时间',
                      status      int                                     not null comment '队伍状态(0-公开 1-私有 2-加密)',
                      password    varchar(256)                            null comment '队伍密码',
                      createTime  datetime    default CURRENT_TIMESTAMP   null comment '创建时间',
                      updateTime  datetime    default CURRENT_TIMESTAMP   null on update CURRENT_TIMESTAMP comment '更新时间',
                      isDeleted   int         default 0                   not null comment '是否删除'
)   comment '队伍';

drop table if exists team_user;

create table team_user (
                    id          bigint auto_increment                   primary key comment '主键id',
                    teamId      bigint                           null comment '队伍id',
                    userId      bigint                                  null comment '队员id',
                    joinTime    datetime                                null comment '加入队伍时间',
                    createTime  datetime    default CURRENT_TIMESTAMP   null comment '创建时间',
                    updateTime  datetime    default CURRENT_TIMESTAMP   null on update CURRENT_TIMESTAMP comment '更新时间',
                    isDeleted   int         default 0                   not null comment '是否删除'

) comment '队伍成员关系';