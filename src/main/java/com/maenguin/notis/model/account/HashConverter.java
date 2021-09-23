package com.maenguin.notis.model.account;

public interface HashConverter {

    byte[] convertToHash(String plainText);

    String bytesToString(byte[] hash);
}
