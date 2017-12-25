package com.tal.dagazk

import org.scalatest.FunSpec

class LoadAdjacencyListTest extends FunSpec with LoadAdjacencyList {
	
	describe("LoadAdjacencyListTest") {
		
		it("should analysis .properties file and retern  Map[String, List[String]]") {
			val map = analysisFile("./src/test/resources/SQOOP_ODS_IPS.txt")
			val list = map("SQOOP_ODS_IPS_END").toString()
			assert(list == "List(SQOOP_ODS_IPS_TB_STU_INCLASS_QUESTION, SQOOP_ODS_IPS_TB_STUDENT_OPUS, ETL_ODS_IPS_TB_STU_ANSWER_CONTENT_DETAIL)")
		}
		
	}
}
