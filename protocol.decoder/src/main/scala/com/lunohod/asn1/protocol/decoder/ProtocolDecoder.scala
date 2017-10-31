package com.lunohod.asn1.protocol.decoder

import com.lunohod.asn.messages._

import scala.reflect.ClassTag

sealed trait Message

abstract class ProtocolError extends Message
// Bad cases
case class UnknownMessage() extends ProtocolError
case class BrokenMessage(exception: Exception) extends ProtocolError

// Archetypes
abstract class LogMessage[T](content:T) extends Message
abstract class ConfigurationMessage[T](revision:String, content:T) extends Message
abstract class SyncMessage[TRequest,TResponse](request:TRequest, response:TResponse) extends Message
abstract class ReportMessage[TRequest](revision:String, content:TRequest) extends Message

object LogGroup { val reg = """"/p/p/0""".r }
case class LogGroup(request:Group) extends LogMessage[Group](request)

object GetGroupBuilderConfiguration { val reg = """/p/cfg/([^/]*)/0""".r }
case class GetGroupBuilderConfiguration(revision:String, configuration:GroupBuilderConfiguration) extends ConfigurationMessage[GroupBuilderConfiguration](revision,configuration)

object PingPong { val reg = """/s/0""".r}
case class PingPong(request:Ping, response:Pong)extends SyncMessage[Ping, Pong](request, response)

object ReportInternalState { val reg = """/p/s/[^/]*/0""".r }
case class ReportInternalState(revision:String, request: InternalState) extends ReportMessage[InternalState](revision, request)

object ProtocolDecoder {
  def decode(verb:String, rl:String, request:Option[Array[Byte]], response:Option[Array[Byte]])(implicit decoder: PayloadDecoder):Message = {

    def decode[T:ClassTag](bytes:Array[Byte]):T = {
      decoder.decode[T](bytes)
    }

    try {
      (rl, request, response) match {
        case (GetGroupBuilderConfiguration.reg(revision), _, Some(response)) => new GetGroupBuilderConfiguration(revision, decode[GroupBuilderConfiguration](response))
        case (LogGroup.reg(), Some(request), _) => new LogGroup(decode[Group](request))
        case (ReportInternalState.reg(revision), Some(request), _) => new ReportInternalState(revision, decode[InternalState](request))
        case (PingPong.reg(), Some(request), Some(response)) => new PingPong(decode[Ping](request), decode[Pong](response))
        case _ => new UnknownMessage();
      }
    } catch {
      case e:Exception => BrokenMessage(e)
    }
  }
}
