#!/bin/bash
hadoop fs -touchz /tmp/azkaban_event/SQOOP_ODS_PY_RXCS_$(date+"%Y%m%d").event
