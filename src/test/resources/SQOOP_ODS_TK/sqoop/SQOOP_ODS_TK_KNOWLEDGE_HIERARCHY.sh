#!/bin/bash
export JAVA_HOME="/usr/local/jdk1.7.0"
#ODS_TK_KNOWLEDGE_HIERARCHY
#import ods_tk_knowledge_hierarchy
sqoop import --connect jdbc:mysql://47.94.37.65:3306/ips_tiku --username ips_tiku --password 'D\\#~zwd9RQ' --table knowledge_hierarchy --hive-import --hive-database odata --hive-table ods_tk_knowledge_hierarchy --hive-overwrite --hive-drop-import-delims -m 1
