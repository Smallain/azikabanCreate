#!/bin/bash
export JAVA_HOME="/usr/local/jdk1.7.0"
#OTEMP_TEST_TB_FINANCE
#import otemp_test_tb_finance
sqoop import --connect jdbc:mysql://192.168.1.107:3306/changzhou_xxgl --username m2k --password mysql2KAFKA --table tb_finance --hive-import --hive-database otemp --hive-table otemp_test_tb_finance --hive-overwrite --hive-drop-import-delims -m 1
