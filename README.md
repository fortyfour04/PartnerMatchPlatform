# 编程队友匹配平台
编程队友匹配平台的项目后端代码仓库。
该项目主要面向大学生群体，旨在搭建一个方便快捷的竞赛、编程、就业、考研考公等不同圈子的队友匹配平台，帮助用户找到各自兴趣相投的同学与队友。采用智能标签推荐算法，为用户提供更精准的队友推荐
### 相关技术栈：
- SpringBoot+MyBatisPlus搭建基础项目框架
- MySql数据库存储数据
- Redis实现缓存数据库查询优化（10w+条数据平均查询时间<150ms）
- Redisson实现缓存预热与分布式锁
- 根据用户标签进行匹配，相关度最高的进行优先推荐
### 其他
接口采用swagger-knife4j进行编写与测试。该仓库为后端代码，适合有一定开发基础、想要进一步扩展技术的JAVA开发同学参与学习