import org.example.entity.Dealer;
import org.example.entity.Impl.Board;
import org.example.entity.Impl.DealerImpl;
import org.example.enums.PokerResult;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;


public class ComboTest {
    Dealer dealer = new DealerImpl();

    @Test
    void twoPairDRAW1() {
        Board board = new Board("10C10H", "10S10D", "5S5H9H", "AH", "3C");
        PokerResult result = dealer.decideWinner(board);
        System.out.println(result);
        assertEquals(result, PokerResult.DRAW);
    }

    @Test
    void twoPairOneWin() {
        Board board = new Board("7S7D", "6S6H", "5S5H3H", "10S", "2D");
        PokerResult result = dealer.decideWinner(board);
        assertEquals(result, PokerResult.PLAYER_ONE_WIN);
    }

    @Test
    void twoPairTwoWin() {
        Board board = new Board("6S6H", "7S7D", "5S5H3H", "10S", "2D");
        PokerResult result = dealer.decideWinner(board);
        assertEquals(result, PokerResult.PLAYER_TWO_WIN);
    }

    @Test
    void twoPairOnBoardAndKickerHand() {
        Board board = new Board("6H3S", "3D7D", "5S5H10H", "10D", "2C");
        PokerResult result = dealer.decideWinner(board);
        System.out.println(result);
        assertEquals(result, PokerResult.PLAYER_TWO_WIN);
    }

    @Test
    void twoPairOnBoardAndKickerHand2() {
        Board board = new Board("JS9H", "9D8S", "5S5H10H", "10D", "2C");
        PokerResult result = dealer.decideWinner(board);
        System.out.println(result);
        assertEquals(result, PokerResult.PLAYER_ONE_WIN);
    }

    @Test
    void twoPairOnBoardAndKickerHand3() {
        Board board = new Board("9D8S", "JS9H", "5S5H10H", "10D", "2C");
        PokerResult result = dealer.decideWinner(board);
        System.out.println(result);
        assertEquals(result, PokerResult.PLAYER_TWO_WIN);
    }

    @Test
    void twoPairOnBoardAndKickerHand4() {
        Board board = new Board("AHAS", "JS9H", "5S5H10H", "10D", "2C");
        PokerResult result = dealer.decideWinner(board);
        System.out.println(result);
        assertEquals(result, PokerResult.PLAYER_ONE_WIN);
    }

    @Test
    void twoPairOnBoardAndKickerHand5() {
        Board board = new Board("JS9H", "AHAS", "5S5H10H", "10D", "2C");
        PokerResult result = dealer.decideWinner(board);
        System.out.println(result);
        assertEquals(result, PokerResult.PLAYER_TWO_WIN);
    }

    @Test
    void twoPairOnBoardAndKickerHand6() {
        Board board = new Board("ADAC", "AHAS", "5S5H10H", "10D", "2C");
        PokerResult result = dealer.decideWinner(board);
        System.out.println(result);
        assertEquals(result, PokerResult.DRAW);
    }

    //Пары
    @Test
    void PairOneWin() {
        Board board = new Board("ADAC", "KSKH", "3S5C8D", "10D", "2C");
        PokerResult result = dealer.decideWinner(board);
        System.out.println(result);
        assertEquals(result, PokerResult.PLAYER_ONE_WIN);
    }
    @Test
    void PairTwoWin() {
        Board board = new Board("KSKH", "ADAC", "3S5C8D", "10D", "2C");
        PokerResult result = dealer.decideWinner(board);
        System.out.println(result);
        assertEquals(result, PokerResult.PLAYER_TWO_WIN);
    }

    @Test
    void PairDraw() {
        Board board = new Board("3H5S", "3C5D", "ADAC8D", "10D", "2C");
        PokerResult result = dealer.decideWinner(board);
        System.out.println(result);
        assertEquals(result, PokerResult.DRAW);
    }
    @Test
    void PairDrawKickerOneWin() {
        Board board = new Board("3HJS", "3C5D", "ADAC8D", "10D", "2C");
        PokerResult result = dealer.decideWinner(board);
        System.out.println(result);
        assertEquals(result, PokerResult.PLAYER_ONE_WIN);
    }

    @Test
    void PairDrawKickerTwoWin() {
        Board board = new Board("3HJS", "3CQD", "10H10D8D", "5C", "2C");
        PokerResult result = dealer.decideWinner(board);
        System.out.println(result);
        assertEquals(result, PokerResult.PLAYER_TWO_WIN);
    }
    @Test
    void PairDrawKickerDRAW() {
        Board board = new Board("9DAS", "8DAH", "10H10D4D", "JD", "QC");
        PokerResult result = dealer.decideWinner(board);
        System.out.println(result);
        assertEquals(result, PokerResult.DRAW);
    }


    @Test
    void PairDrawKickerDRAW2() {
        Board board = new Board("3HJS", "3CJH", "9C5H8D", "JD", "2C");
        PokerResult result = dealer.decideWinner(board);
        System.out.println(result);
        assertEquals(result, PokerResult.DRAW);
    }

    @Test
    void PairDrawKickerOneWin2() {
        Board board = new Board("9HJS", "8CJH", "3C5H2H", "JD", "AC");
        PokerResult result = dealer.decideWinner(board);
        System.out.println(result);
        assertEquals(result, PokerResult.PLAYER_ONE_WIN);
    }
    @Test
    void PairDrawKickerTwoWin2() {
        Board board = new Board("8HJS", "9CJH", "3C5HQH", "JD", "AC");
        PokerResult result = dealer.decideWinner(board);
        System.out.println(result);
        assertEquals(result, PokerResult.PLAYER_TWO_WIN);
    }

    @Test
    void PairDrawKickerDRAW3() {
        Board board = new Board("QSJS", "QHJH", "3C5H2H", "JD", "AC");
        PokerResult result = dealer.decideWinner(board);
        System.out.println(result);
        assertEquals(result, PokerResult.DRAW);
    }


    //Старшая карта
    @Test
    void ElderCardOneWin() {
        Board board = new Board("QSAS", "QHJH", "3C5H2H", "7D", "9C");
        PokerResult result = dealer.decideWinner(board);
        System.out.println(result);
        assertEquals(result, PokerResult.PLAYER_ONE_WIN);
    }
    @Test
    void ElderCardTwoWin() {
        Board board = new Board("QSJS", "QHAH", "3C5H2H", "7D", "9C");
        PokerResult result = dealer.decideWinner(board);
        System.out.println(result);
        assertEquals(result, PokerResult.PLAYER_TWO_WIN);
    }
    @Test
    void ElderCardDRAW() {
        Board board = new Board("QSAS", "QHAH", "3C5H2H", "7D", "9C");
        PokerResult result = dealer.decideWinner(board);
        System.out.println(result);
        assertEquals(result, PokerResult.DRAW);
    }

    @Test
    void ElderCardOneWin1() {
        Board board = new Board("10D2H", "8H2C", "QSKSAH", "7D", "9C");
        PokerResult result = dealer.decideWinner(board);
        System.out.println(result);
        assertEquals(result, PokerResult.PLAYER_ONE_WIN);
    }
    @Test
    void ElderCardTwoWin1() {
        Board board = new Board("8H2C", "10D2H", "QSKSAH", "7D", "9C");
        PokerResult result = dealer.decideWinner(board);
        System.out.println(result);
        assertEquals(result, PokerResult.PLAYER_TWO_WIN);
    }
    @Test
    void ElderCardDRAW1() {
        Board board = new Board("4D5D", "5H4H", "QSKSAH", "7D", "9C");
        PokerResult result = dealer.decideWinner(board);
        System.out.println(result);
        assertEquals(result, PokerResult.DRAW);
    }



}