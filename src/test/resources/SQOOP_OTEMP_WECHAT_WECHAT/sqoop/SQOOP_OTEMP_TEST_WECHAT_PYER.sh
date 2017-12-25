#!/bin/bash
export JAVA_HOME="/usr/local/jdk1.7.0"
#OTEMP_TEST_WECHAT_PYER
#import otemp_test_wechat_pyer
sqoop import --connect jdbc:mysql://192.168.181.245:3306/wechat_wechat --username wechat_read --password Tlasa*@121op --table wechat_pyer --hive-import --hive-database otemp --hive-table otemp_test_wechat_pyer --hive-overwrite --hive-drop-import-delims -m 1
