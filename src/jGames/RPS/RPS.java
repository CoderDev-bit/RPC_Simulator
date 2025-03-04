package jGames.RPS;

import java.util.Random;

class RPS {
    // Existing variables ...
    private Random rndMove = new Random();

    Integer intMoveA, intMoveB, intLastMoveA, intLastMoveB;
    Boolean blnIsWinnerA, blnLastWinnerA;
    String strStratA, strStratB;

    Double dblWinRateA, dblWinRateB, dblTieRate;
    Double dblRockPickRateA, dblRockPickRateB, dblPaperPickRateA, dblPaperPickRateB, dblScissorsPickRateA, dblScissorsPickRateB;
    private int[] arrMoveCountA = {1, 1, 1}, arrMoveCountB = {1, 1, 1};

    double dblTotalRounds = 0;
    double dblWinsA = 0;
    int intLossesA = 0;
    double dblWinsB = 0;
    int intLossesB = 0;
    int intTies = 0;

    // Current streak counters.
    private int intCurrentWinStreakA = 0, intCurrentLoseStreakA = 0;
    private int intCurrentWinStreakB = 0, intCurrentLoseStreakB = 0;
    private int intCurrentTieStreak = 0;

    // New maximum streak variables (public for GUI
    // access).
    int intMaxWinStreakA = 0, intMaxLoseStreakA = 0;
    int intMaxWinStreakB = 0, intMaxLoseStreakB = 0;
    int intMaxTieStreak = 0;

    // Entropy for each player.
    double dblEntropyA = 0, dblEntropyB = 0;

    void simulateTrial() {
        // Save previous moves/outcome.
        intLastMoveA = intMoveA;
        intLastMoveB = intMoveB;
        blnLastWinnerA = blnIsWinnerA;

        // Determine moves.
        intMoveA = simulateMove(true);
        intMoveB = simulateMove(false);

        // Determine winner.
        if (intMoveA.equals(intMoveB)) {
            blnIsWinnerA = null;
        } else {
            blnIsWinnerA = (intMoveA == 0 && intMoveB == 2) ||
                    (intMoveA == 1 && intMoveB == 0) ||
                    (intMoveA == 2 && intMoveB == 1);
        }

        dblTotalRounds++;

        // Update streaks and overall stats.
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
        }

        dblWinRateA = dblWinsA / dblTotalRounds;
        dblWinRateB = dblWinsB / dblTotalRounds;
        dblTieRate = (double) intTies / dblTotalRounds;

        // Update move pick rates.
        int totalMovesA = arrMoveCountA[0] + arrMoveCountA[1] + arrMoveCountA[2];
        int totalMovesB = arrMoveCountB[0] + arrMoveCountB[1] + arrMoveCountB[2];
        dblRockPickRateA = (double) arrMoveCountA[0] / totalMovesA;
        dblPaperPickRateA = (double) arrMoveCountA[1] / totalMovesA;
        dblScissorsPickRateA = (double) arrMoveCountA[2] / totalMovesA;
        dblRockPickRateB = (double) arrMoveCountB[0] / totalMovesB;
        dblPaperPickRateB = (double) arrMoveCountB[1] / totalMovesB;
        dblScissorsPickRateB = (double) arrMoveCountB[2] / totalMovesB;

        // Compute entropy for both players.
        dblEntropyA = calculateEntropy(dblRockPickRateA, dblPaperPickRateA, dblScissorsPickRateA);
        dblEntropyB = calculateEntropy(dblRockPickRateB, dblPaperPickRateB, dblScissorsPickRateB);
    }

    // Calculate Shannon entropy (in bits) from three probabilities.
    private double calculateEntropy(double p1, double p2, double p3) {
        return - (entropyTerm(p1) + entropyTerm(p2) + entropyTerm(p3));
    }

    private double entropyTerm(double p) {
        return (p <= 0) ? 0 : p * (Math.log(p) / Math.log(2));
    }

    // This method returns a move (0, 1, or 2) based on the current player's strategy.
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

    // Simulate multiple rounds and then print out statistics.
    void simulateGame(int rounds) {
        for (int i = 0; i < rounds; i++) {
            simulateTrial();
        }
        System.out.println("Total Rounds: " + dblTotalRounds);
        System.out.println("Wins for A: " + dblWinsA);
        System.out.println("Losses for A: " + intLossesA);
        System.out.println("Wins for B: " + dblWinsB);
        System.out.println("Losses for B: " + intLossesB);
        System.out.println("Ties: " + intTies);
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
