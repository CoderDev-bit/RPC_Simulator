package jGames.RPS;

import java.awt.*;

public class IntegrationTest {
    public static void main(String[] args) {
        /* Create an instance of the GUI and initialize it. */
        GUI gui = new GUI();
        gui.initGUI();

        /* Simulate user input on the configuration panel. */
        /* Set a valid speed (e.g., 50 ms) and a fixed number of trials (e.g., 10 rounds). */
        gui.txtSpeed.setText("50");
        gui.txtTrials.setText("10");  // Non-endless mode

        /* Choose strategies (for simplicity, both set to "Random"). */
        gui.cbPlayerA.setSelectedItem("Random");
        gui.cbPlayerB.setSelectedItem("Random");

        /* Simulate clicking the Start button. */
        gui.btnStart.doClick();

        /* Allow some time for the simulation panel to load and the timer to run. */
        try {
            Thread.sleep(1000); // Wait for the GUI to update.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /* Verify that the simulation panel is present in the frame.*/
        boolean simulationPanelFound = false;
        for (Component comp : gui.frmMain.getContentPane().getComponents()) {
            if (comp == gui.pnlSimulation) {
                simulationPanelFound = true;
                break;
            }
        }
        if (simulationPanelFound) {
            System.out.println("IntegrationTest: Simulation panel successfully displayed after Start button click. PASSED");
        } else {
            System.out.println("IntegrationTest: FAILED - Simulation panel not found after Start button click.");
        }

        /* The simulation should stop when the set number of trials is reached.*/
        /* Now simulate clicking the End Game button to load the report panel.*/
        gui.btnEndTest.doClick();
        try {
            Thread.sleep(500); // Wait for the GUI to update.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /* Verify that the report panel is now displayed. */
        boolean reportPanelFound = false;
        for (Component comp : gui.frmMain.getContentPane().getComponents()) {
            if (comp == gui.pnlReport) {
                reportPanelFound = true;
                break;
            }
        }
        if (reportPanelFound) {
            System.out.println("IntegrationTest: Report panel successfully displayed after End Game button click. PASSED");
        } else {
            System.out.println("IntegrationTest: FAILED - Report panel not found after End Game button click.");
        }

        /* Verify that the report table data is consistent with the RPS engine's statistics. */
        if (gui.tblReport != null && gui.objRPS != null) {
            int reportedTrials = Integer.parseInt(gui.tblReport.getValueAt(0, 1).toString());
            int expectedTrials = (int) gui.objRPS.dblTotalRounds;
            if (reportedTrials == expectedTrials) {
                System.out.println("IntegrationTest: Report table data (Completed Trials) is consistent. PASSED");
            } else {
                System.out.println("IntegrationTest: FAILED - Inconsistent Completed Trials data.");
                System.out.println("Expected: " + expectedTrials + ", Reported: " + reportedTrials);
            }
        } else {
            System.out.println("IntegrationTest: FAILED - Report table or RPS object is null.");
        }

        /* Optionally, dispose the frame after testing. */
        gui.frmMain.dispose();

        testInteractionsBetweenStrats();
    }

    /* integrate test the RPS methods by simulating an experiment
    * furthermore, verify the win rates (eg. Random should never lose by more then 10%) to ensure
    * the strategies are programmed correctly
    */
    public static void testInteractionsBetweenStrats() {
        System.out.println("=====================");

        String[] strategies = {"Random", "Human", "Against Human", "Adaptive"};

        for (int i = 0; i < strategies.length; i++) {
            for (int j = i; j < strategies.length; j++) {
                String stratA = strategies[i];
                String stratB = strategies[j];

                System.out.println(stratA + " vs " + stratB);
                RPS rps = new RPS();
                rps.strStratA = stratA;
                rps.strStratB = stratB;

                for (int game = 1; game <= 3; game++) {
                    for (int trial = 1; trial <= 1000; trial++) {
                        rps.simulateTrial();
                    }
                    System.out.println("Game " + game + ": Player A Win Rate: " + (rps.dblWinRateA * 100) +
                            "%, Player B Win Rate: " + (rps.dblWinRateB * 100) +
                            "%, Tie Rate: " + (rps.dblTieRate * 100) + "%");
                }
                System.out.println("=====================");
            }
        }
    }

}
