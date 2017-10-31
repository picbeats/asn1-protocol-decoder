package com.lunohod.asn1.protocol.generator

import org.scalatest.FunSpec

class ProtocolReaderSpec extends FunSpec {

  describe("A Protocol.read") {
    describe("when invoked with path to a file") {

      val file = getClass.getClassLoader.getResource("protocol.csv");

      it ("should return a list containing 4 archetypes") {
        val list = ProtocolReader.read(file)

        assert(list.size == 4)
        assert(list(0).isInstanceOf[SyncPattern])
        assert(list(1).isInstanceOf[LogPattern])
        assert(list(2).isInstanceOf[ReportPattern])
        assert(list(3).isInstanceOf[ConfigPattern])
      }
    }
  }
}
