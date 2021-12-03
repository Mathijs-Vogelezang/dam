package AIs.Hein;

import Game.Board;
import Game.Colors;
import Game.Piece;

import java.util.ArrayList;

/**
 * MiniMax assumes that the board is set up in a specific way, although it handles some conversion if necessary.
 * It assumes the board is a square board of size N by N, where (4 <= N <= 124), (N % 2 == 0). (Standard is 10x10).
 * In the code this actually becomes a board of (N/2) by N, since the empty tiles are left out.
 * The bottom right corner is always a "light" tile, and no pieces ever lie on a light tile.
 * Therefore pieces on a normal board always lie on coordinates (x,y) where ((x % 2) + (y % 2) == 1) [e.g. (0,1) (1,4)],
 * while in code they lie on coordinates (x,y) where (0 <= x <= y/2).
 * x = 0 on the left of a board, and y=0 at the top.
 * For now white tiles are always considered starting on the bottom of the board, and black tiles on the top.
 * TODO: add more perspectives
 */
public class MiniMax {

    private byte standardDepth = 1;

    public MiniMax() {}

    public MiniMax(byte standardDepth) {
        this.standardDepth = standardDepth;
    }

    public int move(Board board) {
        return move(board, standardDepth);
    }

    public int move(Board board, byte depth) {
        byte[][] origin = pieceToByteBoard(board);
        return miniMax(origin, depth, true).value;
    }

    protected Node miniMax(byte[][] node, byte depth, boolean max) {
        if (depth == 0) {
            Node endNode = new Node();
            endNode.value = evaluate(node);
            endNode.board = node;
            return endNode;
        }

        byte[][][] children = generateChildren(node);
        int value;
        byte[][] select = children[0];
        int newValue;
        if (max) { // max player
            value = Integer.MIN_VALUE;
            for (byte[][] child : children) {
                newValue = miniMax(child, depth--, false).value;
                if (newValue > value) {
                    select = child;
                    value = newValue;
                }
            }
        } else { // min player
            value = Integer.MAX_VALUE;
            for (byte[][] child : children) {
                newValue = miniMax(child, depth--, false).value;
                if (newValue < value) {
                    select = child;
                    value = newValue;
                }
            }
        }
        Node newNode = new Node();
        newNode.value = value;
        newNode.board = select;
        return newNode;
    }

    protected static byte[][][] generateChildren(byte[][] node) {
        ArrayList<byte[][]> children = new ArrayList<>();
        for (byte i = 0; i < node.length; i++) {
            for (byte j = 0; j < node[0].length; j++) {
                if (node[i][j] > 0) {
                    addMoves(children, node, i, j);
                }
            }
        }
        return (byte[][][]) children.toArray();
    }

    protected static ArrayList<byte[][]> addMoves(ArrayList<byte[][]> children, byte[][] board, byte x, byte y) {
        if (hasForcedMove(board)) {

        } else {

        }

        // TODO
        // forced moves
        // double hop and further
        // normal moves
        //

        // evaluate and store all possible moves with the piece at this position (recursively),
        // then return the list of those moves
        return children; // stub, improve this
    }

    /**
     * For now white is always considered to be the one to move, and white is considered to start at the bottom
     * TODO: add more perspectives
     */
    protected static boolean hasForcedMove(byte[][] board) {
        for (byte i = 0; i < board.length; i++) { // index i is the x-axis (left to right)
            for (byte j = 0; j < board[0].length; j++) { // index j is the y-axis (top to bottom)
                byte piece = board[i][j];
                if (piece == 3 || piece == 4) { // TODO: add extra king moves
                    // the following if-statements only trigger if a piece is at least 2 regular moves away from the
                    // edge, since that would be required for a forced move
                    byte otherPiece;
                    if (i > 1 || (j % 2 == 0 && i > 0)) { // can move up left or down left
                        if (j > 1) { // can move up left
                            otherPiece = board[i-j%2][j-1]; // j%2 corrects the bias of removing all light squares
                            if ((otherPiece == 1 || otherPiece == 2) && board[i-2][j-2] == 0) {
                                return true;
                            }
                        }
                        if (j < board.length-2) { // can move down left
                            otherPiece = board[i-j%2][j+1];
                            if ((otherPiece == 1 || otherPiece == 2) && board[i-2][j+2] == 0) {
                                return true;
                            }
                        }
                    }
                    if (i < board.length-2 || (j % 2 == 1 && i < board.length-1)) { // can move up right or down right
                        if (j > 1) { // can move up right
                            otherPiece = board[i+(j+1)%2][j-1]; // (j+1)%2 corrects the bias of removing all light squares
                            if ((otherPiece == 1 || otherPiece == 2) && board[i+2][j-2] == 0) {
                                return true;
                            }
                        }
                        if (j < board.length-2) { // can move down right
                            otherPiece = board[i+(j+1)%2][j+1];
                            if ((otherPiece == 1 || otherPiece == 2) && board[i+2][j+2] == 0) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    protected static int evaluate(byte[][] board) {
        int eval = 0;
        for (byte[] row : board) {
            for (byte col : row) {
                if (col == 1) { // black piece
                    eval--;
                } else if (col == 2) { // black king
                    eval -= 4;
                } else if (col == 3) { // white piece
                    eval++;
                } else if (col == 4) { // white king
                    eval += 4;
                }
            }
        }
        return eval;
    }

    protected static byte[][] pieceToByteBoard(Board board) {
        Piece[][] pieces = board.getPieces();
        byte[][] newBoard = new byte[Board.SIZE/2][Board.SIZE];
        Piece piece;
        byte bytePiece;
        for (byte i = 0; i < pieces.length; i++) {
            for (byte j = (byte) (i % 2); j < Board.SIZE; j+=2) {
                piece = pieces[i][j]; // piece[i][j] = (length-y,x)
                if (piece == null) {
                    bytePiece = 0;
                } else if (piece.getColor() == Colors.BLACK) {
                    if (piece.isKing()) {
                        bytePiece = 2;
                    } else {
                        bytePiece = 1;
                    }
                } else {
                    if (piece.isKing()) {
                        bytePiece = 4;
                    } else {
                        bytePiece = 3;
                    }
                }
                newBoard[(j-i%2)/2][newBoard[0].length-1-i] = bytePiece;
            }
        }
        return newBoard;
    }

    public static void drawBoard(byte[][] board) {
        for (byte i = 0; i < board[0].length; i++) {
            for (byte[] bytes : board) {
                System.out.print(bytes[i] + " ");
            }
            System.out.println();
        }
    }

}
