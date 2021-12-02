import java.awt.*;

public class Piece {
    private Colors color;
    private boolean isKing = false;

    public Piece(Colors color) {
        this.color = color;
    }

    public Colors getColor() {
        return color;
    }
}
