package org.example.entity.Impl;

import org.example.InvalidPokerBoardException;
import org.example.entity.Dealer;
import org.example.enums.*;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class DealerImpl implements Dealer {
    private final Deck deck = new Deck();

    @Override
    public Board dealCardsToPlayers() {
        Card[] firstPlayer = new Card[]{deck.getRandomCard(), deck.getRandomCard()};
        Card[] secondPlayer = new Card[]{deck.getRandomCard(), deck.getRandomCard()};

        return new Board(firstPlayer[0].getName() + firstPlayer[1].getName(),
                secondPlayer[0].getName() + secondPlayer[1].getName(),
                null,
                null,
                null);
    }

    @Override
    public Board dealFlop(Board board) {
        Card[] flop = new Card[]{deck.getRandomCard(), deck.getRandomCard(), deck.getRandomCard()};
        return new Board(board.getPlayerOne(),
                board.getPlayerTwo(),
                flop[0].getName() + flop[1].getName() + flop[2].getName(),
                null,
                null);

    }

    @Override
    public Board dealTurn(Board board) {
        Card card = deck.getRandomCard();
        return new Board(board.getPlayerOne(),
                board.getPlayerTwo(),
                board.getFlop(),
                card.getName(),
                null
        );
    }

    @Override
    public Board dealRiver(Board board) {
        Card card = deck.getRandomCard();
        return new Board(board.getPlayerOne(),
                board.getPlayerTwo(),
                board.getFlop(),
                board.getTurn(),
                card.getName()
        );
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
            var card = Stream.of(deck1.getCards()).filter(c -> c.getName().equals(name))
                    .findFirst()
                    .orElseThrow(() -> new InvalidPokerBoardException("Не удалось распарсить карту на столе"));
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
        Card[] fPlayer = new Card[]{cards1[0], cards1[1]};
        Card[] sPlayer = new Card[]{cards2[0], cards2[1]};

        if (!Stream.of(cards1).allMatch(Card::isOnBoard))
            throw new InvalidPokerBoardException("Не корректные карты на столе");
        if (!Stream.of(cards2).allMatch(Card::isOnBoard))
            throw new InvalidPokerBoardException("Не корректные карты на столе");
        Arrays.sort(cards1);
        Arrays.sort(cards2);
        Combination firstPlayerCombo = solverCombination(cards1);
        Combination secondPlayerCombo = solverCombination(cards2);
        return result(firstPlayerCombo, secondPlayerCombo, fPlayer, sPlayer);
    }

    public PokerResult result(Combination firstCombo, Combination secondCombo, Card[] fPlayer, Card[] sPlayer) {
        if (firstCombo.ordinal() < secondCombo.ordinal()) {
            return PokerResult.PLAYER_ONE_WIN;
        } else if (firstCombo.ordinal() > secondCombo.ordinal()) {
            return PokerResult.PLAYER_TWO_WIN;
        } else {
            return solverDraw(fPlayer, sPlayer);
        }
    }

    public PokerResult solverDraw(Card[] player1, Card[] player2) {
        PokerResult res = PokerResult.DRAW;
        Arrays.sort(player1);
        Arrays.sort(player2);
        if (player1[1].getNominal() > player2[1].getNominal()) {
            res = PokerResult.PLAYER_ONE_WIN;
        } else if (player1[1].getNominal() < player2[1].getNominal()) {
            res = PokerResult.PLAYER_TWO_WIN;
        }
        return res;
    }

    public Combination solverCombination(Card[] cards) {
        return Arrays.stream(Combination.values())
                .filter(combination -> combination.verify(cards))
                .findFirst()
                .orElse(Combination.HIGH_CARD);
    }
}

