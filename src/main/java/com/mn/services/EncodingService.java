package com.mn.services;

import java.security.NoSuchAlgorithmException;

public interface EncodingService {

    String encode(String text, byte[] salt) throws NoSuchAlgorithmException;

    byte[] generateSalt();
}
