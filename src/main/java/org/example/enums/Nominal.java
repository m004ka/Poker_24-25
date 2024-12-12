package org.example.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum Nominal {
    J(11),
    Q(12),
    K(13),
    A(14);
    private final int nominal;


}
