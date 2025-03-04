package app;

import java.util.*;

public class RPS {

    // Random number generator
    Random rndMove = new Random();

    // Moves: null means not set yet.
    Integer intMoveA, intMoveB, intLastMoveA, intLastMoveB;
    // blnIsWinnerA is true if A wins, false if B wins, null if tie.
    Boolean blnIsWinnerA, blnLastWinnerA;

    // Strategy strings for each player.
    String strStratA, strStratB;

    // Various performance metrics.
    Double dblWinRateA, dblWinRateB, dblWinStreakA, dblLoseStreakA, dblWinStreakB, dblLoseStreakB, dblTieRate;
    Double dblRockPickRateA, dblRockPickRateB, dblPaperPickRateA, dblPaperPickRateB, dblScissorsPickRateA, dblScissorsPickRateB;

    // Starting with 1 to avoid division by zero in rate calculations.
    int[] arrMoveCountA = {1, 1, 1}, arrMoveCountB = {1, 1, 1};

    // Counters for overall stats.
    public double totalRounds = 0;
    public double winsA = 0;
    public int lossesA = 0;
    public double winsB = 0;
    public int lossesB = 0;
    public int ties = 0;
    private int currentWinStreakA = 0;
    private int currentLoseStreakA = 0;
    private int currentWinStreakB = 0;
    private int currentLoseStreakB = 0;

    /*
     * Representation:
     *   Rock = 0, Paper = 1, Scissors = 2
     */

    // Simulate one round/trial of RPS.
    public void simulateTrial() {
        // Save previous moves and outcome.
        intLastMoveA = intMoveA;
        intLastMoveB = intMoveB;
        blnLastWinnerA = blnIsWinnerA;

        // Determine moves based on strategies.
        intMoveA = simulateMove(true);
        intMoveB = simulateMove(false);

// Determine winner:
// If same move then tie.
        if (intMoveA.equals(intMoveB)) {
            blnIsWinnerA = null;
        }
// Cases where A wins (rock beats scissors, paper beats rock, scissors beats paper).
        else if ((intMoveA == 0 && intMoveB == 2) ||
                (intMoveA == 1 && intMoveB == 0) ||
                (intMoveA == 2 && intMoveB == 1)) {
            blnIsWinnerA = true;
        } else {
            blnIsWinnerA = false;
        }

        // Update overall stats.
        totalRounds++;
        if (blnIsWinnerA == null) {
            ties++;
            currentWinStreakA = 0;
            currentLoseStreakA = 0;
            currentWinStreakB = 0;
            currentLoseStreakB = 0;
        } else if (blnIsWinnerA) {
            winsA++;
            lossesB++;
            currentWinStreakA++;
            currentLoseStreakA = 0;
            currentWinStreakB = 0;
            currentLoseStreakB++;
        } else {
            winsB++;
            lossesA++;
            currentWinStreakA = 0;
            currentLoseStreakA++;
            currentWinStreakB++;
            currentLoseStreakB = 0;
        }
        dblWinRateA = (double) winsA / totalRounds;
        dblWinRateB = (double) winsB / totalRounds;
        dblTieRate = (double) ties / totalRounds;
        dblWinStreakA = (double) currentWinStreakA;
        dblLoseStreakA = (double) currentLoseStreakA;
        dblWinStreakB = (double) currentWinStreakB;
        dblLoseStreakB = (double) currentLoseStreakB;

        // Update pick rates based on counts.
        int totalMovesA = arrMoveCountA[0] + arrMoveCountA[1] + arrMoveCountA[2];
        int totalMovesB = arrMoveCountB[0] + arrMoveCountB[1] + arrMoveCountB[2];
        dblRockPickRateA = (double) arrMoveCountA[0] / totalMovesA;
        dblPaperPickRateA = (double) arrMoveCountA[1] / totalMovesA;
        dblScissorsPickRateA = (double) arrMoveCountA[2] / totalMovesA;
        dblRockPickRateB = (double) arrMoveCountB[0] / totalMovesB;
        dblPaperPickRateB = (double) arrMoveCountB[1] / totalMovesB;
        dblScissorsPickRateB = (double) arrMoveCountB[2] / totalMovesB;
    }

    // This method returns a move (0, 1, or 2) based on the current player's strategy.
    int simulateMove(boolean isTurnOfPlayerA) {
        // Choose the correct strategy.
        String strat = isTurnOfPlayerA ? strStratA : strStratB;
        // Get the last move of the player and the opponent.
        Integer lastMoveSelf = isTurnOfPlayerA ? intLastMoveA : intLastMoveB;
        Integer lastMoveOpponent = isTurnOfPlayerA ? intLastMoveB : intLastMoveA;
        // Determine if the last trial was a win for the current player.
        // For player A, we store blnLastWinnerA directly.
        // For player B, a win for B means A lost.
        Boolean lastWinSelf = null;
        if (lastMoveSelf != null) {
            if (isTurnOfPlayerA) {
                lastWinSelf = blnLastWinnerA;
            } else {
                lastWinSelf = (blnLastWinnerA == null ? null : !blnLastWinnerA);
            }
        }
        int move;
        switch(strat) {
            case "Random":
                // Uniform random move.
                move = rndMove.nextInt(3);
                break;

            case "Human":
                // If first move or tie (no last outcome) then random.
                if (lastMoveSelf == null || lastWinSelf == null) {
                    move = rndMove.nextInt(3);
                } else if (lastWinSelf) {
                    // Generate a random threshold > 50% (between 0.5 and 1.0)
                    double threshold = 0.5 + rndMove.nextDouble() * 0.5;
                    // If won last round, use the threshold to decide whether to repeat last move.
                    move = (rndMove.nextDouble() < threshold) ? lastMoveSelf : rndMove.nextInt(3);
                } else {
                    double threshold = 0.5 + rndMove.nextDouble() * 0.5;
                    // If lost last round, use the threshold to decide whether to counter the opponent's last move.
                    move = (lastMoveOpponent != null && rndMove.nextDouble() < threshold)
                            ? counterMove(lastMoveOpponent)
                            : rndMove.nextInt(3);
                }
                break;


            case "Against Human":
                // Similar to "Human" but if won, mimic opponent's last move.
                if (lastMoveSelf == null || lastWinSelf == null) {
                    move = rndMove.nextInt(3);
                } else if (lastWinSelf) {
                    move = (lastMoveOpponent != null && rndMove.nextDouble() < 0.7)
                            ? lastMoveOpponent
                            : rndMove.nextInt(3);
                } else {
                    move = (lastMoveOpponent != null && rndMove.nextDouble() < 0.7)
                            ? counterMove(lastMoveOpponent)
                            : rndMove.nextInt(3);
                }
                break;

            case "Adaptive":
                if (lastMoveOpponent == null) {
                    move = rndMove.nextInt(3);
                } else {
                    int[] oppCounts = isTurnOfPlayerA ? arrMoveCountB : arrMoveCountA;

                    // Compute the total count for normalization.
                    int total = oppCounts[0] + oppCounts[1] + oppCounts[2];
                    double r = rndMove.nextDouble();
                    double cumulative = 0.0;
                    int predictedMove = 0;

                    // Build a weighted probability distribution based on the counts.
                    for (int i = 0; i < oppCounts.length; i++) {
                        cumulative += (double) oppCounts[i] / total;
                        if (r < cumulative) {
                            predictedMove = i;
                            break;
                        }
                    }

                    move = counterMove(predictedMove);
                }
                break;


            default:
                // Default to random if strategy is unknown.
                move = rndMove.nextInt(3);
                break;
        }
        updateMoveCount(isTurnOfPlayerA, move);
        //System.out.println("Move: " + move);
        return move;

    }

    // Increment the move count for the given player.
    private void updateMoveCount(boolean isPlayerA, int move) {
        if (isPlayerA) {
            arrMoveCountA[move]++;
        } else {
            arrMoveCountB[move]++;
        }
    }

    // Given an opponent move, return the counter move.
    // (Rock -> Paper, Paper -> Scissors, Scissors -> Rock)
    private int counterMove(int move) {
        return (move + 1) % 3;
    }

    // Predict the opponent's next move based on past counts.
    private int predictOpponentMove(int[] counts) {
        int predicted = 0;
        for (int i = 1; i < counts.length; i++) {
            if (counts[i] > counts[predicted]) {
                predicted = i;
            }
        }
        return predicted;
    }

    // Simulate multiple rounds and then print out statistics.
    public void simulateGame(int rounds) {
        for (int i = 0; i < rounds; i++) {
            simulateTrial();
        }
        System.out.println("Total Rounds: " + totalRounds);
        System.out.println("Wins for A: " + winsA);
        System.out.println("Losses for A: " + lossesA);
        System.out.println("Wins for B: " + winsB);
        System.out.println("Losses for B: " + lossesB);
        System.out.println("Ties: " + ties);
        System.out.println("Win rate for A: " + dblWinRateA);
        System.out.println("Win rate for B: " + dblWinRateB);
        System.out.println("Tie rate: " + dblTieRate);
        System.out.println("Player A Move Rates: Rock: " + dblRockPickRateA +
                ", Paper: " + dblPaperPickRateA +
                ", Scissors: " + dblScissorsPickRateA);
        System.out.println("Player B Move Rates: Rock: " + dblRockPickRateB +
                ", Paper: " + dblPaperPickRateB +
                ", Scissors: " + dblScissorsPickRateB);
    }

}
