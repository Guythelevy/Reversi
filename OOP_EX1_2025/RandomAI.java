import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomAI extends AIPlayer {
    private final Random random = new Random();

    public RandomAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        List<Position> validMoves = gameStatus.ValidMoves();
        if (validMoves.isEmpty()) {
            return null; // אין מהלכים חוקיים
        }

        // בחירת מהלך בהתפלגות נורמלית
        Position randomPosition = selectPositionWithNormalDistribution(validMoves);

        // בחירת דיסקית רנדומלית
        Disc randomDisc = selectRandomDisc();
        if (randomDisc == null) {
            return null; // אין דיסקיות זמינות
        }

        return new Move(randomPosition, randomDisc);
    }

    private Position selectPositionWithNormalDistribution(List<Position> validMoves) {
        int size = validMoves.size();

        // בחירת אינדקס בהתפלגות נורמלית
        double gaussian = random.nextGaussian();
        double scaledGaussian = (gaussian - Math.floor(gaussian)) * size; // התפלגות בין 0 ל-size
        int index = Math.abs((int) scaledGaussian % size); // תיקון לטווח החוקי

        return validMoves.get(index);
    }

    private Disc selectRandomDisc() {
        // יצירת רשימה של דיסקיות זמינות
        List<Disc> possibleDiscs = new ArrayList<>();

        if (getNumber_of_bombs() > 0) {
            possibleDiscs.add(new BombDisc(this));
        }
        if (getNumber_of_unflippedable() > 0) {
            possibleDiscs.add(new UnflippableDisc(this));
        }
        possibleDiscs.add(new SimpleDisc(this)); // תמיד ניתן להוסיף דיסקית רגילה

        // בחירת דיסקית רנדומלית אם יש אפשרויות
        if (!possibleDiscs.isEmpty()) {
            return possibleDiscs.get(random.nextInt(possibleDiscs.size()));
        }

        return null; // אין דיסקיות זמינות
    }
}
