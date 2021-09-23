package com.maenguin.notis.model.account;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.Arrays;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hash {

    private byte[] hash;

    public Hash(byte[] hash) {
        this.hash = hash;
    }

    public Hash(String plainText) {
        this.hash = new Sha256Converter().convertToHash(plainText);
    }

    @Override
    public String toString() {
        return "Hash{" +
                "hash=" + new Sha256Converter().bytesToString(hash) +
                '}';
    }
}
