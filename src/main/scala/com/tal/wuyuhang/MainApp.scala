package com.tal.wuyuhang

object MainApp extends App {
	if(args.length<1){
		println("please input some path!")
	} else {
		val sourceCode = SourceCode.fromFile(args(0))
		println(s"name = ${sourceCode.name}		lines = ${sourceCode.count}")
	}
}
