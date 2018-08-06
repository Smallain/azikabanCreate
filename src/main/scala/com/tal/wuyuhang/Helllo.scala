package com.tal.wuyuhang

import java.text.SimpleDateFormat
import java.util.Date

object Helllo extends App {
	var countMoney: Double = 0.0
	var year:Int = 1
	
	def money(benjin: Double): Double = {
		if (year<=60) {
			countMoney = benjin + benjin * 0.0275
			year+=1
			money(countMoney)
		}
		countMoney
	}
	println(money(100000))
}

