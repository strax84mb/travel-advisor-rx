package com.mn.services.impl;

import com.mn.services.EncodingService;

import javax.inject.Singleton;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

@Singleton
public class EncodingServiceImpl implements EncodingService {

    @Override
    public String encode(String text, byte[] salt) throws NoSuchAlgorithmException {
        var digest = MessageDigest.getInstance("SHA-256");
        digest.update(salt);
        var encoded = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        return DatatypeConverter.printHexBinary(encoded);
    }

    @Override
    public byte[] generateSalt() {
        byte[] bytes = new byte[16];
        new Random(new Date().getTime()).nextBytes(bytes);
        return bytes;
    }
}
