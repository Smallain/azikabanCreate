#!/bin/bash
hadoop fs -touchz /tmp/azkaban_event/SQOOP_ODS_COR_$(date+"%Y%m%d").event
