package org.example.entity.Impl;

import org.example.InvalidPokerBoardException;
import org.example.entity.Dealer;
import org.example.enums.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class DealerImpl implements Dealer {
    private final Deck deck = new Deck();

    @Override
    public Board dealCardsToPlayers() {
        Card[] firstPlayer = new Card[]{deck.getRandomCard(), deck.getRandomCard()};
        Card[] secondPlayer = new Card[]{deck.getRandomCard(), deck.getRandomCard()};

        return new Board(firstPlayer[0].getName() + firstPlayer[1].getName(), secondPlayer[0].getName() + secondPlayer[1].getName(), null, null, null);
    }

    @Override
    public Board dealFlop(Board board) {
        Card[] flop = new Card[]{deck.getRandomCard(), deck.getRandomCard(), deck.getRandomCard()};
        return new Board(board.getPlayerOne(), board.getPlayerTwo(), flop[0].getName() + flop[1].getName() + flop[2].getName(), null, null);

    }

    @Override
    public Board dealTurn(Board board) {
        Card card = deck.getRandomCard();
        return new Board(board.getPlayerOne(), board.getPlayerTwo(), board.getFlop(), card.getName(), null);
    }

    @Override
    public Board dealRiver(Board board) {
        Card card = deck.getRandomCard();
        return new Board(board.getPlayerOne(), board.getPlayerTwo(), board.getFlop(), board.getTurn(), card.getName());
    }

    public Card[] parseToCards(Board board, Player player) {
        Deck deck1 = new Deck();
        String cardParse = (player == Player.PLAYER_ONE ? board.getPlayerOne() + board.getFlop() + board.getTurn() + board.getRiver() : board.getPlayerTwo() + board.getFlop() + board.getTurn() + board.getRiver());
        Pattern pattern = Pattern.compile("(10|[2-9JQKA])([CDHS])");
        Matcher matcher = pattern.matcher(cardParse);
        int iter = 0;
        Card[] cards = new Card[7];
        while (matcher.find()) {
            if (iter > 6) throw new InvalidPokerBoardException("На столе больше карт чем нужно");
            var name = matcher.group(1) + matcher.group(2);
            var card = Stream.of(deck1.getCards()).filter(c -> c.getName().equals(name)).findFirst().orElseThrow(() -> new InvalidPokerBoardException("Не удалось распарсить карту на столе"));
            if (card.isOnBoard()) throw new InvalidPokerBoardException("Дублирование карт");
            card.setOnBoard(true);
            cards[iter] = card;
            iter++;
        }
        if (iter < 7) throw new InvalidPokerBoardException("На столе не хватает карт или не все карты на столе верны");

        return cards;
    }

    public void findErrors(Board board) {
        if (board == null) throw new InvalidPokerBoardException("Доска пуста");
        if (board.getPlayerOne() == null) throw new InvalidPokerBoardException("У первого игрока нет карт");
        if (board.getPlayerTwo() == null) throw new InvalidPokerBoardException("У второго игрока нет карт");
        if (board.getFlop() == null) throw new InvalidPokerBoardException("На столе не разложен Флоп");
        if (board.getTurn() == null) throw new InvalidPokerBoardException("На столе не разложен Терн");
        if (board.getRiver() == null) throw new InvalidPokerBoardException("На столе не разложен Ривер");
    }

    @Override
    public PokerResult decideWinner(Board board) throws InvalidPokerBoardException {
        findErrors(board);
        Card[] cards1 = parseToCards(board, Player.PLAYER_ONE);
        Card[] cards2 = parseToCards(board, Player.PLAYER_TWO);



        if (!Stream.of(cards1).allMatch(Card::isOnBoard))
            throw new InvalidPokerBoardException("Не корректные карты на столе");
        if (!Stream.of(cards2).allMatch(Card::isOnBoard))
            throw new InvalidPokerBoardException("Не корректные карты на столе");
        Arrays.sort(cards1);
        Arrays.sort(cards2);

        Combination firstPlayerCombo = solverCombination(cards1, Player.PLAYER_ONE);
        Combination secondPlayerCombo = solverCombination(cards2, Player.PLAYER_TWO);
        System.out.println("fp: "+ firstPlayerCombo );
        System.out.println("sp: " + secondPlayerCombo);
        return result(firstPlayerCombo, secondPlayerCombo, cards1, cards2);
    }

    public PokerResult result(Combination firstCombo, Combination secondCombo, Card[] fPlayer, Card[] sPlayer) {
        if (firstCombo.ordinal() < secondCombo.ordinal()) {
            return PokerResult.PLAYER_ONE_WIN;
        } else if (firstCombo.ordinal() > secondCombo.ordinal()) {
            return PokerResult.PLAYER_TWO_WIN;
        } else if (firstCombo == Combination.HIGH_CARD && secondCombo == Combination.HIGH_CARD) {
            return solverDraw(fPlayer, sPlayer);
        } else if (firstCombo == secondCombo) {
            return solverCombo(fPlayer, sPlayer, firstCombo);
        }

        return PokerResult.DRAW;
    }

    public PokerResult Full_House_Solver(List<Card> first, List<Card> second) {
        int setFp = first.get(2).getNominal(), setSp = second.get(2).getNominal(), pairFp, pairSp;
        if (setFp != setSp) {
            if (setFp > setSp) return PokerResult.PLAYER_ONE_WIN;
            return PokerResult.PLAYER_TWO_WIN;
        } else {
            if (first.get(0).getNominal() != setFp) {
                pairFp = first.get(4).getNominal();
            }
            pairFp = first.get(0).getNominal();
            if (second.get(0).getNominal() != setSp) {
                pairSp = second.get(4).getNominal();
            }
            pairSp = second.get(0).getNominal();
            if (pairFp == pairSp) return PokerResult.DRAW;
            if (pairFp > pairSp) return PokerResult.PLAYER_ONE_WIN;
            return PokerResult.PLAYER_TWO_WIN;
        }

    }


    public PokerResult solverCombo(Card[] cards1, Card[] cards2, Combination combination) {
        if (combination == Combination.FLASH_ROYAL) return PokerResult.DRAW;
        List<Card> firstPlayer = new ArrayList<Card>();
        List<Card> secondPlayer = new ArrayList<Card>();

        for (int i = 0; i < 7; i++) {
            if (cards1[i].isInComboFirstPlayer()) firstPlayer.add(cards1[i]);
            if (cards2[i].isInComboSecondPlayer()) secondPlayer.add(cards2[i]);
        }
        if (combination != Combination.FULL_HOUSE && combination != Combination.TWO_PAIR && combination != Combination.FLASH && combination != Combination.STREET_FLASH) {
            if (firstPlayer.get(firstPlayer.size() - 1).getNominal() > secondPlayer.get(secondPlayer.size() - 1).getNominal())
                return PokerResult.PLAYER_ONE_WIN;
            else if (firstPlayer.get(firstPlayer.size() - 1).getNominal() < secondPlayer.get(secondPlayer.size() - 1).getNominal())
                return PokerResult.PLAYER_TWO_WIN;
        } else {
            if (combination == Combination.TWO_PAIR) {
                if (firstPlayer.get(3).getNominal() > secondPlayer.get(3).getNominal())
                    return PokerResult.PLAYER_ONE_WIN;
                if (firstPlayer.get(3).getNominal() < secondPlayer.get(3).getNominal())
                    return PokerResult.PLAYER_TWO_WIN;
                if (firstPlayer.get(0).getNominal() > secondPlayer.get(0).getNominal())
                    return PokerResult.PLAYER_ONE_WIN;
                if (firstPlayer.get(0).getNominal() < secondPlayer.get(0).getNominal())
                    return PokerResult.PLAYER_TWO_WIN;
            } else if (combination == Combination.FULL_HOUSE) {
                return Full_House_Solver(firstPlayer, secondPlayer);
            }
        }
        List<Card> kicker1 = new ArrayList<>();
        List<Card> kicker2 = new ArrayList<>();
        for (int i = 6, j = 0; j != 5 - firstPlayer.size(); i--) {
            if (!cards1[i].isInComboFirstPlayer()) {
                kicker1.add(cards1[i]);
                j++;
            }
        }
        for (int i = 6, j = 0; j < 5 - secondPlayer.size(); i--) {
            if (!cards2[i].isInComboSecondPlayer()) {
                kicker2.add(cards2[i]);
                j++;
            }
        }
        if(kicker1.isEmpty() && kicker2.isEmpty()){
            for (int i = secondPlayer.size() - 1; i != -1; i--) {
                if (firstPlayer.get(i).getNominal() > secondPlayer.get(i).getNominal()) return PokerResult.PLAYER_ONE_WIN;
                if (firstPlayer.get(i).getNominal() < secondPlayer.get(i).getNominal()) return PokerResult.PLAYER_TWO_WIN;
            }
        }else {
            for (int i = kicker1.size() - 1; i != -1; i--) {
                if (kicker1.get(i).getNominal() > kicker2.get(i).getNominal()) return PokerResult.PLAYER_ONE_WIN;
                if (kicker1.get(i).getNominal() < kicker2.get(i).getNominal()) return PokerResult.PLAYER_TWO_WIN;
            }
        }
        return PokerResult.DRAW;
    }


    public PokerResult solverDraw(Card[] player1, Card[] player2) {
        for (int i = player1.length - 1 ; i != -1 + 2; i--) {
            if (player1[i].getNominal() > player2[i].getNominal()) return PokerResult.PLAYER_ONE_WIN;
            if (player1[i].getNominal() < player2[i].getNominal()) return PokerResult.PLAYER_TWO_WIN;
        }
        return PokerResult.DRAW;
    }

    Solver solver = new Solver();
    public Combination solverCombination(Card[] cards, Player player) {
        return solver.setCombination(cards, player);
    }
}

