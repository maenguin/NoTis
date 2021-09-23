package com.maenguin.notis.dto.tistory;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"tistory"})
public class TistoryReponse<T> {
    private T tistory;

    public TistoryReponse(T tistory) {
        this.tistory = tistory;
    }
}
