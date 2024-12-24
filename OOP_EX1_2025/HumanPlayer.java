public class HumanPlayer extends Player {

    public HumanPlayer(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    @Override
    public boolean isHuman() {
        return true; // תמיד מחזיר true עבור שחקן אנושי
    }
}
