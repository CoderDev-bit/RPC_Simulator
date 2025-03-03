package app;

public class Test {

    boolean blnEndless;
    Integer IntStopAtTrial;
    Object objGame;

    void construct(String game, boolean endless, Integer stopAtTrial) {

        if (game.equals("RPS")) {
            objGame = new RPS();
            blnEndless = endless;
            IntStopAtTrial = stopAtTrial;
        }

        System.out.println(objGame);

    }

}
