/**************************************************************************
 *
 * File name:
 * RPS.java
 *
 * Description:
 * This file contains a class RPS that simulates a Rock, Paper, Scissors
 * (RPS) game between two players (A and B). The game tracks various
 * statistics such as win rates, move pick rates, and streaks. It includes
 * functionality for handling different move strategies, including Random,
 * Human-like, Against Human, and Adaptive strategies based on the players'
 * previous moves and outcomes. The class also calculates and updates
 * entropy for each player to measure their unpredictability and improve
 * gameplay dynamics.
 *
 * Author:
 * S. Patel
 *
 * Date: Mar/3/2025
 *
 * Concepts:
 * - Use of Random for move generation and strategy implementation
 * - Use of arrays to track move counts and player statistics
 * - Boolean and Integer logic for determining winners and game outcomes
 * - Calculation of entropy to analyze player behavior
 * - Use of conditional statements to manage player strategies
 * - Implementation of adaptive move logic to counter opponent's tendencies
 *
 ***************************************************************************/
package jGames.RPS;

import java.util.Random;

class RPS {

    private final Random rndMove = new Random();
    private final int[] arrMoveCountA = {1, 1, 1};
    private final int[] arrMoveCountB = {1, 1, 1};

    Integer intMoveA, intMoveB, intLastMoveA, intLastMoveB;
    Boolean blnIsWinnerA, blnLastWinnerA;
    String strStratA, strStratB;
    Double dblWinRateA, dblWinRateB, dblTieRate;
    Double dblRockPickRateA, dblRockPickRateB, dblPaperPickRateA, dblPaperPickRateB, dblScissorsPickRateA, dblScissorsPickRateB;

    double dblTotalRounds = 0;
    double dblWinsA = 0;
    int intLossesA = 0;
    double dblWinsB = 0;
    int intLossesB = 0;
    int intTies = 0;
    int intMaxWinStreakA = 0, intMaxLoseStreakA = 0;
    int intMaxWinStreakB = 0, intMaxLoseStreakB = 0;
    int intMaxTieStreak = 0;
    double dblEntropyA = 0, dblEntropyB = 0;
    private int intCurrentWinStreakA = 0, intCurrentLoseStreakA = 0;
    private int intCurrentWinStreakB = 0, intCurrentLoseStreakB = 0;
    private int intCurrentTieStreak = 0;

    /**********************************************************************
     * Method name:
     * simulateTrial
     *
     * Description:
     * This method simulates a trial in the game by determining the moves of two players,
     * comparing their moves to decide the winner, and updating various statistics
     * such as streaks, win rates, and move pick rates.
     *
     * Parameters:
     * None
     *
     * Parameter Restrictions:
     * No restrictions
     *
     * Return:
     * None
     *
     * Return Restrictions:
     * No restrictions
     **********************************************************************/
    void simulateTrial() {
        /* Save previous moves/outcome */
        intLastMoveA = intMoveA;
        intLastMoveB = intMoveB;
        blnLastWinnerA = blnIsWinnerA;

        intMoveA = simulateMove(true);
        intMoveB = simulateMove(false);

        /*
         * Compare the moves of Player A and Player B to determine the winner.
         * If moves are equal, it's a tie, otherwise, determine the winner based on the game rules.
         */
        if (intMoveA.equals(intMoveB)) {
            blnIsWinnerA = null;
        } else {
            blnIsWinnerA = (intMoveA == 0 && intMoveB == 2) ||
                    (intMoveA == 1 && intMoveB == 0) ||
                    (intMoveA == 2 && intMoveB == 1);
        } /* end of if else block */

        dblTotalRounds++;

        /*
         * Update the streaks, wins, losses, ties, and max streaks for both players.
         * Reset streaks if a tie occurs or if the current player's streak is broken.
         */
        if (blnIsWinnerA == null) {
            intTies++;
            intCurrentTieStreak++;
            if (intCurrentTieStreak > intMaxTieStreak) {
                intMaxTieStreak = intCurrentTieStreak;
            }
            intCurrentWinStreakA = intCurrentLoseStreakA = 0;
            intCurrentWinStreakB = intCurrentLoseStreakB = 0;
        } else if (blnIsWinnerA) {
            dblWinsA++;
            intLossesB++;
            // Player A wins.
            intCurrentWinStreakA++;
            if (intCurrentWinStreakA > intMaxWinStreakA) {
                intMaxWinStreakA = intCurrentWinStreakA;
            }
            intCurrentLoseStreakA = 0;
            // Player B loses.
            intCurrentLoseStreakB++;
            if (intCurrentLoseStreakB > intMaxLoseStreakB) {
                intMaxLoseStreakB = intCurrentLoseStreakB;
            }
            intCurrentWinStreakB = 0;
            intCurrentTieStreak = 0;
        } else {
            dblWinsB++;
            intLossesA++;
            // Player B wins.
            intCurrentWinStreakB++;
            if (intCurrentWinStreakB > intMaxWinStreakB) {
                intMaxWinStreakB = intCurrentWinStreakB;
            }
            intCurrentLoseStreakB = 0;
            // Player A loses.
            intCurrentLoseStreakA++;
            if (intCurrentLoseStreakA > intMaxLoseStreakA) {
                intMaxLoseStreakA = intCurrentLoseStreakA;
            }
            intCurrentWinStreakA = 0;
            intCurrentTieStreak = 0;
        } /* end of if-else block */

        dblWinRateA = dblWinsA / dblTotalRounds;
        dblWinRateB = dblWinsB / dblTotalRounds;
        dblTieRate = (double) intTies / dblTotalRounds;

        /* Update move pick rates. */
        int totalMovesA = arrMoveCountA[0] + arrMoveCountA[1] + arrMoveCountA[2];
        int totalMovesB = arrMoveCountB[0] + arrMoveCountB[1] + arrMoveCountB[2];
        dblRockPickRateA = (double) arrMoveCountA[0] / totalMovesA;
        dblPaperPickRateA = (double) arrMoveCountA[1] / totalMovesA;
        dblScissorsPickRateA = (double) arrMoveCountA[2] / totalMovesA;
        dblRockPickRateB = (double) arrMoveCountB[0] / totalMovesB;
        dblPaperPickRateB = (double) arrMoveCountB[1] / totalMovesB;
        dblScissorsPickRateB = (double) arrMoveCountB[2] / totalMovesB;

        /* Compute entropy for both players */
        dblEntropyA = calculateEntropy(dblRockPickRateA, dblPaperPickRateA, dblScissorsPickRateA);
        dblEntropyB = calculateEntropy(dblRockPickRateB, dblPaperPickRateB, dblScissorsPickRateB);
    }

    /**********************************************************************
     * Method name:
     * calculateEntropy
     *
     * Description:
     * This method calculates the entropy for a set of three probabilities (p1, p2, p3).
     * It uses the formula for entropy, which is the sum of the individual entropy terms for each probability.
     *
     * Parameters:
     * - p1: Probability of the first event.
     * - p2: Probability of the second event.
     * - p3: Probability of the third event.
     *
     * Parameter Restrictions:
     * - Probabilities must be between 0 and 1 (inclusive).
     *
     * Return:
     * - A double value representing the entropy for the given probabilities.
     *
     * Return Restrictions:
     * - No restrictions.
     **********************************************************************/
    private double calculateEntropy(double p1, double p2, double p3) {
        return -(entropyTerm(p1) + entropyTerm(p2) + entropyTerm(p3));
    }

    /**********************************************************************
     * Method name:
     * entropyTerm
     *
     * Description:
     * This method calculates the entropy term for a single probability value.
     * It computes the term p * log2(p), which contributes to the total entropy.
     * If the probability is 0 or less, it returns 0 as log(0) is undefined.
     *
     * Parameters:
     * - p: The probability of a single event.
     *
     * Parameter Restrictions:
     * - p must be between 0 and 1 (inclusive).
     *
     * Return:
     * - A double value representing the entropy term for the given probability.
     *
     * Return Restrictions:
     * - No restrictions.
     **********************************************************************/
    private double entropyTerm(double p) {
        return (p <= 0) ? 0 : p * (Math.log(p) / Math.log(2));
    }

    /**********************************************************************
     * Method name:
     * simulateMove
     *
     * Description:
     * This method simulates a move for a player based on their chosen strategy.
     * It determines the move based on different strategies like "Random", "Human", "Against Human", and "Adaptive".
     * It also updates the move count for the player and returns the move chosen.
     *
     * Parameters:
     * - isTurnOfPlayerA: A boolean indicating whether it is Player A's turn (true) or Player B's turn (false).
     *
     * Parameter Restrictions:
     * - isTurnOfPlayerA: Boolean value (true for Player A, false for Player B).
     *
     * Return:
     * - An integer representing the move chosen (0 = Rock, 1 = Paper, 2 = Scissors).
     *
     * Return Restrictions:
     * - No restrictions.
     **********************************************************************/
    private int simulateMove(boolean isTurnOfPlayerA) {
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

        /* This switch statement determined a player's move based on his strategy */
        switch (strat) {

            /*
             * In the "Random" strategy, the move is chosen uniformly at random
             * between Rock, Paper, and Scissors.
             */
            case "Random":
                // Uniform random move.
                move = rndMove.nextInt(3);
                break;

            /*
             * In the "Human" strategy, if it's the first move or a tie, the move is chosen randomly.
             * If the player won the previous round, the move will likely be repeated.
             * If the player lost, the strategy will likely counter the opponent's last move.
             */
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

            /*
             * The "Against Human" strategy is similar to the "Human" strategy,
             * but if the player won the previous round, they mimic the opponent's last move.
             * If the player lost, they counter the opponent's last move.
             */
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

            /*
             * In the "Adaptive" strategy, the move is based on the opponent's previous move frequencies.
             * It calculates a weighted probability distribution and chooses a counter move.
             */
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
        return move;

    }

    /**********************************************************************
     * Method name:
     * updateMoveCount
     *
     * Description:
     * This method updates the move count for either Player A or Player B based on the given move.
     * It increments the appropriate move count for the player (either A or B) based on the boolean parameter.
     *
     * Parameters:
     * - isPlayerA: A boolean flag indicating whether the move is for Player A (true) or Player B (false).
     * - move: The move to be counted (0 = Rock, 1 = Paper, 2 = Scissors).
     *
     * Parameter Restrictions:
     * - isPlayerA: Boolean value (true for Player A, false for Player B).
     * - move: An integer between 0 and 2 representing Rock, Paper, or Scissors.
     *
     * Return:
     * - None
     *
     * Return Restrictions:
     * - No restrictions.
     **********************************************************************/
    private void updateMoveCount(boolean isPlayerA, int move) {
        if (isPlayerA) {
            arrMoveCountA[move]++;
        } else {
            arrMoveCountB[move]++;
        }
    }

    /**********************************************************************
     * Method name:
     * counterMove
     *
     * Description:
     * This method returns the counter move to the given move in a Rock, Paper, Scissors game.
     * It determines the move that beats the given move by following the game logic.
     *
     * Parameters:
     * - move: The move to counter (0 = Rock, 1 = Paper, 2 = Scissors).
     *
     * Parameter Restrictions:
     * - move: An integer between 0 and 2 representing Rock, Paper, or Scissors.
     *
     * Return:
     * - An integer representing the counter move (0 = Rock, 1 = Paper, 2 = Scissors).
     *
     * Return Restrictions:
     * - No restrictions.
     **********************************************************************/
    private int counterMove(int move) {
        return (move + 1) % 3;
    }

}
