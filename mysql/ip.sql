DROP DATABASE IF EXISTS ip;
CREATE DATABASE ip;

DROP TABLE IF EXISTS ip.ip;
CREATE TABLE ip.ip(
  id  INT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID pk',
  min  VARCHAR(255) COMMENT '起始IP地址',
  max  VARCHAR(255) COMMENT '终止IP地址',
  geo  VARCHAR(255) COMMENT '地理位置'
);
SELECT *
FROM ip.ip;