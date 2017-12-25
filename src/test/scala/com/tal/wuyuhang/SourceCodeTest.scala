package com.tal.wuyuhang

import org.scalatest.FunSpec

class SourceCodeTest extends FunSpec {
	
	describe("SourceCodeTest") {
		
		it("should fromStrLines") {
			val sourceCode: SourceCode = SourceCode.fromFile("./src/test/resources/sourceFileSameple.txt")
			assert(sourceCode.name == "sourceFileSameple.txt")
			assert(sourceCode.path == "./src/test/resources/sourceFileSameple.txt")
			assert(sourceCode.count == 5)
		}
		
	}
}
