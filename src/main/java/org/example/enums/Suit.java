package org.example.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum Suit {
    CLUBS('C'),
    DIAMONDS('D'),
    HEARTS('H'),
    SPADES('S');
    private final char code;


}
