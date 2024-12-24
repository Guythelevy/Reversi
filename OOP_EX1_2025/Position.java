public class Position {
    private final int col;
    private final int row;


    public Position(int x, int y) {
        this.col = x;
        this.row = y;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    // מתודות נוספות: row() ו-col()
    public int row() {
        return col; // שורה מיוצגת על ידי x
    }

    public int col() {
        return row; // עמודה מיוצגת על ידי y
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return col == position.col && row == position.row;
    }

    @Override
    public int hashCode() {
        return 31 * col + row;
    }

    @Override
    public String toString() {
        return "(" + col + ", " + row + ")";
    }
}
