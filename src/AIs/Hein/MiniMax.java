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
 * TODO: change to fit checkers standards better (e.g. (x,y)==(0,0) at bottom left)
 * TODO: change byte piece representation (make white (1,2) instead of black, or use enum)
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

    public Node miniMax(byte[][] node, byte depth, boolean max) {
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

    public NodeNode miniMax(byte[] node, byte depth, boolean max) {
        if (depth == 0) {
            NodeNode endNode = new NodeNode();
            endNode.value = evaluate(node);
            endNode.board = node;
            return endNode;
        }

        byte[][] children = generateChildren(node);
        int value;
        byte[] select = children[0];
        int newValue;
        if (max) { // max player
            value = Integer.MIN_VALUE;
            for (byte[] child : children) {
                newValue = miniMax(child, depth--, false).value;
                if (newValue > value) {
                    select = child;
                    value = newValue;
                }
            }
        } else { // min player
            value = Integer.MAX_VALUE;
            for (byte[] child : children) {
                newValue = miniMax(child, depth--, false).value;
                if (newValue < value) {
                    select = child;
                    value = newValue;
                }
            }
        }

        NodeNode newNode = new NodeNode();
        newNode.value = value;
        newNode.board = select;
        return newNode;
    }

    public static byte[][][] generateChildren(byte[][] node) {
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

    public static byte[][] generateChildren(byte[] node) {
        ArrayList<byte[]> children = new ArrayList<>();
        for (byte i = 0; i < node.length; i++) {
            if (node[i] > 0) {
                children = addMoves(children, node, i);
            }
        }
        return (byte[][]) children.toArray();
    }

    private static ArrayList<byte[]> addMoves(ArrayList<byte[]> children, byte[] node, byte i) {
        return null; // stub
    }

    public static ArrayList<byte[][]> addMoves(ArrayList<byte[][]> children, byte[][] board, byte x, byte y) {
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
     * @deprecated should use byte[] board representation
     */
    public static boolean hasForcedMove(byte[][] board) {
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

    /**
     * Tells you if the player who is next to move has any forced moves.
     * Board is considered as an array corresponding to the accessible tiles on the board.
     * For instance: on a standard board the top left is 0, top right is 4, bottom left is 45, bottom right is 49
     * TODO: update to work for extended king movement
     * @param board byte array representation of board
     * @param whiteMove true if white is next to move
     * @return (number of forced moves) >= 1
     */
    public static boolean hasForcedMove(byte[] board, boolean whiteMove) {
        byte piece = whiteMove ? (byte) 3 : (byte) 1;
        byte king = whiteMove ? (byte) 4 : (byte) 2;
        byte oppPiece = whiteMove ? (byte) 1 : (byte) 3;
        byte oppKing = whiteMove ? (byte) 2 : (byte) 4;
        byte b = (byte) Math.sqrt((board.length * 2)); // size of the side of a board
        byte h = (byte) (b / 2); // half of the size of the side of a board
        byte c; // correction byte that is 1 on uneven rows
        byte empty = 0;

        for (byte i = 0; i < board.length; i++) {
            if (board[i] == piece || board[i] == king) {
                c = i % b >= h ? (byte) 1 : (byte) 0;
                if (i-b-1 > 0 && i % h > 0 && (board[i-h-c] == oppPiece || board[i+h-1+c] == oppKing)
                        && board[i-b-1] == empty) { // up left
                    return true;
                }
                if (i-b+1 > 0 && i % h < h-1 && (board[i-h+1-c] == oppPiece || board[i+h+c] == oppKing)
                        && board[i-b+1] == empty) { // up right
                    return true;
                }
                if (i+b-1 < board.length-1 && i % h > 0 && (board[i+h-c] == oppPiece || board[i-h-1+c] == oppKing)
                        && board[i+b-1] == empty) { // down left
                    return true;
                }
                if (i+b+1 < board.length-1 && i % h < h-1 && (board[i+h+1-c] == oppPiece || board[i-4-1+c] == oppKing)
                        && board[i+b+1] == empty) { // down right
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * @deprecated should use byte[] board representation
     */
    public static int evaluate(byte[][] board) {
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

    public static int evaluate(byte[] board) {
        int eval = 0;
        for (byte piece : board) {
            switch (piece) {
                case 1:
                    eval--;
                    break;
                case 2:
                    eval -= 4;
                    break;
                case 3:
                    eval++;
                    break;
                case 4:
                    eval += 4;
            }
        }
        return eval;
    }

    public static byte[][] pieceToByteBoard(Board board) {
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

    public static byte[] parseByteBoard(Board board) {
        Piece[][] pieces = board.getPieces();
        byte boardSize = (byte) (pieces.length * pieces.length / 2);
        byte[] newBoard = new byte[boardSize];

        Piece piece;
        byte newPiece;
        byte index;
        for (byte i = 0; i < pieces.length; i++) { // y-axis of pieces array
            for (byte j = 0; j < pieces.length/2; j++) { // x-axis of pieces array
                piece = pieces[i][j*2+(i%2)];
                index = (byte) (i*pieces.length/2 + j);
                if (piece == null) {
                    newPiece = 0;
                } else if (piece.getColor() == Colors.BLACK) {
                    if (piece.isKing()) {
                        newPiece = 2;
                    } else {
                        newPiece = 1;
                    }
                } else {
                    if (piece.isKing()) {
                        newPiece = 4;
                    } else {
                        newPiece = 3;
                    }
                }
                newBoard[index] = newPiece;
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

    public static String boardString(byte[] board) {
        StringBuilder output = new StringBuilder();
        byte boardSize = (byte) (Math.sqrt(board.length * 2) / 2);
        byte check;
        for (byte i = 0; i < board.length; i++) {
            check = (byte) (i % (boardSize * 2));
            if (check == 0) { // start of an even row
                output.append("|    ");
            } else if (check == boardSize) { // start of an uneven row
                output.append("| ");
            }
            int piece = board[i];
            switch (piece) {
                case 0:
                    output.append("-");
                    break;
                case 1:
                    output.append("b");
                    break;
                case 2:
                    output.append("B");
                    break;
                case 3:
                    output.append("w");
                    break;
                case 4:
                    output.append("W");
            }
            if (check == boardSize-1) { // end of an even row
                output.append(" |");
            } else if (check == boardSize * 2 - 1) { // end of an uneven row
                output.append("    |");
            } else {
                output.append("     ");
            }
            if (i % boardSize == boardSize - 1) output.append("\n");
        }
        return output.toString();
    }

    public static void main(String[] args) {
        Board board = new Board();
        board.generateBoard();
        System.out.println(board.toString());
        System.out.println(boardString(parseByteBoard(board)));
    }

}
