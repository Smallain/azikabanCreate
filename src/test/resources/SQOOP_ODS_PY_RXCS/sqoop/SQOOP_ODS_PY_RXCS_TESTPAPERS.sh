#!/bin/bash
export JAVA_HOME="/usr/local/jdk1.7.0"
#ODS_PY_RXCS_TESTPAPERS
#import ods_py_rxcs_testpapers
sqoop import --connect jdbc:mysql://192.168.23.24:3414/py_rxcs --username m2k --password mysql2KAFKA --table testpapers --hive-import --hive-database odata --hive-table ods_py_rxcs_testpapers --hive-overwrite --hive-drop-import-delims -m 1
