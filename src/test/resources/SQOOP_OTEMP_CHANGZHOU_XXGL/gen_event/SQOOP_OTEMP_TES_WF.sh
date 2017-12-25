#!/bin/bash
hadoop fs -touchz /tmp/azkaban_event/SQOOP_OTEMP_TES_$(date+"%Y%m%d").event
