package org.example.enums;

import lombok.RequiredArgsConstructor;
import org.example.entity.Impl.Card;

import java.util.*;
import java.util.function.Function;


@RequiredArgsConstructor
public enum Combination {
    FLASH_ROYAL(cards -> {
        Map<Suit, List<Card>> map = new HashMap<>();
        for (Card card : cards) {
            var list = map.getOrDefault(card.getSuit(), new ArrayList<>());
            list.add(card);
            map.put(card.getSuit(), list);
        }

        var optList = map.values()
                .stream()
                .filter(l -> l.size() > 4)
                .findFirst();

        if (optList.isEmpty()) return false;

        var list = optList.get();
        int prev = list.get(0).getNominal();

        if (prev != 10 || list.get(list.size() - 1).getNominal() != 14)
            return false;

        for (int i = 1; i < list.size(); i++) {
            int current = list.get(i).getNominal();
            if (current - prev != 1) return false;
            prev = current;
        }

        return true;
    }),
    STREET_FLASH(cards -> {
        for (int i = 0; i < 7 - 5; i++) {
            if ((cards[i].getNominal() + 1 == cards[i + 1].getNominal() && cards[i + 1].getNominal() + 1 == cards[i + 2].getNominal() && cards[i + 2].getNominal() + 1 == cards[i + 3].getNominal() && cards[i + 3].getNominal() + 1 == cards[i + 4].getNominal()) && (cards[i].getSuit() == cards[i + 1].getSuit() && cards[i + 1].getSuit() == cards[i + 2].getSuit() && cards[i + 2].getSuit() == cards[i + 3].getSuit() && cards[i + 3].getSuit() == cards[i + 4].getSuit())) {
                return true;
            }
        }
        return false;
    }),
    BOB(cards -> {
        for (int i = 0; i < 7 - 3; i++) {
            if (cards[i].getNominal() == cards[i + 1].getNominal() && cards[i + 1].getNominal() == cards[i + 2].getNominal() && cards[i + 2].getNominal() == cards[i + 3].getNominal()) {
                return true;
            }
        }
        return false;
    }),
    FULL_HOUSE(cards -> {
        boolean pair = false;
        boolean set = false;
        int num = 0;
        for (int i = 0; i < 7 - 2; i++) {
            if (cards[i].getNominal() == cards[i + 1].getNominal() && cards[i + 1].getNominal() == cards[i + 2].getNominal()) {
                set = true;
                num = i;
                break;
            }
        }
        if (set) {
            Card[] cards1 = new Card[4];
            for (int i = 0, j = 0; i < 4; i++, j++) {
                if (j == num || j == num + 1 || j == num + 2) {
                    i--;
                } else {
                    cards1[i] = cards[j];
                }
            }
            Arrays.sort(cards1); // Они конечно и так отсортированны, но на всякий проверять не было времени
            for (int i = 0; i < 4 - 1; i++) {
                if (cards1[i].getNominal() == cards1[i + 1].getNominal()) {
                    pair = true;
                    break;
                }
            }
        }
        return pair;
    }),
    FLASH(cards -> {
        int c = 0, d = 0, h = 0, s = 0;
        for (int i = 0; i < 7; i++) {
            switch (cards[i].getSuit()) {
                case CLUBS:
                    c++;
                    break;
                case DIAMONDS:
                    d++;
                    break;
                case HEARTS:
                    h++;
                    break;
                case SPADES:
                    s++;
                    break;
            }
        }
        return c > 4 || d > 4 || h > 4 || s > 4;
    }),
    STREET(cards -> {
        for (int i = 0; i < 7 - 5; i++) {
            if (cards[i].getNominal() + 1 == cards[i + 1].getNominal() && cards[i + 1].getNominal() + 1 == cards[i + 2].getNominal() && cards[i + 2].getNominal() + 1 == cards[i + 3].getNominal() && cards[i + 3].getNominal() + 1 == cards[i + 4].getNominal()) {
                return true;
            }
        }
        return false;
    }),
    SET(cards -> {
        for (int i = 0; i < 7 - 3; i++) {
            if (cards[i].getNominal() == cards[i + 1].getNominal() && cards[i + 1].getNominal() == cards[i + 2].getNominal()) {
                return true;
            }
        }
        return false;
    }),
    TWO_PAIR(cards -> {
        boolean pair = false;
        boolean result = false;
        for (int i = 0; i < 7 - 1; i++) {
            if (cards[i].getNominal() == cards[i + 1].getNominal() && !pair) {
                pair = true;
                if (i != 5) i++;
                else continue;
            }
            if (cards[i].getNominal() == cards[i + 1].getNominal() && pair) {
                result = true;
            }
        }
        return result;
    }),
    PAIR(cards -> {
        for (int i = 0; i < 7 - 1; i++) {
            if (cards[i].getNominal() == cards[i + 1].getNominal()) {
                return true;
            }
        }
        return false;
    }),
    HIGH_CARD(cards -> false);

    private final Function<Card[], Boolean> verifier;

    public boolean verify(Card[] cards) {
        return verifier.apply(cards);
    }
}
