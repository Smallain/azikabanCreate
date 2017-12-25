#!/bin/bash
export JAVA_HOME="/usr/local/jdk1.7.0"
#ODS_TK_TK_QUESTION_ZZ
#import ods_tk_tk_question_zz
sqoop import --connect jdbc:mysql://47.94.37.65:3306/ips_tiku --username ips_tiku --password 'D\\#~zwd9RQ' --table tk_question_zz --hive-import --hive-database odata --hive-table ods_tk_tk_question_zz --hive-overwrite --hive-drop-import-delims -m 1
