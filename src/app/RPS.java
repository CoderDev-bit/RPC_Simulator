package app;

import java.util.Random;

public class RPS {

    Random rndMove = new Random();
    Integer intMoveA, intMoveB, intLastMoveA, intLastMoveB;
    String strStratB, strStratA;
    Double dblWinRateA, dblWinStreakA, dblLoseStreakA, dblWinStreakB, dblLoseStreakB, dblTieRate, dblRockPickRateA, dblRockPickRateB, dblPaperPickRateA, dblPaperPickRateB, dblScissorsPickRateA, dblScissorsPickRateB;
    int[] arrMoveCountA = {1,1,1}, arrMoveCountB = {1,1,1};

    /*
    *
    * Rock = 0, Paper = 1, Scissors = 2
    *
    *
    */

    void simulateTrial() {

        intLastMoveA = intMoveA;
        intLastMoveB = intMoveB;

        intMoveA = simulateMove(true);
        intMoveB = simulateMove(false);

    }

    int simulateMove(boolean isTurnOfPlayerA) {

        if (isTurnOfPlayerA) {

            switch (strStratA) {

                case "Random":

                    return rndMove.nextInt(3);

                case "Human":

                    return intLastMoveB;

                case "Against Human":

                    return simulateMove(false);

                case "Adaptive":



            }

        }

        return 0;

    }

}
