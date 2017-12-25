package com.tal.dagazk

trait LoadAdjacencyList {
	type Path = String
	
	def analysisFile(path: Path): Map[String, List[String]] = {
		import scala.io._
		val source = Source.fromFile(path)
		val lines = source.getLines().toList.filter(_!="--------").filter(_!="#增量")
		val dependenciseProperties = lines.map(_.split("="))
		val dpMap = (for (dp <- dependenciseProperties) yield dp.head -> dp.tail).toMap
		for ((key, value) <- dpMap) yield (key,value.toList.flatMap(_.split(",")))
	}
}
