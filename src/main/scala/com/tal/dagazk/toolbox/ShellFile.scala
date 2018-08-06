package com.tal.dagazk.toolbox

import java.text.SimpleDateFormat
import java.util.Date

import com.tal.dagazk.Util.Path

object ShellFile extends WriteToFile {
	/**
		* shell脚本选择器
		* 根据文件名称筛选shell脚本执行其中包含，全量脚本，增量脚本，生成事件脚本
		*
		* @param path     文件路径
		* @param fileName 文件名称
		* @param db_info  数据库链接信息
		*/
	
	def shellFileMaker(path: Path, fileName: String, db_info: Map[String, List[String]],projectName:String) {
		
		//解析数据库配置信息
		
		//数据源信息
		val url_source = db_info("url_source").mkString
		val port_source = db_info("port_source").mkString
		val user_source = db_info("user_source").mkString
		var password_source = db_info("password_source").mkString
		
		withWriter(path.dropRight(6)+"/password")("password.file")(password_source)
		
		
		password_source = s"--password-file hdfs://turinghdfs:8020/user/hdfs/password/$projectName/password.file"
		
		val database_source = db_info("database_source").mkString
		val table_source = db_info("table_source")
		
		//目标信息（全量）
		val database_target = db_info("database_target").mkString
		val table_target = db_info("table_target").mkString
		
		//目标信息（增量）
		val database_target_cream = db_info("database_target_cream").mkString
		val table_target_cream = db_info("table_target_cream").mkString
		
		//根据文件名信息筛选要指定执行的脚本
		if (fileName.toUpperCase.contains("_WF")) {
			//执行事件生成脚本
			eventFile(path, fileName)
		} else if (fileName.toUpperCase.contains("SQOOP_")) {
			//执行数据加载脚本
			if (fileName.toUpperCase.contains("_S_")) {
				//执行增量加载脚本
				for (table_src <- table_source) {
					
					//以下用正则表达式区分一张表的名称包含在另一张表中的情况，例如 t_area t_area_101，如果用t_area去匹配，会匹配出两张表
					import scala.util.matching.Regex
					val tab = "_" + table_src + "$"
					val pattern = new Regex(tab)
					
					val fntc = fileName.toLowerCase
					
					val matched = (pattern findAllIn fntc).mkString
					
					if (matched.isEmpty) {
					} else {
						creamFile(path, fileName, url_source, port_source, user_source
							, password_source, database_source, table_src, database_target_cream, table_target_cream)
					}
				}
			} else {
				//执行全量加载脚本
				for (table_src <- table_source) {
					//以下用正则表达式区分一张表的名称包含在另一张表中的情况，例如 t_area t_area_101，如果用t_area去匹配，会匹配出两张表
					import scala.util.matching.Regex
					val tab = "_" + table_src + "$"
					val pattern = new Regex(tab)
					
					val fntc = fileName.toLowerCase
					
					val matched = (pattern findAllIn fntc).mkString
					
					if (matched.isEmpty) {
					} else {
						amountFile(path, fileName, url_source, port_source, user_source
							, password_source, database_source, table_src, database_target, table_target)
					}
				}
			}
		}
	}
	
	/**
		* 加载全量数据脚本
		*
		* @param path            文件路径
		* @param fileName        文件名称
		* @param url_source      数据源链接urk
		* @param port_source     数据源端口
		* @param user_source     数据源用户名称
		* @param password_source 数据源密码信息
		* @param database_source 数据源数据库
		* @param table_src       数据源表
		* @param database_target 目标数据库
		* @param table_target    目标数据表
		*/
	def amountFile(path: Path, fileName: String, url_source: String, port_source: String, user_source: String, password_source: String
								 , database_source: String, table_src: String, database_target: String, table_target: String) {
		
		import scala.util.matching.Regex
		
		//val pattens = "tb_student_points_record_[0-9]"
		
		val tab_re = "student_honor_[0-9]"
		val tab_red = "tb_student_points_record_detail_[0-9]"
		val pattern_re = new Regex(tab_re)
		val pattern_red = new Regex(tab_red)
		
		
		//匹配城市id
		val city_re = "[0-9]+"
		val pattern_city = new Regex(city_re)
		
		
		val fntc = fileName.toLowerCase
		
		val match_re = (pattern_re findAllIn fntc).mkString.dropRight(2)
		val match_red = (pattern_red findAllIn fntc).mkString.dropRight(2)
		val match_city = (pattern_city findAllIn fntc).mkString
		
		var partition_para = ""
		if (match_city.isEmpty) {
			partition_para = ""
		} else {
			partition_para = s"--hive-partition-key city_id --hive-partition-value \'$match_city\' "
		}
		
		if (match_re.isEmpty) {
			if (match_red.isEmpty) {
				//如果不是像tb_student_points_record_010这样需要合并各个城市的表的话，采取正常的抽取逻辑
				val command_lines: String =
					s"""#!/bin/bash
						 |export JAVA_HOME="/usr/local/jdk1.7.0"
						 |#${table_target.toUpperCase + table_src.toUpperCase}
						 |#import ${table_target + table_src}
						 |hdfs dfs -test -e /user/hdfs/$table_src
						 |if [ $$? -eq 0 ] ;then
						 |	echo 'Directory is already exist.Delete Directory'
						 | 	hdfs dfs -rm -r /user/hdfs/$table_src
						 |else
						 |	echo 'Directory is not exist.Can be started.'
						 |fi
						 |sqoop import --connect $url_source --username $user_source  $password_source --table $table_src --hive-import --hive-database $database_target --hive-table ${table_target + table_src} --hive-overwrite --hive-drop-import-delims -m 1
						 |""".stripMargin
				withWriter(path)(fileName + ".sh")(command_lines)
			} else {
				//如果像tb_student_points_record_detail_010这样的表的话，将目标表 同为合并为表tb_student_points_record_detail
				var overwrite_para = "--hive-overwrite"
				if (match_city.equals("010")) {
					overwrite_para = "--hive-overwrite"
				}
				val command_lines: String =
					s"""#!/bin/bash
						 |export JAVA_HOME="/usr/local/jdk1.7.0"
						 |#${table_target.toUpperCase + match_red.toUpperCase}
						 |#import ${table_target + match_red}
						 |hdfs dfs -test -e /user/hdfs/$table_src
						 |if [ $$? -eq 0 ] ;then
						 |	echo 'Directory is already exist.Delete Directory'
						 | 	hdfs dfs -rm -r /user/hdfs/$table_src
						 |else
						 |	echo 'Directory is not exist.Can be started.'
						 |fi
						 |sqoop import --connect $url_source --username $user_source  $password_source --table $table_src --hive-import --hive-database $database_target --hive-table ${table_target + match_red} $overwrite_para --hive-drop-import-delims -m 1
						 |""".stripMargin
				
				withWriter(path)(fileName + ".sh")(command_lines)
			}
		} else {
			//如果像tb_student_points_record_010这样的表的话，将目标表 同为合并为表tb_student_points_record
			var overwrite_para = "--hive-overwrite"
			if (match_city.equals("010")) {
				overwrite_para = "--hive-overwrite"
			}
			val command_lines: String =
				s"""#!/bin/bash
					 |export JAVA_HOME="/usr/local/jdk1.7.0"
					 |#${table_target.toUpperCase + match_re.toUpperCase}
					 |#import ${table_target + match_re}
					 |hdfs dfs -test -e /user/hdfs/$table_src
					 |if [ $$? -eq 0 ] ;then
					 |	echo 'Directory is already exist.Delete Directory'
					 | 	hdfs dfs -rm -r /user/hdfs/$table_src
					 |else
					 |	echo 'Directory is not exist.Can be started.'
					 |fi
					 |sqoop import --connect $url_source --username $user_source  $password_source --table $table_src --hive-import --hive-database $database_target --hive-table ${table_target + match_re} $overwrite_para $partition_para --hive-drop-import-delims -m 1
					 |""".stripMargin
			
			withWriter(path)(fileName + ".sh")(command_lines)
		}
		
	}
	
	/**
		* 增量数据加载脚本
		*
		* @param path            文件路径
		* @param fileName        文件名称
		* @param url_source      数据源url链接
		* @param port_source     数据源端口
		* @param user_source     数据源用户
		* @param password_source 数据源密码
		* @param database_source 数据源数据库
		* @param table_src       数据源表
		* @param database_target 目标数据库
		* @param table_target    目标数据表
		*/
	def creamFile(path: Path, fileName: String, url_source: String, port_source: String, user_source: String, password_source: String
								, database_source: String, table_src: String, database_target: String, table_target: String) {
		
		import scala.util.matching.Regex
		
		//val pattens = "tb_student_points_record_[0-9]"
		
		val tab_re = "plan_stu_score_[0-9]"
		val tab_red = "tb_student_points_record_detail_[0-9]"
		val pattern_re = new Regex(tab_re)
		val pattern_red = new Regex(tab_red)
		
		//匹配城市
		val city_re = "[0-9]+"
		val pattern_city = new Regex(city_re)
		
		
		val fntc = fileName.toLowerCase
		
		
		val match_re = (pattern_re findAllIn fntc).mkString.dropRight(2)
		val match_red = (pattern_red findAllIn fntc).mkString.dropRight(2)
		val match_city = (pattern_city findAllIn fntc).mkString
		
		var partition_para = ""
		if (match_city.isEmpty) {
			partition_para = ""
		} else {
			partition_para = s"--hive-partition-key city_id --hive-partition-value \'$match_city\' "
		}
		
		
		if (match_re.isEmpty) {
			if (match_red.isEmpty) {
				val command_lines: String =
					s"""#!/bin/bash
						 |export JAVA_HOME="/usr/local/jdk1.7.0"
						 |#${table_target.toUpperCase + table_src.toUpperCase}
						 |#import ${table_target + table_src}
						 |hdfs dfs -test -e /user/hdfs/${table_target + table_src}
						 |if [ $$? -eq 0 ] ;then
						 |	echo 'Directory is already exist.Delete Directory'
						 | 	hdfs dfs -rm -r /user/hdfs/${table_target + table_src}
						 |else
						 |	echo 'Directory is not exist.Can be started.'
						 |fi
						 |s_date=$$(date -d \'-1 days\' +%Y-%m-%d)
						 |if [ $$# -eq 1 ]; then
						 |	s_date=$$1
						 |fi
						 |sqoop import --connect $url_source --username $user_source --password $password_source --hive-import --hive-database $database_target --hive-table ${table_target + table_src} --target-dir ${table_target + table_src} --hive-overwrite --query \"select * from ${table_src} where substr(create_time,1,10)>=\'$${s_date}\' and \\$$CONDITIONS\" --hive-drop-import-delims -m 1
						 |""".stripMargin
				val path_cream = path.split("/").dropRight(1).mkString("/") + "/incream"
				withWriter(path_cream)(fileName + ".sh")(command_lines)
			} else {
				var overwrite_para = ""
				if (match_city.equals("010")) {
					overwrite_para = "--hive-overwrite"
				}
				
				val command_lines: String =
					s"""#!/bin/bash
						 |export JAVA_HOME="/usr/local/jdk1.7.0"
						 |#${table_target.toUpperCase + match_red.toUpperCase}
						 |#import ${table_target + match_red}
						 |hdfs dfs -test -e /user/hdfs/${table_target + match_red}
						 |if [ $$? -eq 0 ] ;then
						 |	echo 'Directory is already exist.Delete Directory'
						 | 	hdfs dfs -rm -r /user/hdfs/${table_target + match_red}
						 |else
						 |	echo 'Directory is not exist.Can be started.'
						 |fi
						 |s_date=$$(date -d \'-1 days\' +%Y-%m-%d)
						 |if [ $$# -eq 1 ]; then
						 |	s_date=$$1
						 |fi
						 |
						 |sqoop import --connect $url_source --username $user_source --password $password_source --hive-import --hive-database $database_target --hive-table ${table_target + match_red} --target-dir ${table_target + match_red} $overwrite_para --query \"select * from $table_src where substr(spr_create_date,1,10)>=\'$${s_date}\' and \\$$CONDITIONS\" --hive-drop-import-delims -m 1
						 |""".stripMargin
				val path_cream = path.split("/").dropRight(1).mkString("/") + "/incream"
				withWriter(path_cream)(fileName + ".sh")(command_lines)
			}
		} else {
			var overwrite_para = ""
			if (match_city.equals("010")) {
				overwrite_para = "--hive-overwrite"
			}
			val command_lines: String =
			//				s"""#!/bin/bash
			//					 |export JAVA_HOME="/usr/local/jdk1.7.0"
			//					 |#${table_target.toUpperCase + match_re.toUpperCase}
			//					 |#import ${table_target + match_re}
			//					 |s_date=$$(date -d \'-1 days\' +%Y-%m-%d)
			//					 |if [ $$# -eq 1 ]; then
			//					 |	s_date=$$1
			//					 |fi
			//					 |sqoop import --connect $url_source --username $user_source --password $password_source --hive-import --hive-database $database_target --hive-table ${table_target + match_re} --target-dir ${table_target + match_re} $overwrite_para --query \"select * from $table_src where substr(spr_create_date,1,10)>=\'$${s_date}\' and \\$$CONDITIONS\" --hive-drop-import-delims -m 1
			//					 |""".stripMargin
				s"""#!/bin/bash
					 |export JAVA_HOME="/usr/local/jdk1.7.0"
					 |#${table_target.toUpperCase + match_re.toUpperCase}
					 |#import ${table_target + match_re}
					 |hdfs dfs -test -e /user/hdfs/${table_target + match_re + "_" + match_city}
					 |if [ $$? -eq 0 ] ;then
					 |	echo 'Directory is already exist.Delete Directory'
					 | 	hdfs dfs -rm -r /user/hdfs/${table_target + match_re + "_" + match_city}
					 |else
					 |	echo 'Directory is not exist.Can be started.'
					 |fi
					 |s_date=$$(date -d \'-1 days\' +%Y-%m-%d)
					 |if [ $$# -eq 1 ]; then
					 |	s_date=$$1
					 |fi
					 |sqoop import --connect $url_source --username $user_source --password $password_source --hive-import --hive-database $database_target --hive-table ${table_target + match_re} --target-dir ${table_target + match_re + "_" + match_city} $overwrite_para --query \"select * from $table_src where \\$$CONDITIONS\" --hive-drop-import-delims -m 1
					 |""".stripMargin
			val path_cream = path.split("/").dropRight(1).mkString("/") + "/incream"
			withWriter(path_cream)(fileName + ".sh")(command_lines)
		}
		
	}
	
	/**
		* 创建抽取调度动作完成后的事件生成脚本
		*
		* @param path     文件路径
		* @param fileName 文件名称
		*/
	def eventFile(path: Path, fileName: String) {
		val schemeName = fileName.dropRight(2)
		val now: Date = new Date()
		val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyyMMdd")
		val today = dateFormat.format(now)
		val command_lines: String =
			s"""#!/bin/bash
				 |hadoop fs -touchz /tmp/azkaban_event/$schemeName$$(date +"%Y%m%d").event
				 |""".stripMargin
		val path_event = path.split("/").dropRight(1).mkString("/") + "/gen_event"
		withWriter(path_event)(fileName + ".sh")(command_lines)
		
		
	}
}