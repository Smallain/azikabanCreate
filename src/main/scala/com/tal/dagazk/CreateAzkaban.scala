package com.tal.dagazk

import com.tal.dagazk.toolbox.HqlFile
import com.tal.dagazk.toolbox.JobFile
import com.tal.dagazk.toolbox.ShellFile
import com.tal.dagazk.toolbox.WriteToFile

object CreateAzkaban extends App with LoadAdjacencyList with WriteToFile {
	
	
	
	val mp = DAG.analysisFile("./src/test/resources/SQOOP_OTEMP_WECHAT_WECHAT.txt")
	val db_info = DAG.analysisFile("./src/test/resources/SQOOP_OTEMP_WECHAT_WECHAT_DB.txt").filter(k => k._2 != Nil)
	val listSort = DAG.topology(mp).filter(_ != "")
	
	
	/**
		* 生成Azkaban调度程序文件
		*/
	
	var command_lines = ""
	for (i <- listSort) {
		val deps = mp(i).mkString(",")
		JobFile.jobFileMaker("./src/test/resources/SQOOP_OTEMP_WECHAT_WECHAT/azkaban", i, deps)
		var tmp_s = s"输出队列是：$i								依赖是：$deps\n"
		command_lines += tmp_s
		withWriter("./src/test/resources/")("dag_squeue.txt")(command_lines)
	}
	
	/**
		* 生成shell脚本调度程序文件
		*/
	
	for (i <- listSort) {
		ShellFile.shellFileMaker("./src/test/resources/SQOOP_OTEMP_WECHAT_WECHAT/sqoop", i, db_info)
	}
	
	/**
		* 生成hql脚本调度程序文件
		*/
	
	for (i <- listSort) {
		HqlFile.hqlFileMaker("./src/test/resources/SQOOP_OTEMP_WECHAT_WECHAT/etl", i)
	}
}
