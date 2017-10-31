package com.lunohod.messages;

import org.bn.IDecoder;

import java.io.ByteArrayInputStream;

public class Asn1Decoder {

    protected UperCoderFactory coderFactory;

    public Asn1Decoder(UperCoderFactory coderFactory) {
        this.coderFactory = coderFactory;
    }

    public Object decode(Class t, byte[] bytes) throws Exception {
        IDecoder decoder = coderFactory.newDecoder();
        ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        return decoder.decode(stream, t);
    }
}
