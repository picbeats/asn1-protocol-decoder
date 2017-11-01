package com.lunohod.asn1.protocol.generator

import java.io.Writer

import LineWriter._

sealed abstract class MessagePattern(name:String, url:String) {
  val regexString = url.replace("_", "([^/]*)")

  class CompanionWriter(writer: Writer) {
    def writeCompanionObject():Unit = {
      writer.writeln(s"""object $name { val reg: Regex = \"\"\"$regexString\"\"\".r }""")
    }
  }

  implicit def companionWriter(writer: Writer) = new CompanionWriter(writer)

  def writeTypeDefinitions(writer: Writer):Unit
  def writeCase(writer: Writer):Unit
}

case class SyncPattern(name: String, url:String, request:String, response:String) extends MessagePattern(name, url) {
  def writeTypeDefinitions(writer: Writer):Unit = {
    writer.writeCompanionObject()
    writer.write(s"case class $name(request:$request,response:$response) extends SyncMessage[$request, $response](request, response)\n")
  }

  override def writeCase(writer: Writer): Unit = {
    writer.writeln(s"        case ($name.reg(), Some(req), Some(resp)) => new $name(decode[$request](req), decode[$response](resp))")
  }
}

case class LogPattern(name: String, url:String, request:String) extends MessagePattern(name, url) {
  def writeTypeDefinitions(writer: Writer):Unit = {
    writer.writeCompanionObject()
    writer.writeln(s"case class $name(request:$request) extends LogMessage[$request](request)")
    writer.writeln()
  }

  override def writeCase(writer: Writer): Unit = {
    writer.writeln(s"        case ($name.reg(), Some(req), _) => new $name(decode[$request](req))")
  }
}

case class ReportPattern(name: String, url:String, request: String) extends MessagePattern(name, url) {
  def writeTypeDefinitions(writer: Writer):Unit = {
    writer.writeCompanionObject()
    writer.writeln(s"case class $name(revision:String, request: $request) extends ReportMessage[$request](revision, request)")
    writer.writeln()
  }

  override def writeCase(writer: Writer): Unit = {
    writer.writeln(s"        case ($name.reg(revision), Some(req), _) => new $name(revision, decode[$request](req))")
  }
}

case class ConfigPattern(name: String, url:String, response: String) extends MessagePattern(name, url) {
  def writeTypeDefinitions(writer: Writer):Unit = {
    writer.writeCompanionObject()
    writer.writeln(s"case class $name(revision:String, configuration:$response) extends ConfigurationMessage[$response](revision,configuration)")
    writer.writeln()
  }

  override def writeCase(writer: Writer): Unit = {
    writer.writeln(s"        case ($name.reg(revision), _, Some(resp)) => new $name(revision, decode[$response](resp))")
  }
}

