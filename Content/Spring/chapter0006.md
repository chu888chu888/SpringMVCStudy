#基于Spring MVC 的HelloWorld

## 1 环境准备

* IntelliJ IDEA 2016.1
* jdk1.8
* maven3.0.5
* tomcat或jetty

## 2 创建库表

```
MariaDB [(none)]> create database sampledb default character set utf8;
Query OK, 1 row affected (0.01 sec)
MariaDB [(none)]> use sampledb;
MariaDB [(none)]> create table t_user(
    -> user_id int auto_increment primary key,
    -> user_name varchar(30),
    -> password varchar(32),
    -> last_visit datetime,
    -> last_ip varchar(23)) engine=innodb;
MariaDB [sampledb]> create table t_login_log(
    -> login_log_id int auto_increment primary key,
    -> user_id int,
    -> ip varchar(23),
    -> login_datetime datetime) engine = innodb;
Query OK, 0 rows affected (0.01 sec)
MariaDB [sampledb]> insert into t_user(user_name,password) values('admin','123456');
Query OK, 1 row affected (0.00 sec)
```
## 创建工程

![](../../images/Spring/00001.png)
