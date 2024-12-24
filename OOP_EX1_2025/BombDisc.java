import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BombDisc implements Disc {
    private Player owner;

    public BombDisc(Player owner) {
        this.owner = owner;
    }

    @Override
    public Player getOwner() {
        return owner;
    }

    @Override
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    @Override
    public String getType() {
        return "💣"; // סימן לדיסק פצצה
    }

    /**
     * Handles the explosion logic for a BombDisc.
     *
     * @param position The position of the bomb on the board.
     * @param board    The game board.
     * @param boardSize The size of the board.
     * @return A list of positions flipped due to the bomb's explosion.
     */
    public List<Position> explode(Position position, Disc[][] board, int boardSize) {
        List<Position> flippedPositions = new ArrayList<>();
        Set<Position> visited = new HashSet<>();
        handleExplosion(position, board, boardSize, flippedPositions, visited);
        return flippedPositions;
    }

    /**
     * Recursively handles the explosion of a BombDisc and its impact on adjacent discs.
     *
     * @param position         The current position being handled.
     * @param board            The game board.
     * @param boardSize        The size of the board.
     * @param flippedPositions The list of positions flipped by the explosion.
     * @param visited          A set of visited positions to prevent infinite recursion.
     */
    private void handleExplosion(Position position, Disc[][] board, int boardSize,
                                 List<Position> flippedPositions, Set<Position> visited) {
        if (visited.contains(position)) return; // אם המיקום כבר טופל, נעצור
        visited.add(position);

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;

                int x = position.getCol() + dx;
                int y = position.getRow() + dy;

                if (x >= 0 && x < boardSize && y >= 0 && y < boardSize) {
                    Position adjacentPos = new Position(x, y);
                    Disc adjacentDisc = board[x][y];

                    if (adjacentDisc != null) {
                        // הפוך את הבעלות על הדיסק הסמוך אם הוא לא בלתי הפיך
                        if (!(adjacentDisc instanceof UnflippableDisc)) {
                            adjacentDisc.setOwner(this.owner);
                            flippedPositions.add(adjacentPos);

                            // אם הדיסק הסמוך הוא פצצה, נפעיל את הפיצוץ שלה גם כן
                            if (adjacentDisc instanceof BombDisc) {
                                ((BombDisc) adjacentDisc).handleExplosion(adjacentPos, board, boardSize, flippedPositions, visited);
                            }
                        }
                    }
                }
            }
        }
    }
}
