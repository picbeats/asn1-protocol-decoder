package com.lunohod.asn1.protocol.generator

import java.io.{BufferedWriter, File, FileWriter}
import org.rogach.scallop.ScallopConf

class Main(args: Array[String]) {
  val conf = new Conf(args)  // Note: This line also works for "object Main extends App"

  def start(): Unit = {
    val patterns = ProtocolReader.read(conf.in.apply());
    patterns.foreach(println _)
    val file = new File(conf.out.apply())
    val bw = new BufferedWriter(new FileWriter(file))
    ProtocolDecoderWriter.generateProtocolDecoder(patterns)(bw)
    bw.close()
  }
}

object Main extends App {
  private val VERSION = "0.1"
  try {
    System.out.println("ASN1 REST Protocol decoder compiler v" + VERSION)
    System.out.println("        (c) 2017 Roman Razilov")
    new Main(args).start()
  } catch {
    case ex: Exception => System.err.println(ex)
  }
}

final class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {
  val in = opt[String](required = true, descr = "input .csv defining COAP level protocol")
  val out = opt[String](required = true, descr = "output decoder scala file")
  verify()
}

