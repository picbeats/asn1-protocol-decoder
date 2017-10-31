package com.lunohod.messages;

import org.bn.CoderFactory;
import org.bn.IDecoder;
import org.bn.IEncoder;

public class UperCoderFactory {

    private final String encodingSchema = "PER/Unaligned";
    private CoderFactory coderFactory = new CoderFactory();

    public IDecoder newDecoder() {
        return coderFactory.newDecoder(encodingSchema);
    }

    public IEncoder newEncoder() { return coderFactory.newEncoder(encodingSchema);    }
}
