#!/bin/bash
export JAVA_HOME="/usr/local/jdk1.7.0"
#ODS_COR_XES_TUTOR_CLASS
#import ods_cor_xes_tutor_class
sqoop import --connect jdbc:mysql://rm-2ze86x0cxybkm4b4irw.mysql.rds.aliyuncs.com:3306/ipstutor --username turing_rep --password drw_5lv_do --table xes_tutor_class --hive-import --hive-database odata --hive-table ods_cor_xes_tutor_class --hive-overwrite --hive-drop-import-delims -m 1
