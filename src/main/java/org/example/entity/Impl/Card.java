package org.example.entity.Impl;

import lombok.*;
import org.example.enums.Suit;

@Data
public class Card implements Comparable<Card> {

    private final String name;
    private final int nominal;
    private final Suit suit;
    private boolean onBoard = false;

    private boolean inComboFirstPlayer;
    private boolean inComboSecondPlayer;

    public Card(Suit suit, int nominal) {
        this.nominal = nominal;
        this.suit = suit;
        this.name = generateName();
    }

    private String generateName() {
        return switch (nominal) {
            case 11 -> "J" + suit.getCode();
            case 12 -> "Q" + suit.getCode();
            case 13 -> "K" + suit.getCode();
            case 14 -> "A" + suit.getCode();
            default -> String.valueOf(nominal) + suit.getCode();
        };
    }

    @Override
    public int compareTo(Card o) {
        return Integer.compare(this.nominal, o.nominal);
    }
}
