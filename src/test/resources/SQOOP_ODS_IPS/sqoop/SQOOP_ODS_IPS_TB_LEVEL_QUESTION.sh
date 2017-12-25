#!/bin/bash
export JAVA_HOME="/usr/local/jdk1.7.0"
#ODS_IPS_TB_STU_ANSWER_EXAM
#import ods_ips_tb_stu_answer_exam
sqoop import --connect jdbc:mysql://192.168.23.24:4307/points --username sprogram --password turingavaTar --table tb_stu_answer_exam --hive-import --hive-database odata --hive-table ods_ips_tb_stu_answer_exam --hive-drop-import-delims -m 1
