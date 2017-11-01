package com.lunohod.asn1.protocol.generator

import java.io.Writer
import LineWriter._

object ProtocolDecoderWriter {
  def generateProtocolDecoder(patterns:Array[MessagePattern])(fileWriter: Writer):Unit = {
    fileWriter.writeln("package com.lunohod.asn1.protocol.decoder")
    fileWriter.writeln("// This a generated file!")
    fileWriter.writeln("import com.lunohod.asn.messages._")
    fileWriter.writeln("import scala.reflect.ClassTag")
    fileWriter.writeln("import scala.util.matching.Regex")
    fileWriter.writeln()
    fileWriter.writeln("sealed trait Message")
    fileWriter.writeln()
    fileWriter.writeln("abstract class ProtocolError extends Message")
    fileWriter.writeln("// Bad cases")
    fileWriter.writeln("case class UnknownMessage() extends ProtocolError")
    fileWriter.writeln("case class BrokenMessage(exception: Exception) extends ProtocolError")
    fileWriter.writeln()
    fileWriter.writeln("// Patterns")
    fileWriter.writeln("abstract class LogMessage[T](content:T) extends Message")
    fileWriter.writeln("abstract class ConfigurationMessage[T](revision:String, content:T) extends Message")
    fileWriter.writeln("abstract class SyncMessage[TRequest,TResponse](request:TRequest, response:TResponse) extends Message")
    fileWriter.writeln("abstract class ReportMessage[TRequest](revision:String, content:TRequest) extends Message")
    fileWriter.writeln()
    patterns.foreach(_.writeTypeDefinitions(fileWriter))

    fileWriter.writeln("object ProtocolDecoder {")
    fileWriter.writeln("  def decode(verb:String, rl:String, request:Option[Array[Byte]], response:Option[Array[Byte]])(implicit decoder: PayloadDecoder):Message = {")

    fileWriter.writeln("    def decode[T:ClassTag](bytes:Array[Byte]):T = {")
    fileWriter.writeln("      decoder.decode[T](bytes)")
    fileWriter.writeln("    }")

    fileWriter.writeln("    try {")
    fileWriter.writeln("      (rl, request, response) match {")
    patterns.foreach(_.writeCase(fileWriter))

    fileWriter.writeln("      case _ => UnknownMessage()")
    fileWriter.writeln("      }")
    fileWriter.writeln("    } catch {")
    fileWriter.writeln("      case e:Exception => BrokenMessage(e)")
    fileWriter.writeln("    }")
    fileWriter.writeln("  }")
    fileWriter.writeln("}")
  }
}
