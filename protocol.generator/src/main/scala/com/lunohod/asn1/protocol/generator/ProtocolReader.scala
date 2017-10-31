package com.lunohod.asn1.protocol.generator

import java.io.File
import java.net.URL

import kantan.csv._
import kantan.csv.engine._
import kantan.csv.ops._

import scala.collection.mutable.ArrayBuffer


object ProtocolReader {
  implicit val protocolCsvDecoder: RowDecoder[MessagePattern] = RowDecoder.ordered {
    (resource: String, name: String, request: Option[String], response: Option[String]) => {
      (request, response) match {
        case (Some(req), Some(resp)) => SyncPattern(name, resource, req, resp)
        case (Some(req), None) =>
          if (resource.contains('_'))
            ReportPattern(name, resource, req)
          else
            LogPattern(name, resource, req)
        case (None, Some(resp)) => ConfigPattern(name, resource, resp)
        case _ => throw new Exception(s"Bad row $resource,$name,$request,$response")
      }
    }
  }

  def read[A](source: CsvSourceOps[A]): Array[MessagePattern] = {
    source.unsafeReadCsv[ArrayBuffer,MessagePattern](rfc.withHeader).toArray
  }
}

