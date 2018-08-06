package com.tal.dagazk

import com.tal.dagazk.toolbox.HqlFile
import com.tal.dagazk.toolbox.JobFile
import com.tal.dagazk.toolbox.ShellFile
import com.tal.dagazk.toolbox.WriteToFile

object CreateAzkaban extends LoadAdjacencyList with WriteToFile {
	
	def main(args: Array[String]): Unit = {
		//		if (args.length < 1) {
		//			println("请输入参数")
		//
		//		} else {
		
		//			val project_name = args(0)
		//
		//			val source_path = args(1)
		//
		//			val source_file_config = args(2)
		//
		//			val source_file_db = args(3)
		val project_name = "SQOOP_ODS_TAPP"
		val source_path = "/Users/smallain/IdeaProjects/Scala_Training/Tal_Azkaban_Factory/src/test/resources"
		val source_file_config = "SQOOP_ODS_TAPP.txt"
		val source_file_db = "SQOOP_ODS_TAPP_DB.txt"
		
		
		val mp = DAG.analysisFile(source_path + "/" + source_file_config)
		val db_info = DAG.analysisFile(source_path + "/" + source_file_db).filter(k => k._2 != Nil)
		val listSort = DAG.topology(mp).filter(_ != "")
		
		
		/**
			* 生成Azkaban调度程序文件
			*/
		
		var command_lines = ""
		for (i <- listSort) {
			val deps = mp(i).mkString(",")
			JobFile.jobFileMaker(source_path + "/" + project_name + "/azkaban", i, deps)
			//var tmp_s = s"输出队列是：$i								依赖是：$deps\n"
			//command_lines += tmp_s
			//withWriter("./src/test/resources/")("dag_squeue.txt")(command_lines)
		}
		
		/**
			* 生成shell脚本调度程序文件
			*/
		
		for (i <- listSort) {
			ShellFile.shellFileMaker(source_path + "/" + project_name + "/sqoop", i, db_info,project_name)
		}
		
		/**
			* 生成hql脚本调度程序文件
			*/
		
		for (i <- listSort) {
			HqlFile.hqlFileMaker(source_path + "/" + project_name + "/etl", i)
		}
		//}
	}
}