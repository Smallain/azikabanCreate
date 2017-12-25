package com.tal.dagazk.toolbox

import com.tal.dagazk.Util.Path

object HqlFile extends WriteToFile{
	def hqlFileMaker(path: Path, fileName: String): Unit = {
		if (fileName.toUpperCase.contains("ETL_")) {
			val path_cream = path.split("/").dropRight(1).mkString("/") + "/etl"
			withWriter(path_cream)(fileName + ".hql")("")
		}
		
	}
}
