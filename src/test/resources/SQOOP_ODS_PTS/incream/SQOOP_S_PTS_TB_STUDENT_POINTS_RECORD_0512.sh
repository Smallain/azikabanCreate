#!/bin/bash
export JAVA_HOME="/usr/local/jdk1.7.0"
#S_PTS_TB_STUDENT_POINTS_RECORD
#import s_pts_tb_student_points_record
s_date=$(date -d '-7 days' +%Y-%m-%d)
if [ $# -eq 1 ]; then
	s_date=$1
fi
sqoop import --connect jdbc:mysql://192.168.23.24:4307/points --username sprogram --password turingavaTar --hive-import --hive-database sdata --hive-table s_pts_tb_student_points_record --target-dir s_pts_tb_student_points_record  --query "select * from tb_student_points_record_0512 where substr(spr_create_date,1,10)>='${s_date}' and \$CONDITIONS" -m 1
