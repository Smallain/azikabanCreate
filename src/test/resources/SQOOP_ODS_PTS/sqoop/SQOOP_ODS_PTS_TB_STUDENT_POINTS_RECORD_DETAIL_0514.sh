#!/bin/bash
export JAVA_HOME="/usr/local/jdk1.7.0"
#ODS_PTS_TB_STUDENT_POINTS_RECORD_DETAIL
#import ods_pts_tb_student_points_record_detail
sqoop import --connect jdbc:mysql://192.168.23.24:4307/points --username sprogram --password turingavaTar --table tb_student_points_record_detail_0514 --hive-import --hive-database odata --hive-table ods_pts_tb_student_points_record_detail  --hive-drop-import-delims -m 1
