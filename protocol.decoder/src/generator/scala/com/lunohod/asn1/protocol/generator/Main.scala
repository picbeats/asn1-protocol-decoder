package com.lunohod.asn1.protocol.generator

import java.io.{BufferedWriter, File, FileWriter}
import java.nio.file.{Files, Path, Paths}

class Main(args: Array[String]) {
  val conf = new Conf(args)  // Note: This line also works for "object Main extends App"

  def start(): Unit = {
    val source = conf.in.apply()
    val sourceFile= Paths.get(source).toFile
    println(s"Source: $sourceFile")
    val patterns = ProtocolReader.read(sourceFile);
    patterns.foreach(println _)

    val target = conf.out.apply()
    println(s"Target: $target")
    val file = new File(target)
    val directory = Paths.get(target).getParent()
    println(s"Create directory to: $directory")
    Files.createDirectories(directory)
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



