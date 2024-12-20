package org.example;

import org.example.entity.Dealer;
import org.example.entity.Impl.Board;
import org.example.entity.Impl.DealerImpl;

public class PokerGame {
    public static void main(String[] args) {
        Dealer dealer = new DealerImpl();
//        Board board = dealer.dealCardsToPlayers();
//        board = dealer.dealFlop(board);
//        board = dealer.dealTurn(board);
//        board = dealer.dealRiver(board);
        Board board = new Board("10S2S", "10DKH", "4S3S6S", "5S", "AH");
        System.out.println(board);
        System.out.println(dealer.decideWinner(board));
    }

}
