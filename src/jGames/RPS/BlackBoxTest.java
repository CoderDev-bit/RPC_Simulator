package jGames.RPS;

public class BlackBoxTest {
    /* Tolerance for double comparisons. */
    static final double TOLERANCE = 0.0001;

    public static void main(String[] args) {
        testRoundIncrementAndOutcomeConsistency();
        testOutcomeRulesConsistency();
        testCumulativeStatsConsistency();
        testPickRatesSumToOne();
        testEntropyRange();
        testStrategyOutcomes();
    }

    /* Test that simulateTrial() increases the round count and that the outcome is set correctly. */
    public static void testRoundIncrementAndOutcomeConsistency() {
        RPS rps = new RPS();
        rps.strStratA = "Random";
        rps.strStratB = "Random";
        double initialRounds = rps.dblTotalRounds;
        rps.simulateTrial();
        double newRounds = rps.dblTotalRounds;
        boolean roundIncremented = Math.abs(newRounds - (initialRounds + 1)) < TOLERANCE;

        /* Verify outcome consistency for this single trial. */
        boolean outcomeValid = false;
        if (rps.intMoveA.equals(rps.intMoveB)) {
            outcomeValid = (rps.blnIsWinnerA == null);
        } else {
            /* Check winning rule: */
            /* Rock (0) beats Scissors (2), Paper (1) beats Rock (0), Scissors (2) beats Paper (1) */
            if (rps.intMoveA == 0 && rps.intMoveB == 2) {
                outcomeValid = (rps.blnIsWinnerA != null && rps.blnIsWinnerA);
            } else if (rps.intMoveA == 1 && rps.intMoveB == 0) {
                outcomeValid = (rps.blnIsWinnerA != null && rps.blnIsWinnerA);
            } else if (rps.intMoveA == 2 && rps.intMoveB == 1) {
                outcomeValid = (rps.blnIsWinnerA != null && rps.blnIsWinnerA);
            } else {
                outcomeValid = (rps.blnIsWinnerA != null && !rps.blnIsWinnerA);
            }
        }
        if (roundIncremented && outcomeValid) {
            System.out.println("testRoundIncrementAndOutcomeConsistency: PASSED");
        } else {
            System.out.println("testRoundIncrementAndOutcomeConsistency: FAILED");
        }
    }



    /* Simulate many rounds and ensure that for each round the outcome rule holds. */
    public static void testOutcomeRulesConsistency() {
        RPS rps = new RPS();
        rps.strStratA = "Random";
        rps.strStratB = "Random";
        boolean allRoundsValid = true;
        int trials = 100;
        for (int i = 0; i < trials; i++) {
            rps.simulateTrial();
            if (rps.intMoveA.equals(rps.intMoveB)) {
                if (rps.blnIsWinnerA != null) {
                    allRoundsValid = false;
                    break;
                }
            } else {
                boolean expectedWinnerA = (rps.intMoveA == 0 && rps.intMoveB == 2) ||
                        (rps.intMoveA == 1 && rps.intMoveB == 0) ||
                        (rps.intMoveA == 2 && rps.intMoveB == 1);
                if (rps.blnIsWinnerA == null || rps.blnIsWinnerA != expectedWinnerA) {
                    allRoundsValid = false;
                    break;
                }
            }
        }
        System.out.println("testOutcomeRulesConsistency: " + (allRoundsValid ? "PASSED" : "FAILED"));
    }

    /* Check that cumulative win, loss, and tie counts add up to the total rounds. */
    public static void testCumulativeStatsConsistency() {
        RPS rps = new RPS();
        rps.strStratA = "Random";
        rps.strStratB = "Random";
        int trials = 500;
        for (int i = 0; i < trials; i++) {
            rps.simulateTrial();
        }
        double totalRounds = rps.dblTotalRounds;
        double sumA = rps.dblWinsA + rps.intLossesA + rps.intTies;
        double sumB = rps.dblWinsB + rps.intLossesB + rps.intTies;
        boolean consistencyA = Math.abs(totalRounds - sumA) < TOLERANCE;
        boolean consistencyB = Math.abs(totalRounds - sumB) < TOLERANCE;
        if (consistencyA && consistencyB) {
            System.out.println("testCumulativeStatsConsistency: PASSED");
        } else {
            System.out.println("testCumulativeStatsConsistency: FAILED");
            System.out.println("Total rounds: " + totalRounds + ", SumA: " + sumA + ", SumB: " + sumB);
        }
    }

    /* Verify that the pick rates for rock, paper, and scissors sum to 1 for both players. */
    public static void testPickRatesSumToOne() {
        RPS rps = new RPS();
        rps.strStratA = "Random";
        rps.strStratB = "Random";
        int trials = 300;
        for (int i = 0; i < trials; i++) {
            rps.simulateTrial();
        }
        double sumA = rps.dblRockPickRateA + rps.dblPaperPickRateA + rps.dblScissorsPickRateA;
        double sumB = rps.dblRockPickRateB + rps.dblPaperPickRateB + rps.dblScissorsPickRateB;
        boolean validA = Math.abs(sumA - 1.0) < TOLERANCE;
        boolean validB = Math.abs(sumB - 1.0) < TOLERANCE;
        if (validA && validB) {
            System.out.println("testPickRatesSumToOne: PASSED");
        } else {
            System.out.println("testPickRatesSumToOne: FAILED");
            System.out.println("SumA: " + sumA + ", SumB: " + sumB);
        }
    }

    /* Check that the computed entropy for both players is within the valid range. */
    public static void testEntropyRange() {
        RPS rps = new RPS();
        rps.strStratA = "Random";
        rps.strStratB = "Random";
        int trials = 300;
        for (int i = 0; i < trials; i++) {
            rps.simulateTrial();
        }
        double entropyA = rps.dblEntropyA;
        double entropyB = rps.dblEntropyB;
        double maxEntropy = Math.log(3) / Math.log(2); // Maximum entropy for 3 outcomes (~1.585)
        boolean validA = (entropyA >= 0 && entropyA <= maxEntropy);
        boolean validB = (entropyB >= 0 && entropyB <= maxEntropy);
        if (validA && validB) {
            System.out.println("testEntropyRange: PASSED");
        } else {
            System.out.println("testEntropyRange: FAILED");
            System.out.println("EntropyA: " + entropyA + ", EntropyB: " + entropyB);
        }
    }

    /* For each pair of strategies, simulate many rounds and check that the cumulative stats remain consistent. */
    public static void testStrategyOutcomes() {
        String[] strategies = {"Random", "Human", "Against Human", "Adaptive"};
        int trials = 1000;
        boolean allPass = true;
        for (String stratA : strategies) {
            for (String stratB : strategies) {
                RPS rps = new RPS();
                rps.strStratA = stratA;
                rps.strStratB = stratB;
                for (int i = 0; i < trials; i++) {
                    rps.simulateTrial();
                }
                double totalRounds = rps.dblTotalRounds;
                double sumA = rps.dblWinsA + rps.intLossesA + rps.intTies;
                double sumB = rps.dblWinsB + rps.intLossesB + rps.intTies;
                if (Math.abs(totalRounds - sumA) >= TOLERANCE || Math.abs(totalRounds - sumB) >= TOLERANCE) {
                    allPass = false;
                    System.out.println("testStrategyOutcomes: FAILED for strategies: " + stratA + " vs " + stratB);
                }
            }
        }
        if (allPass) {
            System.out.println("testStrategyOutcomes: PASSED for all strategy combinations");
        }
    }


}
