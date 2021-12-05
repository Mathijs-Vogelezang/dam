package AIs.Hein;

import Game.Board;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Tests {

    byte[] board = new byte[50];
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
            board[i] = 0;
        }
    }

    @Test
    void move() {
        assertDoesNotThrow(() -> AI.move(board2, true));
        assertDoesNotThrow(() -> AI.move(board2, (byte) 1, true));
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
    }

    @Test
    void hasForcedMoveWhite() { // TODO: add test for king when implemented
        board[45] = 3; // White piece in bottom left corner
        assertFalse(MiniMax.hasForcedMove(board, true));
        board[40] = 1; // Black piece diagonally next to white corner piece
        assertTrue(MiniMax.hasForcedMove(board, true)); // White can take black
        assertFalse(MiniMax.hasForcedMove(board, false)); // Black cannot take white
        board[40] = 0;
        board[36] = 1; // Black piece out of reach
        assertFalse(MiniMax.hasForcedMove(board, true));
    }

    @Test
    void evaluate() { // currently disregards position, only considers material values
        assertEquals(MiniMax.evaluate(board), 0);
        board[0] = 3; // add regular white piece
        assertEquals(MiniMax.evaluate(board), 1);
        board[1] = 2; // add black king
        assertEquals(MiniMax.evaluate(board), -3);
        board[1] = 4; // replace black king with white king
        assertEquals(MiniMax.evaluate(board), 5);
        board[49] = 1; // add regular black piece;
        assertEquals(MiniMax.evaluate(board), 4);
    }

    @Test
    void parseByteBoard() {
        assertFalse(boardEquals(board, MiniMax.parseByteBoard(board2)));
        for (int i = 0; i < 20; i++) {
                board[i] = 3;
                board[i+30] = 1;
        }
//        MiniMax.drawBoard(MiniMax.pieceToByteBoard(board2));
        assertTrue(boardEquals(board, MiniMax.parseByteBoard(board2)));
    }

    boolean boardEquals(byte[] board1, byte[] board2) {
        for (byte i = 0; i < board1.length; i++) {
            if (board1[i] != board2[i]) {
                return false;
            }
        }
        return true;
    }

    @Test
    void boardString() {
        String checkBoardString = "|    w     w     w     w     w |\n" +
                "| w     w     w     w     w    |\n" +
                "|    w     w     w     w     w |\n" +
                "| w     w     w     w     w    |\n" +
                "|    -     -     -     -     - |\n" +
                "| -     -     -     -     -    |\n" +
                "|    b     b     b     b     b |\n" +
                "| b     b     b     b     b    |\n" +
                "|    b     b     b     b     b |\n" +
                "| b     b     b     b     b    |\n";
        for (int i = 0; i < 20; i++) {
            board[i] = 3;
            board[i+30] = 1;
        }
        String boardString = MiniMax.boardString(board);
        assertEquals(checkBoardString, boardString);
    }

}
