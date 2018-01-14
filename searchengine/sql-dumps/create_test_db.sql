CREATE DATABASE `test`;
CREATE USER 'test-user'@'%'
  IDENTIFIED BY 'test';
GRANT ALL PRIVILEGES ON test.* TO 'test-user'@'%';