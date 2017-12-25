package com.tal.wuyuhang

import org.scalatest.FunSpec

class HellloTest extends FunSpec {
  describe("Hello"){
    it("can add x and y"){
      assert(Helllo.add(1,2) == 3)
    }
  }
}
