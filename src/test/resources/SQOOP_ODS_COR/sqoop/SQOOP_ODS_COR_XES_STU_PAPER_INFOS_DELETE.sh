#!/bin/bash
export JAVA_HOME="/usr/local/jdk1.7.0"
#ODS_COR_XES_STU_PAPER_INFOS_DELETE
#import ods_cor_xes_stu_paper_infos_delete
sqoop import --connect jdbc:mysql://rm-2ze86x0cxybkm4b4irw.mysql.rds.aliyuncs.com:3306/ipstutor --username turing_rep --password drw_5lv_do --table xes_stu_paper_infos_delete --hive-import --hive-database odata --hive-table ods_cor_xes_stu_paper_infos_delete --hive-overwrite --hive-drop-import-delims -m 1
