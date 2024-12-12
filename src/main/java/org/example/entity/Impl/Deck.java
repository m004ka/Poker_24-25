package org.example.entity.Impl;

import lombok.Getter;
import org.example.InvalidPokerBoardException;
import org.example.enums.Suit;

import java.util.Random;
import java.util.stream.Stream;

public class Deck {
    private static final Random random = new Random();
    private static final int MIN_NOMINAL = 0;
    private static final int MAX_NOMINAL = 51;
    @Getter
    private final Card[] cards = generateDeck();

    private Card[] generateDeck() {
        Suit[] suit = new Suit[]{Suit.SPADES, Suit.HEARTS, Suit.DIAMONDS, Suit.CLUBS};
        int number = 0;
        Card[] cards = new Card[52];
        for (int i = 0; i < 4; i++) {
            for (int j = 2; j < 15; j++) {
                cards[number] = new Card(suit[i], j);
                number++;
            }
        }
        return cards;
    }

    public Card getRandomCard() {
        if (Stream.of(cards).allMatch(Card::isOnBoard))
            throw new InvalidPokerBoardException("Все карты колоды используются");
        int num = random.nextInt((MAX_NOMINAL - MIN_NOMINAL) + 1) + MIN_NOMINAL;
        if (cards[num].isOnBoard()) {
            return getRandomCard();
        } else {
            cards[num].setOnBoard(true);
            return cards[num];
        }
    }
}
