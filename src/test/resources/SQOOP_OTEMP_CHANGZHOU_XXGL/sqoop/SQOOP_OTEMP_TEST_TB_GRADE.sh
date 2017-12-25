#!/bin/bash
export JAVA_HOME="/usr/local/jdk1.7.0"
#OTEMP_TEST_TB_GRADE
#import otemp_test_tb_grade
sqoop import --connect jdbc:mysql://192.168.1.107:3306/changzhou_xxgl --username m2k --password mysql2KAFKA --table tb_grade --hive-import --hive-database otemp --hive-table otemp_test_tb_grade --hive-overwrite --hive-drop-import-delims -m 1
