import java.util.List;

public class GreedyAI extends AIPlayer {

    public GreedyAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        List<Position> validMoves = gameStatus.ValidMoves();
        if (validMoves.isEmpty()) {
            return null; // אין מהלכים חוקיים
        }

        Position bestPosition = null;
        int maxFlips = -1;

        // עבור על כל המהלכים החוקיים וחפש את המהלך עם מספר ההפיכות המרבי
        for (Position position : validMoves) {
            int flips = gameStatus.countFlips(position);
            if (flips > maxFlips) {
                maxFlips = flips;
                bestPosition = position;
            }
        }

        if (bestPosition == null) {
            return null; // אין מהלך מתאים
        }

        Disc greedyDisc = new SimpleDisc(this); // דיסק רגיל
        return new Move(bestPosition, greedyDisc);
    }
}
