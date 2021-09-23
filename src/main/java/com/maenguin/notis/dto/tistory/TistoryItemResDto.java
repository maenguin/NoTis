package com.maenguin.notis.dto.tistory;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(of = {"status", "item"})
public class TistoryItemResDto<T> {
    private int status;
    private T item;

    public TistoryItemResDto(int status, T item) {
        this.status = status;
        this.item = item;
    }
}
