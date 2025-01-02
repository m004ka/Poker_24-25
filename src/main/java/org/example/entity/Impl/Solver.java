package org.example.entity.Impl;

import lombok.RequiredArgsConstructor;
import org.example.enums.Combination;
import org.example.enums.Player;
import org.example.enums.PokerResult;
import org.example.enums.Suit;

import java.util.*;


@RequiredArgsConstructor
public class Solver {

    public Combination setCombination(Card[] cards, Player player) {
        if (isFlashRoyal(cards, player)) return Combination.FLASH_ROYAL;
        if (isStreetFlash(cards, player)) return Combination.STREET_FLASH;
        if (isBOB(cards, player)) return Combination.BOB;
        if (isFullHouse(cards, player)) return Combination.FULL_HOUSE;
        if (isFlash(cards, player)) return Combination.FLASH;
        if (isStreet(cards, player)) return Combination.STREET;
        if (isSet(cards, player)) return Combination.SET;
        if (isTwoPair(cards, player)) return Combination.TWO_PAIR;
        if (isPair(cards, player)) return Combination.PAIR;
        return Combination.HIGH_CARD;
    }

    boolean isFlashRoyal(Card[] cards, Player player) {
        Map<Suit, List<Card>> map = new HashMap<>();
        for (Card card : cards) {
            var list = map.getOrDefault(card.getSuit(), new ArrayList<>());
            list.add(card);
            map.put(card.getSuit(), list);
        }

        var optList = map.values().stream().filter(l -> l.size() > 4).findFirst();

        if (optList.isEmpty()) return false;


        var list = optList.get();
        List<Card> parseCard = new ArrayList<>(optList.get());
        int i = 0;
        do {
            if (parseCard.get(i).getNominal() == 10 && parseCard.get(i + 1).getNominal() == 11 && parseCard.get(i + 2).getNominal() == 12 && parseCard.get(i + 3).getNominal() == 13 && parseCard.get(i + 4).getNominal() == 14) {
                for (int j = i; j < i + 4; j++) {
                    if (player == Player.PLAYER_ONE) {
                        cards[j].setInComboFirstPlayer(true);
                    } else {
                        cards[j].setInComboSecondPlayer(true);
                    }
                }
                return true;
            }
            i++;

        } while (i < parseCard.size() - 4);
        return false;
    }

    boolean isStreetFlash(Card[] cards, Player player) {
        Map<Suit, List<Card>> map = new HashMap<>();
        for (Card card : cards) {
            var list = map.getOrDefault(card.getSuit(), new ArrayList<>());
            list.add(card);
            map.put(card.getSuit(), list);
        }
        var optList = map.values().stream().filter(l -> l.size() > 4).findFirst();

        if (optList.isEmpty()) return false;
        List<Card> parseCard = new ArrayList<>(optList.get());
        Card[] parse = parseCard.toArray(new Card[0]);
        if (isFlash(parse, player)) return true;
        return false;
    }

    boolean isBOB(Card[] cards, Player player) {
        for (int i = 0; i < cards.length - 3; i++) {
            if (cards[i].getNominal() == cards[i + 1].getNominal() && cards[i + 1].getNominal() == cards[i + 2].getNominal() && cards[i + 2].getNominal() == cards[i + 3].getNominal()) {
                for (int j = i; j < i + 4; j++) {
                    if (player == Player.PLAYER_ONE) {
                        cards[j].setInComboFirstPlayer(true);
                    } else {
                        cards[j].setInComboSecondPlayer(true);
                    }
                }
                return true;
            }
        }
        return false;
    }

    boolean isFullHouse(Card[] cards, Player player) {
        boolean pair = false;
        boolean set = false;
        int num = 0;
        int iter = 0;

        for (int i = 0; i < cards.length - 2; i++) {
            if (cards[i].getNominal() == cards[i + 1].getNominal() && cards[i + 1].getNominal() == cards[i + 2].getNominal()) {
                iter = i;
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
                    for (int j = iter; j < i + 3; j++) {
                        if (player == Player.PLAYER_ONE) {
                            cards[j].setInComboFirstPlayer(true);
                        } else {
                            cards[j].setInComboSecondPlayer(true);
                        }
                    }
                    for (int j = i; j < i + 2; j++) {
                        if (player == Player.PLAYER_ONE) {
                            cards[j].setInComboFirstPlayer(true);
                        } else {
                            cards[j].setInComboSecondPlayer(true);
                        }
                    }
                    pair = true;
                    break;
                }
            }
        }
        return pair;
    }

    boolean isFlash(Card[] cards, Player player) {
        Map<Suit, List<Card>> map = new HashMap<>();
        for (Card card : cards) {
            var list = map.getOrDefault(card.getSuit(), new ArrayList<>());
            list.add(card);
            map.put(card.getSuit(), list);
        }

        var optList = map.values().stream().filter(l -> l.size() > 4).findFirst();
        ArrayList<Card> cardList = new ArrayList<>();
        if (optList.isPresent()) {
            cardList.addAll(optList.get());
        }
        if (optList.isEmpty()) return false;

        for (int i = 0; i < cardList.size(); i++) {
            for (int j = 0; j < cards.length; j++) {
                if (cardList.get(i).getName() == cards[j].getName()) {
                    if (player == Player.PLAYER_ONE) cards[j].setInComboFirstPlayer(true);
                    if (player == Player.PLAYER_TWO) cards[j].setInComboSecondPlayer(true);
                }
            }


        }


        return true;
    }

    boolean isStreet(Card[] cards, Player player) {
        Set<Integer> uniqueRanks = new HashSet<>();
        List<Card> newCards = new ArrayList<>();

        for (Card card : cards) {
            if (uniqueRanks.add(card.getNominal())) {
                newCards.add(card);
            }
        }

        if (newCards.size() < 5) {
            return false;
        }
        newCards.sort(Comparator.comparingInt(Card::getNominal));
        List<List<Card>> possibleStreets = new ArrayList<>();

        for (int i = 0; i <= newCards.size() - 5; i++) {
            List<Card> street = new ArrayList<>();
            street.add(newCards.get(i));

            for (int j = 1; j < 5; j++) {
                if (newCards.get(i + j).getNominal() == newCards.get(i + j - 1).getNominal() + 1) {
                    street.add(newCards.get(i + j));
                } else {
                    break;
                }
            }


            if (street.size() == 5) {
                possibleStreets.add(street);
            }
        }

        if (possibleStreets.isEmpty()) {
            return false;
        }

        possibleStreets.sort((a, b) -> Integer.compare(b.get(4).getNominal(), a.get(4).getNominal()));
        List<Card> topStreet = possibleStreets.get(0);

        for (Card card : topStreet) {
            if (player == Player.PLAYER_ONE) {
                card.setInComboFirstPlayer(true);
            } else {
                card.setInComboSecondPlayer(true);
            }
        }

        return true;
    }


    boolean isSet(Card[] cards, Player player) {
        for (int i = 0; i < cards.length - 2; i++) {
            if (cards[i].getNominal() == cards[i + 1].getNominal() && cards[i + 1].getNominal() == cards[i + 2].getNominal()) {
                for (int j = i; j < i + 3; j++) {
                    if (player == Player.PLAYER_ONE) {
                        cards[j].setInComboFirstPlayer(true);
                    } else {
                        cards[j].setInComboSecondPlayer(true);
                    }
                }
                return true;
            }
        }
        return false;
    }

    boolean isTwoPair(Card[] cards, Player player) {
        boolean pair = false;
        boolean result = false;
        for (int i = 0; i < cards.length - 1; i++) {
            if (cards[i].getNominal() == cards[i + 1].getNominal() && !pair) {
                if (player == Player.PLAYER_ONE) {
                    cards[i].setInComboFirstPlayer(true);
                    cards[i + 1].setInComboFirstPlayer(true);
                } else {
                    cards[i].setInComboSecondPlayer(true);
                    cards[i + 1].setInComboSecondPlayer(true);
                }
                pair = true;
                if (i != 5) i++;
                else continue;
            }
            if (cards[i].getNominal() == cards[i + 1].getNominal() && pair) {
                if (player == Player.PLAYER_ONE) {
                    cards[i].setInComboFirstPlayer(true);
                    cards[i + 1].setInComboFirstPlayer(true);
                } else {
                    cards[i].setInComboSecondPlayer(true);
                    cards[i + 1].setInComboSecondPlayer(true);
                }
                result = true;
            }
        }
        int iter = 0;
        for (int i = cards.length - 1; i > 0; i--) {

            if (player == Player.PLAYER_ONE) {
                if (cards[i].isInComboFirstPlayer() && iter < 4) {
                    iter++;
                } else if (cards[i].isInComboFirstPlayer() && iter > 3) {
                    cards[i].setInComboFirstPlayer(false);
                    iter++;
                }
            } else if (player == Player.PLAYER_TWO) {
                if (cards[i].isInComboSecondPlayer() && iter < 4) {
                    iter++;
                } else if (cards[i].isInComboSecondPlayer() && iter > 3) {
                    cards[i].setInComboSecondPlayer(false);
                    iter++;
                }
            }
        }
        return result;
    }

    boolean isPair(Card[] cards, Player player) {
        for (int i = 0; i < cards.length - 1; i++) {
            if (cards[i].getNominal() == cards[i + 1].getNominal()) {
                if (player == Player.PLAYER_ONE) {
                    cards[i].setInComboFirstPlayer(true);
                    cards[i + 1].setInComboFirstPlayer(true);
                } else {
                    cards[i].setInComboSecondPlayer(true);
                    cards[i + 1].setInComboSecondPlayer(true);
                }
                return true;
            }
        }
        return false;
    }
}

