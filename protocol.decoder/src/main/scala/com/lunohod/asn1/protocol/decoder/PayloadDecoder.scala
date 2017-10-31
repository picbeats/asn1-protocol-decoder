package com.lunohod.asn1.protocol.decoder

import com.lunohod.messages.{Asn1Decoder, UperCoderFactory}

import scala.reflect.ClassTag

trait PayloadDecoder {
  def decode[T:ClassTag](bytes:Array[Byte]):T
}


object ASN1MessageDecoder extends PayloadDecoder {
  private val coderFactory = new UperCoderFactory()
  private val decoder = new Asn1Decoder(coderFactory)

  override def decode[T: ClassTag](bytes: Array[Byte]) = {
    val tClass = implicitly[ClassTag[T]].runtimeClass
    decoder.decode(tClass,bytes).asInstanceOf[T]
  }
}
