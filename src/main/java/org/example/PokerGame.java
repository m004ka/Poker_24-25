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
        Board board = new Board( "10C7D", "8D9C", "2H3S4H", "5C", "6S");
        System.out.println(board);
        System.out.println(dealer.decideWinner(board));
    }

}
