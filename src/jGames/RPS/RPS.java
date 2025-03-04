package jGames.RPS;

import java.util.Random;

public class RPS {
    // Existing variables ...
    Random rndMove = new Random();

    Integer intMoveA, intMoveB, intLastMoveA, intLastMoveB;
    Boolean blnIsWinnerA, blnLastWinnerA;
    String strStratA, strStratB;

    Double dblWinRateA, dblWinRateB, dblTieRate;
    Double dblRockPickRateA, dblRockPickRateB, dblPaperPickRateA, dblPaperPickRateB, dblScissorsPickRateA, dblScissorsPickRateB;
    int[] arrMoveCountA = {1, 1, 1}, arrMoveCountB = {1, 1, 1};

    public double totalRounds = 0;
    public double winsA = 0;
    public int lossesA = 0;
    public double winsB = 0;
    public int lossesB = 0;
    public int ties = 0;

    // Current streak counters.
    private int currentWinStreakA = 0, currentLoseStreakA = 0;
    private int currentWinStreakB = 0, currentLoseStreakB = 0;
    private int currentTieStreak = 0;

    // New maximum streak variables (public for GUI access).
    public int maxWinStreakA = 0, maxLoseStreakA = 0;
    public int maxWinStreakB = 0, maxLoseStreakB = 0;
    public int maxTieStreak = 0;

    // Entropy for each player.
    public double entropyA = 0, entropyB = 0;

    public void simulateTrial() {
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

        totalRounds++;

        // Update streaks and overall stats.
        if (blnIsWinnerA == null) {
            ties++;
            currentTieStreak++;
            if (currentTieStreak > maxTieStreak) {
                maxTieStreak = currentTieStreak;
            }
            currentWinStreakA = currentLoseStreakA = 0;
            currentWinStreakB = currentLoseStreakB = 0;
        } else if (blnIsWinnerA) {
            winsA++;
            lossesB++;
            // Player A wins.
            currentWinStreakA++;
            if (currentWinStreakA > maxWinStreakA) {
                maxWinStreakA = currentWinStreakA;
            }
            currentLoseStreakA = 0;
            // Player B loses.
            currentLoseStreakB++;
            if (currentLoseStreakB > maxLoseStreakB) {
                maxLoseStreakB = currentLoseStreakB;
            }
            currentWinStreakB = 0;
            currentTieStreak = 0;
        } else {
            winsB++;
            lossesA++;
            // Player B wins.
            currentWinStreakB++;
            if (currentWinStreakB > maxWinStreakB) {
                maxWinStreakB = currentWinStreakB;
            }
            currentLoseStreakB = 0;
            // Player A loses.
            currentLoseStreakA++;
            if (currentLoseStreakA > maxLoseStreakA) {
                maxLoseStreakA = currentLoseStreakA;
            }
            currentWinStreakA = 0;
            currentTieStreak = 0;
        }

        dblWinRateA = winsA / totalRounds;
        dblWinRateB = winsB / totalRounds;
        dblTieRate = (double) ties / totalRounds;

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
        entropyA = calculateEntropy(dblRockPickRateA, dblPaperPickRateA, dblScissorsPickRateA);
        entropyB = calculateEntropy(dblRockPickRateB, dblPaperPickRateB, dblScissorsPickRateB);
    }

    // Calculate Shannon entropy (in bits) from three probabilities.
    private double calculateEntropy(double p1, double p2, double p3) {
        return - (entropyTerm(p1) + entropyTerm(p2) + entropyTerm(p3));
    }

    private double entropyTerm(double p) {
        return (p <= 0) ? 0 : p * (Math.log(p) / Math.log(2));
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
