package Game;

public class Piece {
    private Colors color;
    private boolean isKing = false;

    public Piece(Colors color) {
        this.color = color;
    }

    public Colors getColor() {
        return color;
    }

    public boolean isKing() {
        return isKing;
    }

    public void isKing(boolean isKing) {
        this.isKing = isKing;
    }
}
