package com.maenguin.notis.model.account;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class Sha256Converter implements HashConverter {

    @Override
    public byte[] convertToHash(String plainText) {
        Assert.notNull(plainText, "plainText must not be null");
        Assert.isTrue(!plainText.isEmpty() && !plainText.isBlank(), "plainText should not be empty and blank");
        byte[] hash = new byte[]{};
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            hash = messageDigest.digest(plainText.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            log.error("error: no such algorithm `SHA-256`", e);
        }
        return hash;
    }

    @Override
    public String bytesToString(byte[] hash) {
        Assert.notNull(hash, "hash must not be null");
        Assert.isTrue(hash.length > 0, "hash should have any content");
        var stringBuilder = new StringBuilder();
        for (byte b : hash) {
            stringBuilder.append(Integer.toHexString(0xff & b));
        }
        return stringBuilder.toString();
    }
}
