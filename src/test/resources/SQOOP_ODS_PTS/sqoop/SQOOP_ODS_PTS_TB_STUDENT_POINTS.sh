#!/bin/bash
export JAVA_HOME="/usr/local/jdk1.7.0"
#ODS_PTS_TB_STUDENT_POINTS
#import ods_pts_tb_student_points
sqoop import --connect jdbc:mysql://192.168.23.24:4307/points --username sprogram --password turingavaTar --table tb_student_points --hive-import --hive-database odata --hive-table ods_pts_tb_student_points --hive-overwrite --hive-drop-import-delims -m 1
