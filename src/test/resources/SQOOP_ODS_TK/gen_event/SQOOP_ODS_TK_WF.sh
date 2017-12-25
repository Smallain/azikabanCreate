#!/bin/bash
hadoop fs -touchz /tmp/azkaban_event/SQOOP_ODS_TK_$(date+"%Y%m%d").event
