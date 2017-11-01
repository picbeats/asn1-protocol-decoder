package com.lunohod.asn1.protocol.generator

import java.io.Writer

class LineWriter(writer: Writer) {
  def writeln(string:String): Unit = {
    writer.write(string)
    writeln()
  }

  def writeln():Unit= {
    writer.write("\n")
  }
}

object LineWriter {
  implicit def lineWriter(fileWriter: Writer) = new LineWriter(fileWriter)
}