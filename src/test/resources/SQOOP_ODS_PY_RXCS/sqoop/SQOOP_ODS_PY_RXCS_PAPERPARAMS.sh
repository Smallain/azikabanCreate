#!/bin/bash
export JAVA_HOME="/usr/local/jdk1.7.0"
#ODS_PY_RXCS_PAPERPARAMS
#import ods_py_rxcs_paperparams
sqoop import --connect jdbc:mysql://192.168.23.24:3414/py_rxcs --username m2k --password mysql2KAFKA --table paperparams --hive-import --hive-database odata --hive-table ods_py_rxcs_paperparams --hive-overwrite --hive-drop-import-delims -m 1
