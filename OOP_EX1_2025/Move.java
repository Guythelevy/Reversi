import java.util.ArrayList;
import java.util.List;

public class Move {
    private final Position position; // המיקום של המהלך
    private final Disc disc; // הדיסק שהונח
    private List<Position> flippedDiscs; // רשימת דיסקים שהתהפכו במהלך

    public Move(Position position, Disc disc) {
        this.position = position;
        this.disc = disc;
        this.flippedDiscs = new ArrayList<>();
    }

    // מחזירה את המיקום של המהלך
    public Position getPosition() {
        return position;
    }

    // מחזירה את הדיסק שהונח
    public Disc getDisc() {
        return disc;
    }

    // מתודה בשם position() שנדרשת על ידי ה-GUI
    public Position position() {
        return position;
    }

    // מתודה בשם disc() שנדרשת על ידי ה-GUI
    public Disc disc() {
        return disc;
    }

    // מחזירה את רשימת הדיסקים שהתהפכו
    public List<Position> getFlippedDiscs() {
        return flippedDiscs;
    }

    // מגדירה את רשימת הדיסקים שהתהפכו
    public void setFlippedDiscs(List<Position> flippedDiscs) {
        this.flippedDiscs = flippedDiscs;
    }

    // בדיקת חוקיות המהלך
    public boolean isValid(Disc[][] board, int boardSize) {
        // בדיקה אם המשבצת פנויה
        if (board[position.getCol()][position.getRow()] != null) {
            return false;
        }

        // בדיקה אם יש לפחות דיסק אחד שאפשר להפוך
        return countFlips(board, boardSize) > 0;
    }


    private int countFlips(Disc[][] board, int boardSize) {
        int totalFlips = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                totalFlips += countFlipsInDirection(board, boardSize, dx, dy);
            }
        }
        return totalFlips;
    }


    private int countFlipsInDirection(Disc[][] board, int boardSize, int dx, int dy) {
        int x = position.getCol() + dx;
        int y = position.getRow() + dy;
        int flips = 0;

        while (x >= 0 && x < boardSize && y >= 0 && y < boardSize) {
            Disc currentDisc = board[x][y];
            if (currentDisc == null) {
                return 0;
            } else if (currentDisc.getOwner() == disc.getOwner()) {
                return flips;
            } else {
                flips++;
            }
            x += dx;
            y += dy;
        }
        return 0;
    }

    /**
     * Places a disc on the board.
     *
     * @param board The game board.
     */
    public void placeDisc(Disc[][] board) {
        if (disc == null || disc.getOwner() == null) {
            throw new IllegalArgumentException("Disc or owner cannot be null at position: " + position);
        }
        board[position.getCol()][position.getRow()] = disc;
    }

    /**
     * Flips discs affected by this move.
     *
     * @param board     The game board.
     * @param boardSize The size of the board.
     * @return A list of positions that were flipped.
     */
    public List<Position> flipDiscs(Disc[][] board, int boardSize) {
        List<Position> flippedPositions = new ArrayList<>();

        if (disc instanceof BombDisc) {
            flippedPositions.addAll(((BombDisc) disc).explode(position, board, boardSize));
        } else {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue;
                    flippedPositions.addAll(flipInDirection(board, boardSize, dx, dy));
                }
            }
        }
        return flippedPositions;
    }

    /**
     * Flips discs in a specific direction.
     *
     * @param board     The game board.
     * @param boardSize The size of the board.
     * @param dx        Direction along the X-axis.
     * @param dy        Direction along the Y-axis.
     * @return A list of positions flipped in this direction.
     */
    private List<Position> flipInDirection(Disc[][] board, int boardSize, int dx, int dy) {
        int x = position.getCol() + dx;
        int y = position.getRow() + dy;
        List<Position> toFlip = new ArrayList<>();

        while (x >= 0 && x < boardSize && y >= 0 && y < boardSize) {
            Disc currentDisc = board[x][y];

            if (currentDisc == null || currentDisc.getOwner() == null) {
                return new ArrayList<>();
            } else if (currentDisc.getOwner() == disc.getOwner()) {
                for (Position pos : toFlip) {
                    Disc toFlipDisc = board[pos.getCol()][pos.getRow()];
                    if (!(toFlipDisc instanceof UnflippableDisc) || ((UnflippableDisc) toFlipDisc).canBeFlipped()) {
                        toFlipDisc.setOwner(disc.getOwner());
                    }
                }
                return toFlip;
            } else {
                toFlip.add(new Position(x, y));
            }

            x += dx;
            y += dy;
        }
        return new ArrayList<>();
    }

}
