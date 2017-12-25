package com.tal.wuyuhang

import java.text.SimpleDateFormat
import java.util.Date

object Helllo extends App {
	
	println("hello, world")
	val ss =
		"""sssssss
			|ssssssss
			|sssss
			|ssssssss
		""".stripMargin
	
	def add(x: Int, y: Int): Int = x + y
	
	println(ss)
	
	val now:Date = new Date()
	val dateF: SimpleDateFormat = new SimpleDateFormat("yyyyMMdd")
	val hh = dateF.format(now)
	
	println(hh)
	
	val ssss = "SQOOP_ODS_IPS_WF".filter(_.toString!="WF")
	val wd = "WF"
	
}
