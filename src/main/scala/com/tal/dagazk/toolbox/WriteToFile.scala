package com.tal.dagazk.toolbox

import java.io.File
import java.io.Writer
import java.io.BufferedWriter
import java.io.FileWriter

import com.tal.dagazk.Util.Path

/**
	* Created by smallain on 2017/9/3.
	*/
trait WriteToFile {
	
	/**
		* path:			输出文件路径
		* fileName:	输出文件名称
		* messages:	输出文件内容
		*/
	
	def withWriter(path: Path)(fileName: String)(messages: String): Unit = {
		var writer: Writer = null
		try {
			val fileDir = new File(path)
			if (fileDir.isDirectory) {
				val file = new File(path + "/" + fileName)
				if (!file.exists()) file.createNewFile()
				writer = new BufferedWriter(new FileWriter(file))
				writer.write(messages)
				writer.flush()
			} else {
				fileDir.mkdirs()
				val file = new File(path + "/" + fileName)
				if (!file.exists()) file.createNewFile()
				writer = new BufferedWriter(new FileWriter(file))
				writer.write(messages)
				writer.flush()
			}
			
		} finally {
			if (writer != null) writer.close()
		}
	}
	
}
