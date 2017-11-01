package com.lunohod.asn1.protocol.generator

import org.rogach.scallop.ScallopConf

final class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {
  val in = opt[String](required = true, descr = "input .csv defining COAP level protocol")
  val out = opt[String](required = true, descr = "output decoder scala file")
  verify()
}
