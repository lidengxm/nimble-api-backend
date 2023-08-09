# 数据库初始化

-- 创建库
create database if not exists api;

-- 切换库
use api;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    unionId      varchar(256)                           null comment '微信开放平台id',
    mpOpenId     varchar(256)                           null comment '公众号openId',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    accessKey    varchar(512) not null comment 'accessKey',
    secretKey    varchar(512) not null comment 'secretKey',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_unionId (unionId)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 帖子表
create table if not exists post
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(512)                       null comment '标题',
    content    text                               null comment '内容',
    tags       varchar(1024)                      null comment '标签列表（json 数组）',
    thumbNum   int      default 0                 not null comment '点赞数',
    favourNum  int      default 0                 not null comment '收藏数',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '帖子' collate = utf8mb4_unicode_ci;

-- 帖子点赞表（硬删除）
create table if not exists post_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子点赞';

-- 帖子收藏表（硬删除）
create table if not exists post_favour
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子收藏';


-- 接口表
create table if not exists api.`interface_info`
(
    `id` bigint not null auto_increment comment 'id主键' primary key,
    `name` varchar(256) not null comment '名称',
    `description` varchar(256) null comment '描述',
    `url` varchar(512) not null comment '接口地址',
    `requestHeader` text null comment '请求头',
    `responseHeader` text null comment '响应头',
    `requestParams` text null comment '请求参数',
    `sdk` varchar(255) character set utf8mb4 collate utf8mb4_0900_ai_ci null default null comment '接口对应的SDK类路径',
    `status` int default 0 not null comment '接口状态（0-关闭，1-开启）',
    `method` varchar(256) not null comment '请求类型',
    `userId` bigint not null comment '创建人',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)'
) comment '接口信息' collate = utf8mb4_unicode_ci;

-- 插入模拟数据
insert into api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('袁越彬', '谢晓啸', 'www.omega-oberbrunner.net', '许鹏', '傅浩', '戴鸿煊', 8777);
insert into api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('许涛', '许哲瀚', 'www.robbin-bode.co', '何志泽', '孙懿轩', '董梓晨', 54498871);
insert into api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('方聪健', '史凯瑞', 'www.sherilyn-bayer.name', '夏耀杰', '郑聪健', '姚黎昕', 95785315);
insert into api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('丁思淼', '黎峻熙', 'www.isis-jaskolski.biz', '程弘文', '毛荣轩', '赖立果', 597);
insert into api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('贺明轩', '许擎宇', 'www.ammie-oconnell.name', '方明哲', '阎明', '廖雨泽', 3245);
insert into api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('杨瑞霖', '杨志泽', 'www.andra-kulas.info', '赵建辉', '郭鑫磊', '于擎苍', 716);
insert into api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('戴志强', '黎涛', 'www.claud-marvin.org', '白鑫鹏', '严思源', '王鹏涛', 8);
insert into api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('姜懿轩', '莫烨霖', 'www.mariano-russel.com', '杜靖琪', '蒋立轩', '熊智渊', 713272);
insert into api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('卢建辉', '夏哲瀚', 'www.amado-hansen.com', '曾哲瀚', '曹鹤轩', '吴峻熙', 1);
insert into api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('侯鸿煊', '贾文昊', 'www.glenn-koss.co', '林风华', '方鹭洋', '余博涛', 93798);
insert into api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('钱擎苍', '龚胤祥', 'www.wonda-kirlin.info', '顾昊焱', '曹修杰', '任伟诚', 5);
insert into api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('姜鹭洋', '冯明杰', 'www.willian-mcglynn.com', '洪烨磊', '石浩宇', '陆子涵', 506);
insert into api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('丁鑫磊', '杨鹏涛', 'www.eneida-doyle.com', '钟煜城', '白嘉懿', '贺绍齐', 1);
insert into api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('薛烨霖', '马浩轩', 'www.gaylene-oberbrunner.name', '蒋晟睿', '武靖琪', '袁鹭洋', 2165);
insert into api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('龙弘文', '潘耀杰', 'www.lanny-glover.info', '任思源', '熊明哲', '卢越彬', 1207);
insert into api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('武立诚', '于梓晨', 'www.elouise-olson.org', '郭鑫鹏', '龙昊强', '严烨伟', 2151);
insert into api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('马昊然', '范炎彬', 'www.zoraida-casper.org', '田浩轩', '陆靖琪', '魏苑博', 7);
insert into api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('贺钰轩', '汪伟诚', 'www.winford-kuhlman.com', '袁钰轩', '汪修杰', '戴弘文', 5823);
insert into api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('宋鹏飞', '马梓晨', 'www.rudolf-gleichner.co', '姚明', '吕伟宸', '钱哲瀚', 7206);
insert into api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('邓修杰', '彭浩轩', 'www.amal-strosin.org', '崔烨霖', '邓君浩', '胡嘉熙', 4369060891);

-- 用户调用接口关系表
create table if not exists api.`user_interface_info`
(
    `id` bigint not null auto_increment comment '主键' primary key,
    `userId` bigint not null comment '调用用户 id',
    `interfaceInfoId` bigint not null comment '接口 id',
    `totalNum` int default 0 not null comment '总调用次数',
    `leftNum` int default 0 not null comment '剩余调用次数',
    `status` int default 0 not null comment '0-正常，1-禁用',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)',
) comment '用户调用接口关系';