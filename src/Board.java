import java.util.Arrays;

public class Board {
    public static int SIZE = 10;
    private Piece[][] board = new Piece[SIZE][SIZE];

    public void generateBoard() {
        int height = SIZE / 2 - 1; // The starting height for the rows

        for (int i = 0; i < height; i++) {
            for (int j = i % 2; j < SIZE; j += 2) {
                board[i][j] = new Piece(Colors.WHITE);
                board[height + 2 + i][j] = new Piece(Colors.BLACK);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == null) {
                    builder.append(' ');
                } else if (board[i][j].getColor() == Colors.BLACK) {
                    builder.append('b');
                } else {
                    builder.append('w');
                }
            }

            builder.append("\n");
        }

        return builder.toString();
    }
}
