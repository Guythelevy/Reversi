import java.util.*;

public class GameLogic implements PlayableLogic {
    private GUI_for_chess_like_games gui;

    private static final int BOARD_SIZE = 8;
    private Disc[][] board;
    private Player player1, player2;
    private Player currentPlayer;
    private boolean gameFinished;
    private Stack<Move> moveHistory;


    public GameLogic() {
        board = new Disc[BOARD_SIZE][BOARD_SIZE];
        moveHistory = new Stack<>();
        this.player1 = new HumanPlayer(true);
        this.player2 = new RandomAI(false);
        reset();
    }

       @Override
    public boolean locate_disc(Position position, Disc disc) {
        if (disc instanceof BombDisc && currentPlayer.getNumber_of_bombs() <= 0) {
            System.out.println("Player " + (currentPlayer.isPlayerOne() ? "1" : "2") + " has no bomb discs remaining!");
            return false;
        }
        if (disc instanceof UnflippableDisc && currentPlayer.getNumber_of_unflippedable() <= 0) {
            System.out.println("Player " + (currentPlayer.isPlayerOne() ? "1" : "2") + " has no unflippable discs remaining!");
            return false;
        }

        Move move = new Move(position, disc);
        if (move.isValid(board, BOARD_SIZE)) {
            move.placeDisc(board);
            List<Position> flippedPositions = move.flipDiscs(board, BOARD_SIZE);
            move.setFlippedDiscs(flippedPositions);
            moveHistory.push(move);

            if (disc instanceof BombDisc) {
                currentPlayer.reduce_bomb();
            } else if (disc instanceof UnflippableDisc) {
                currentPlayer.reduce_unflippedable();
            }

            logMove(currentPlayer, disc, position);
            logFlippedDiscs(currentPlayer, flippedPositions);

            switchPlayer();


            return true;
        }
        return false;
    }



    @Override
    public Disc getDiscAtPosition(Position position) {
        return board[position.getCol()][position.getRow()];
    }

    @Override
    public int getBoardSize() {
        return BOARD_SIZE;
    }

    @Override
    public List<Position> ValidMoves() {
        List<Position> validMoves = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Position position = new Position(i, j);
                Move move = new Move(position, new SimpleDisc(currentPlayer));
                if (move.isValid(board, BOARD_SIZE)) {
                    validMoves.add(position);
                }
            }
        }
        return validMoves;
    }

    @Override
    public int countFlips(Position position) {
        int totalFlips = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                totalFlips += countFlipsInDirection(position, dx, dy);
            }
        }
        return totalFlips;
    }

    @Override
    public Player getFirstPlayer() {
        return player1;
    }

    @Override
    public Player getSecondPlayer() {
        return player2;
    }

    @Override
    public void setPlayers(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
    }

    @Override
    public boolean isFirstPlayerTurn() {
        return currentPlayer == player1;
    }

    @Override
    public boolean isGameFinished() {
        if (ValidMoves().isEmpty()) {  // אם אין מהלכים חוקיים לשחקן הנוכחי
            gameFinished = true;

            // חישוב כמות הדיסקים של כל שחקן
            int[] counts = countPlayerDiscs();
            int player1Count = counts[0];
            int player2Count = counts[1];

            if (player1Count > player2Count) {
                player1.addWin();
                logWinner(player1, player1Count, player2, player2Count);
            } else if (player2Count > player1Count) {
                player2.addWin();
                logWinner(player2, player2Count, player1, player1Count); // העברת player2 במקום player1
            } else {
                System.out.println();
                System.out.println("It's a tie! Both players have " + player1Count + " discs.");
                System.out.println();
            }

            // עדכון הניצחונות ב-GUI
            if (gui != null) {
                gui.updateWinsLabels(player1.getWins(), player2.getWins());
            }
        }
        return gameFinished;
    }




    @Override
    public void reset() {

        board = new Disc[BOARD_SIZE][BOARD_SIZE];
        gameFinished = false;
        moveHistory.clear();

        if (player1 == null || player2 == null) {
            throw new IllegalStateException("Players must be initialized before resetting the board.");
        }


        player1.reset_bombs_and_unflippedable();
        player2.reset_bombs_and_unflippedable();


        board[3][3] = new SimpleDisc(player1);
        board[3][4] = new SimpleDisc(player2);
        board[4][3] = new SimpleDisc(player2);
        board[4][4] = new SimpleDisc(player1);
        currentPlayer = player1;
    }

    @Override
    public void undoLastMove() {
        // בדיקה אם אחד השחקנים הוא AI
        if (player1 instanceof AIPlayer || player2 instanceof AIPlayer) {
            System.out.println("Undo is not allowed when one of the players is an AI.");
            System.out.println(); // שורה ריקה בסוף
            return;
        }

        // אם היסטוריית המהלכים ריקה, לא ניתן להחזיר מהלך
        if (moveHistory.isEmpty()) {
            System.out.println("\tNo previous move available to undo.");
            System.out.println(); // שורה ריקה בסוף
            return;
        }

        // שליפת המהלך האחרון
        Move lastMove = moveHistory.pop();
        Position lastPosition = lastMove.getPosition();
        Disc lastDisc = lastMove.getDisc();

        // הסרת הדיסק מהלוח
        board[lastPosition.getCol()][lastPosition.getRow()] = null;

        // שחזור הדיסקים שהתהפכו
        List<Position> flippedPositions = lastMove.getFlippedDiscs();
        for (Position pos : flippedPositions) {
            Disc flippedDisc = board[pos.getCol()][pos.getRow()];
            if (flippedDisc != null) {
                flippedDisc.setOwner(flippedDisc.getOwner().isPlayerOne() ? player2 : player1);
            }
        }

        // עדכון ספירת הדיסקים המיוחדים במקרה של Undo
        if (lastDisc instanceof BombDisc) {
            lastDisc.getOwner().number_of_bombs++;
        } else if (lastDisc instanceof UnflippableDisc) {
            lastDisc.getOwner().number_of_unflippedable++;
        }

        // רישום הפעולה
        logUndoMove(lastMove, flippedPositions);

        // החלפת תור השחקנים
        switchPlayer();

        // הוספת שורה ריקה בסוף הפעולה
        System.out.println();
    }

    private int countFlipsInDirection(Position position, int dx, int dy) {
        int x = position.getCol() + dx;
        int y = position.getRow() + dy;
        int flips = 0;

        while (x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE) {
            Disc currentDisc = board[x][y];

            // אם המשבצת ריקה או הדיסק לא שייך לאף אחד, עצור
            if (currentDisc == null || currentDisc.getOwner() == null) {
                return 0;
            }

            // אם הגענו לדיסק של השחקן הנוכחי, החזר את מספר הדיסקים שנספרו
            if (currentDisc.getOwner() == currentPlayer) {
                return flips;
            }

            // אם זה דיסק מסוג UnflippableDisc, דלג עליו
            if (currentDisc instanceof UnflippableDisc) {
                x += dx;
                y += dy;
                continue; // המשך לבדוק את המיקום הבא
            }

            // אם הדיסק שייך ליריב, ספר אותו
            flips++;
            x += dx;
            y += dy;
        }

        // אם לא הגענו לדיסק של השחקן הנוכחי, החזר 0
        return 0;
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }




    private int[] countPlayerDiscs() {
        int[] counts = new int[2]; // counts[0] = player1, counts[1] = player2
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] != null) {
                    if (board[i][j].getOwner() == player1) {
                        counts[0]++;
                    } else if (board[i][j].getOwner() == player2) {
                        counts[1]++;
                    }
                }
            }
        }
        return counts;
    }
    private void logWinner(Player winner, int winnerCount, Player loser, int loserCount) {
        System.out.println();
        System.out.println("Player " + (winner.isPlayerOne() ? "1" : "2") +
                " wins with " + winnerCount + " discs! Player " +
                (loser.isPlayerOne() ? "1" : "2") + " had " + loserCount + " discs.");
        System.out.println();
    }

    private void logMove(Player player, Disc disc, Position position) {
        System.out.println();
        System.out.println("Player " + (player.isPlayerOne() ? "1" : "2") +
                " placed a " + disc.getType() + " in (" + position.getCol() + ", " + position.getRow() + ")");
        System.out.println();
    }

    private void logFlippedDiscs(Player player, List<Position> flippedPositions) {
        for (Position pos : flippedPositions) {
            Disc flippedDisc = board[pos.getCol()][pos.getRow()];
            // בדיקה אם הדיסק הוא UnflippableDisc
            if (flippedDisc instanceof UnflippableDisc) {
                System.out.println("Player " + (player.isPlayerOne() ? "1" : "2") +
                        " tried to flip an unflippable disc in (" + pos.getCol() + ", " + pos.getRow() + ") - no change.");
                continue;
            }
            System.out.println("Player " + (player.isPlayerOne() ? "1" : "2") +
                    " flipped the " + flippedDisc.getType() + " in (" + pos.getCol() + ", " + pos.getRow() + ")");
        }
        System.out.println(); // רווח אחרי הדפסת כל הדיסקים שהתהפכו
    }


    private void logUndoMove(Move lastMove, List<Position> flippedBackPositions) {
        System.out.println();
        System.out.println("Undoing last move:");
        System.out.println("\tUndo: removing " + lastMove.getDisc().getType() +
                " from (" + lastMove.getPosition().getCol() + ", " + lastMove.getPosition().getRow() + ")");


    }



}
