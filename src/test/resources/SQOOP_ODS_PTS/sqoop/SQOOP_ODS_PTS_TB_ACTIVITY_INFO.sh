#!/bin/bash
export JAVA_HOME="/usr/local/jdk1.7.0"
#ODS_PTS_TB_ACTIVITY_INFO
#import ods_pts_tb_activity_info
sqoop import --connect jdbc:mysql://192.168.23.24:4307/points --username sprogram --password turingavaTar --table tb_activity_info --hive-import --hive-database odata --hive-table ods_pts_tb_activity_info --hive-overwrite --hive-drop-import-delims -m 1
