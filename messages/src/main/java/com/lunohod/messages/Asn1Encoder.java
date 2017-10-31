package com.lunohod.messages;

import com.lunohod.asn.messages.Group;
import org.bn.IEncoder;

import java.io.ByteArrayOutputStream;

public class Asn1Encoder {

    UperCoderFactory encoderFactory;

    public Asn1Encoder(UperCoderFactory encoderFactory) {
        this.encoderFactory = encoderFactory;
    }

    public byte[] encode(Object object) throws Exception {
        IEncoder encoder = encoderFactory.newEncoder();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        encoder.encode(object, outputStream);
        return outputStream.toByteArray();
    }
}

