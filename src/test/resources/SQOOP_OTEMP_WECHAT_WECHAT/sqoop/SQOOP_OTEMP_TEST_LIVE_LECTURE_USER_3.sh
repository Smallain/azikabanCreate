#!/bin/bash
export JAVA_HOME="/usr/local/jdk1.7.0"
#OTEMP_TEST_LIVE_LECTURE_USER_3
#import otemp_test_live_lecture_user_3
sqoop import --connect jdbc:mysql://192.168.181.245:3306/wechat_wechat --username wechat_read --password Tlasa*@121op --table live_lecture_user_3 --hive-import --hive-database otemp --hive-table otemp_test_live_lecture_user_3 --hive-overwrite --hive-drop-import-delims -m 1
