package AIs.Hein;

import Game.Board;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Tests {

    byte[][] board = new byte[5][10];
    static Board board2 = new Board();
    MiniMax AI = new MiniMax();
    MiniMax AI2 = new MiniMax((byte) 2);

    @BeforeAll
    static void init() {
        board2.generateBoard();
    }

    @BeforeEach
    void setup() {
        // Create empty board
        for (byte i = 0; i < board.length; i++) {
            for (byte j = 0; j < board[0].length; j++) {
                board[i][j] = 0;
            }
        }
    }

    @Test
    void move() {
        assertDoesNotThrow(() -> AI.move(board2));
        assertDoesNotThrow(() -> AI.move(board2, (byte) 1));
    }

    @Test
    void miniMax() {
        // TODO
    }

    @Test
    void generateChildren() {
        // TODO
    }

    @Test
    void addMoves() {
        // TODO
        byte[][] test = new byte[1][5];
        test[0][4] = 1;
        System.out.println(test.length + " " + test[0].length + " " + test[0][4]);
    }

    @Test
    void hasForcedMoveWhiteDeprecated() { // TODO: add test for king when implemented
        board[0][9] = 3; // White piece in bottom left corner
        assertFalse(MiniMax.hasForcedMove(board));
        board[0][8] = 1; // Black piece diagonally next to white corner piece
        assertTrue(MiniMax.hasForcedMove(board));
        board[0][8] = 0;
        board[1][7] = 1; // Black piece out of reach
        assertFalse(MiniMax.hasForcedMove(board));
    }

    @Test
    void hasForcedMoveWhite() {

    }

    @Test
    void evaluate() { // currently disregards position, only considers material values
        assertEquals(MiniMax.evaluate(board), 0);
        board[0][0] = 3; // add regular white piece
        assertEquals(MiniMax.evaluate(board), 1);
        board[0][1] = 2; // add black king
        assertEquals(MiniMax.evaluate(board), -3);
        board[0][1] = 4; // replace black king with white king
        assertEquals(MiniMax.evaluate(board), 5);
        board[4][9] = 1; // add regular black piece;
        assertEquals(MiniMax.evaluate(board), 4);
    }

    @Test
    void pieceToByteBoard() {
        assertFalse(boardEquals(board, MiniMax.pieceToByteBoard(board2)));
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j ++) {
                board[i][j] = 1;
                board[i][9-j] = 3;
            }
        }
//        MiniMax.drawBoard(MiniMax.pieceToByteBoard(board2));
        assertTrue(boardEquals(board, MiniMax.pieceToByteBoard(board2)));
    }

    boolean boardEquals(byte[][] board1, byte[][] board2) {
        for (byte i = 0; i < board1.length; i++) {
            for (byte j = 0; j < board1[0].length; j++) {
                if (board1[i][j] != board2[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Test
    void drawBoard() {
        assertDoesNotThrow(() -> MiniMax.drawBoard(board));
    }

}
