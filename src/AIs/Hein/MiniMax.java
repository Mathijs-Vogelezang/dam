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
 * TODO: change byte piece representation (make white (1,2) instead of black, or use enum)
 */
public class MiniMax {

    private byte standardDepth = 1;

    public MiniMax() {}

    public MiniMax(byte standardDepth) {
        this.standardDepth = standardDepth;
    }

    public int move(Board board, boolean whiteMove) {
        return move(board, standardDepth, whiteMove);
    }

    public int move(Board board, byte depth, boolean whiteMove) {
        byte[] origin = parseByteBoard(board);
        return miniMax(origin, depth, true, whiteMove).value;
    }

    /**
     * Calculates the best move to be made if the opponent plays optimally, up to a certain depth
     * @param node current board position
     * @param depth maximum depth to calculate to
     * @param max switch to differentiate between black and white playing optimally
     * @return
     */
    public Node miniMax(byte[] node, byte depth, boolean max, boolean whiteMove) {
        if (depth == 0) {
            Node endNode = new Node();
            endNode.value = evaluate(node);
            endNode.board = node;
            return endNode;
        }

        byte[][] children = generateChildren(node, whiteMove);
        int value;
        byte[] select = children[0];
        int newValue;
        if (max) { // max player
            value = Integer.MIN_VALUE;
            for (byte[] child : children) {
                newValue = miniMax(child, depth--, false, whiteMove).value;
                if (newValue > value) {
                    select = child;
                    value = newValue;
                }
            }
        } else { // min player
            value = Integer.MAX_VALUE;
            for (byte[] child : children) {
                newValue = miniMax(child, depth--, false, whiteMove).value;
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

    /**
     * Generates all the children nodes belonging to a node, meaning all reachable board states
     * @param node current node
     * @return children of node
     */
    public static byte[][] generateChildren(byte[] node, boolean whiteMove) {
        ArrayList<byte[]> children = new ArrayList<>();
        for (byte i = 0; i < node.length; i++) {
            if (node[i] > 0) {
                children = addMoves(node, whiteMove, i);
            }
        }
        return (byte[][]) children.toArray();
    }

    /**
     * Creates a list of all possible moves to be made in a position with a piece. Also considers possible forced moves.
     * @param node current board state
     * @param whiteMove white is to move
     * @param index position of piece to be considered
     * @return the list of currently reachable board states with this piece
     */
    private static ArrayList<byte[]> addMoves(byte[] node, boolean whiteMove, int index) {
        if (hasForcedMove(node, whiteMove)) {
            //
        } else {
            //
        }
        // TODO:
        // double hop and further
        // normal moves
        //
        // evaluate and store all possible moves with the piece at this position (recursively),
        // then return the list of those moves
        return null; // stub
    }

    /**
     * Tells you if the player who is next to move has any forced moves.
     * Board is considered as an array corresponding to the accessible tiles on the board.
     * For instance: on a standard board the top left is 0, top right is 4, bottom left is 45, bottom right is 49
     * @param board byte array representation of board
     * @param whiteMove true if white is next to move
     * @return (number of forced moves) >= 1
     */
    public static boolean hasForcedMove(byte[] board, boolean whiteMove) {
        byte piece = (byte) (whiteMove ? 3 : 1);
        byte king = (byte) (whiteMove ? 4 : 2);
        byte oppPiece = (byte) (whiteMove ? 1 : 3);
        byte oppKing = (byte) (whiteMove ? 2 : 4);
        byte b = (byte) Math.sqrt((board.length * 2)); // size of the side of a board
        byte h = (byte) (b / 2); // half of the size of the side of a board
        byte c; // correction byte
        byte empty = 0;

        for (byte i = 0; i < board.length; i++) {
            if (board[i] == piece || board[i] == king) {
                c = i % b >= h ? (byte) 1 : (byte) 0; // (c == 1) on uneven rows
                if (i-b-1 > 0 && i % h > 0 && ((board[i-h-c] == oppPiece || board[i-h-c] == oppKing) && board[i-b-1] == empty)
                        || (board[i] == king && kingCanCapture(board, i, whiteMove, (byte) 0))) {
                    return true; // up left
                }
                if (i-b+1 > 0 && i % h < h-1 && ((board[i-h+1-c] == oppPiece || board[i-h+1-c] == oppKing) && board[i-b+1] == empty)
                        || (board[i] == king && kingCanCapture(board, i, whiteMove, (byte) 1))) {
                    return true; // up right
                }
                if (i+b-1 < board.length-1 && i % h > 0 && ((board[i+h-c] == oppPiece || board[i+h-c] == oppKing) && board[i+b-1] == empty)
                        || (board[i] == king && kingCanCapture(board, i, whiteMove, (byte) 3))) {
                    return true; // down left
                }
                if (i+b+1 < board.length-1 && i % h < h-1 && ((board[i+h+1-c] == oppPiece || board[i+h+1-c] == oppKing) && board[i+b+1] == empty)
                        || (board[i] == king && kingCanCapture(board, i, whiteMove, (byte) 2))) {
                    return true; // down right
                }
            }
        }

        return false;
    }

    /**
     * Checks if a king given at position i on board has any possible captures in a direction.
     * It stores all the fields in a certain direction in an array, and checks if a capture can be made in that array.
     * @param board board
     * @param i kings position
     * @param whiteMove white is to move
     * @param direction direction (0 up left, 1 up right, 2 down right, 3 down left)
     * @return king can capture
     */
    public static boolean kingCanCapture(byte[] board, byte i, boolean whiteMove, byte direction) {
        ArrayList<Byte> array = new ArrayList<>();
        byte b = (byte) Math.sqrt(board.length * 2); // boardSize
        byte h = (byte) (b / 2); // half boardSize
        switch (direction) {
            case 0:
                while (true) { // up left
                    i = (byte) (i % b >= h ? i - h - 1 : i - h);
                    if (i < 0 || i % h == h - 1) { // check bounds
                        break;
                    }
                    array.add(board[i]);
                }
                break;
            case 1:
                while (true) { // up right
                    i = (byte) (i % b >= h ? i - h : i - h + 1);
                    if (i < 0 || i % h == 0) { // check bounds
                        break;
                    }
                    array.add(board[i]);
                }
                break;
            case 2:
                while (true) { // down right
                    i = (byte) (i % b >= h ? i + h : i + h + 1);
                    if (i >= board.length || i % h == 0) { // check bounds
                        break;
                    }
                    array.add(board[i]);
                }
                break;
            case 3:
                while (true) { // down left
                    i = (byte) (i % b >= h ? i + h - 1 : i + h);
                    if (i >= board.length || i % h == h - 1) { // check bounds
                        break;
                    }
                    array.add(board[i]);
                }
        }
        byte piece;
        for (i = 0; i < array.size(); i++) {
            piece = array.get(i);
            if (i >= array.size() - 1) break;
            if (whiteMove && (piece == 1 || piece == 2) && array.get(i+1) == 0) {
                return true;
            } else if ((piece == 3 || piece == 4) && array.get(i+1) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gives a very basic evaluation of a board state.
     * Kings count as 4, normal pieces as 1, wins count as max integer values.
     * @param board board state
     * @return board evaluation int
     */
    public static int evaluate(byte[] board) {
        int eval = 0;
        byte blackPieces = 0;
        byte whitePieces = 0;
        for (byte piece : board) {
            switch (piece) {
                case 1:
                    eval--;
                    blackPieces++;
                    break;
                case 2:
                    eval -= 4;
                    blackPieces++;
                    break;
                case 3:
                    eval++;
                    whitePieces++;
                    break;
                case 4:
                    whitePieces++;
                    eval += 4;
            }
        }
        if (whitePieces == 0 && blackPieces > 0) {
            return Integer.MIN_VALUE;
        } else if (blackPieces == 0 && whitePieces > 0) {
            return Integer.MAX_VALUE;
        }
        return eval;
    }

    /**
     * Converts the Pieces[][] field in a Board object to a byte[] representation
     * @param board Board object to be converted
     * @return board converted to byte[]
     */
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

    /**
     * Creates a somewhat formatted representation of a given board
     * @param board board
     * @return formatted String representation of board
     */
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
