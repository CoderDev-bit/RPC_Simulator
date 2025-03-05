/**************************************************************************
 * File name:
 * GUI.java
 *
 * Description:
 * This file contains a class GUI that creates a graphical user interface (GUI)
 * for a Rock, Paper, Scissors (RPS) game simulation. It includes functionality
 * for configuring the game settings, starting the game, displaying the results
 * of the simulation, and updating the win rates in real-time. The GUI allows
 * the user to select strategies for both players, define game speed, and specify
 * whether the game should run endlessly or stop after a specified number of trials.
 *
 * Author:
 * S. Patel
 *
 * Date: Mar/3/2025
 *
 * Concepts:
 * - Use of JFrame and Swing components (JLabel, JButton, JComboBox, etc.)
 *   to build the graphical user interface.
 * - Event handling using ActionListeners to manage user interactions with
 *   buttons and other controls.
 * - Use of Timer for periodic updates in the simulation.
 * - Data validation using exception handling to ensure user input for speed and trials is valid.
 * - Use of multi-panel layout for different stages of the simulation (Config, Simulation, Report).
 * - String manipulation to display player moves and results.
 * - Integration of game logic for RPS simulation.
 * - Event-driven programming to allow interaction with the graphical interface.
 ***************************************************************************/


package jGames.RPS;

import javax.swing.*;
import java.awt.*;

class GUI {

    final int CONTAINER_W = 550, CONTAINER_H = 450;
    JFrame frmMain;
    JCheckBox[][] arrCkStats;
    JCheckBox ckSelectAll;
    JPanel pnlConfig, pnlSimulation, pnlReport;
    JButton btnStart, btnPause, btnExit, btnEndTest, btnNewTest;
    JComboBox<String> cbPlayerA, cbPlayerB;
    JRadioButton rbEndless, rbEnding;
    JLabel lblTitle, lblRealTime, lblReport, lblPlayerA, lblPlayerB, lblSpeed, lblDuration, lblStats, lblTrials, lblResultA, lblResultB, lblMoveA, lblMoveB, lblWinRateA, lblWinRateB;
    JTextField txtTrials, txtSpeed;
    JProgressBar pbWinRates;

    JTable tblReport;
    boolean blnImmediateWinner, blnPlayerChoices, blnChoicePickRate, blnTieRate;
    Timer tmrWinRates;
    RPS objRPS;
    Integer wintIntervalDelay, wintStopAtTrial;
    int intTrial;


    /**********************************************************************
     * Method name:
     * initGUI
     *
     * Description:
     * This method initializes the main JFrame window and sets up the basic layout
     * of the application window. It also calls the `initConfigPanel()` method to
     * initialize and display the configuration panel for the user to set up the game.
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
    void initGUI() {
        frmMain = new JFrame("GameTest™");
        frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmMain.setBounds(10, 10, CONTAINER_W, CONTAINER_H);

        initConfigPanel();

        frmMain.add(pnlConfig);
        frmMain.setVisible(true);
    }

    /**********************************************************************
     * Method name:
     * initConfigPanel
     *
     * Description:
     * This method initializes and configures the components for the configuration panel.
     * It sets up labels, buttons, text fields, and checkboxes for game options like
     * player strategies, speed, duration, and statistics to track during the game.
     * It also includes action listeners for handling user input and setting values.
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
    private void initConfigPanel() {
        pnlConfig = new JPanel();
        pnlConfig.setLayout(null);
        pnlConfig.setBounds(0, 0, CONTAINER_W, CONTAINER_H);

        lblTitle = new JLabel("<html><h1>Configuration</h1></html>");
        lblRealTime = new JLabel("<html><i>Real Time</i></html>");
        lblReport = new JLabel("<html><i>Report</i></html>");
        lblPlayerA = new JLabel("<html><i>Player A Strategy</html></i>");
        lblPlayerB = new JLabel("<html><i>Player B Strategy</html></i>");
        lblSpeed = new JLabel("<html><h3>SPEED:</h3></html>");
        lblDuration = new JLabel("<html><h3>DURATION:</h3></html>");
        lblStats = new JLabel("<html><h3>STATS:</h3></html>");
        rbEndless = new JRadioButton("Endless");
        rbEnding = new JRadioButton("Ending");
        cbPlayerA = new JComboBox<>(new String[]{"Random", "Human", "Against Human", "Adaptive"});
        cbPlayerB = new JComboBox<>(new String[]{"Random", "Human", "Against Human", "Adaptive"});
        txtTrials = new JTextField(10);
        txtSpeed = new JTextField(10);
        btnStart = new JButton("Start");
        btnExit = new JButton("Exit");

        ckSelectAll = new JCheckBox("<html>Select All. <i>*Some stats cannot be disabled!</i></html>");
        ckSelectAll.setBounds(100, 340, 200, 30);
        arrCkStats = new JCheckBox[2][];
        arrCkStats[0] = new JCheckBox[2];
        arrCkStats[1] = new JCheckBox[2];
        String[] arrStatNames = {"Immediate Winner", "Player Choices", "Choice Pick Rate", "Tie Rate"};

        int i = 0;
        for (int row = 0; row < arrCkStats.length; row++) {
            for (int col = 0; col < arrCkStats[row].length; col++) {
                arrCkStats[row][col] = new JCheckBox(arrStatNames[i]);
                arrCkStats[row][col].setBounds(100, 190 + (i * 30), 200, 30);

                pnlConfig.add(arrCkStats[row][col]);
                i++;
            }
        }

        ckSelectAll.addActionListener(e -> {

            for (JCheckBox[] arrCkStat : arrCkStats) {
                for (JCheckBox jCheckBox : arrCkStat) {
                    jCheckBox.setSelected(true);
                }
            }

            ckSelectAll.setSelected(false);

        });

        lblTitle.setBounds(100, 5, 300, 30);
        lblRealTime.setBounds(30, 205, 100, 30);
        lblReport.setBounds(30, 255, 100, 30);
        lblPlayerA.setBounds(300, 70, 100, 30);
        lblPlayerB.setBounds(300, 150, 100, 30);
        lblDuration.setBounds(100, 75, 100, 30);
        lblSpeed.setBounds(300, 320, 100, 30);
        lblStats.setBounds(100, 165, 100, 30);
        cbPlayerA.setBounds(300, 100, 100, 30);
        cbPlayerB.setBounds(300, 180, 100, 30);
        rbEndless.setBounds(100, 100, 100, 30);
        rbEnding.setBounds(200, 100, 100, 30);
        txtTrials.setBounds(100, 130, 100, 30);
        txtSpeed.setBounds(300, 350, 110, 30);
        btnStart.setBounds(300, 230, 100, 30);
        btnExit.setBounds(300, 270, 100, 30);

        rbEndless.setSelected(true);
        txtTrials.setVisible(false);
        txtTrials.setText("");

        txtSpeed.setText("interval delay in ms");

        rbEndless.addActionListener(e -> {

            rbEnding.setSelected(false);
            txtTrials.setVisible(false);
            txtTrials.setText("");

        });

        rbEnding.addActionListener(e -> {

            rbEndless.setSelected(false);
            txtTrials.setVisible(true);
            txtTrials.setText("# of trials");

        });

        /*
         * This action listener method handles the logic when the "Start" button is clicked.
         * It first validates the speed and trial input fields to ensure the values are valid.
         * Then, it sets the game simulation parameters (player strategies, stats to track, etc.)
         * and starts the game simulation. It initializes the simulation panel and begins
         * the timer to run the simulation.
         */
        btnStart.addActionListener(e -> {

            // First, validate and parse the speed input
            try {
                wintIntervalDelay = Integer.parseInt(txtSpeed.getText());
                if (wintIntervalDelay <= 0) {
                    throw new NumberFormatException("Speed must be > 0");
                }
            } catch (NumberFormatException exc) {
                JOptionPane.showMessageDialog(pnlConfig, "Invalid speed");
                return;
            }

            // Next, validate and parse the trials input (allowing endless mode)
            if (txtTrials.getText().isEmpty()) {
                wintStopAtTrial = null;  // Endless mode
            } else {
                try {
                    wintStopAtTrial = Integer.parseInt(txtTrials.getText());
                    if (wintStopAtTrial <= 0) {
                        throw new NumberFormatException("Trials must be > 0");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(pnlConfig, "Invalid number of trials");
                    return;
                }
            }


            initSimulationPanel();
            objRPS = new RPS();
            objRPS.strStratA = cbPlayerA.getSelectedItem().toString();
            objRPS.strStratB = cbPlayerB.getSelectedItem().toString();
            blnImmediateWinner = arrCkStats[0][0].isSelected();
            blnPlayerChoices = arrCkStats[0][1].isSelected();
            blnChoicePickRate = arrCkStats[1][0].isSelected();
            blnTieRate = arrCkStats[1][1].isSelected();

            tmrWinRates = new Timer(wintIntervalDelay, ae -> {

                if (intTrial == (wintStopAtTrial != null ? wintStopAtTrial : -1)) {

                    tmrWinRates.stop();
                    btnEndTest.setEnabled(true);
                    btnPause.setEnabled(false);
                    return;

                }

                objRPS.simulateTrial();

                lblTrials.setText("Trial #: " + (int) objRPS.dblTotalRounds);

                if (blnImmediateWinner) {
                    if (objRPS.blnIsWinnerA != null) {
                        lblResultA.setText((objRPS.blnIsWinnerA) ? "W" : "L");
                        lblResultB.setText((!objRPS.blnIsWinnerA) ? "W" : "L");
                    } else {
                        lblResultA.setText("T");
                        lblResultB.setText("T");
                    }
                }

                if (blnPlayerChoices) {
                    lblMoveA.setText(getMoveAsString(objRPS.intMoveA));
                    lblMoveB.setText(getMoveAsString(objRPS.intMoveB));
                }

                double playerAWinRate = objRPS.dblWinRateA;
                double playerBWinRate = objRPS.dblWinRateB;

                updateWinRateDisplays(playerAWinRate, playerBWinRate);

                intTrial++;

            });

            frmMain.remove(pnlConfig);
            frmMain.add(pnlSimulation);
            frmMain.revalidate();
            frmMain.repaint();

            tmrWinRates.start();

        }); /* End of button event handler */

        btnExit.addActionListener(e -> System.exit(0));

        txtTrials.setVisible(false);
        txtTrials.setText("");

        pnlConfig.add(cbPlayerA);
        pnlConfig.add(cbPlayerB);
        pnlConfig.add(lblTitle);
        pnlConfig.add(lblRealTime);
        pnlConfig.add(lblReport);
        pnlConfig.add(lblPlayerA);
        pnlConfig.add(lblPlayerB);
        pnlConfig.add(lblDuration);
        pnlConfig.add(lblStats);
        pnlConfig.add(txtTrials);
        pnlConfig.add(btnStart);
        pnlConfig.add(btnExit);
        pnlConfig.add(rbEndless);
        pnlConfig.add(rbEnding);
        pnlConfig.add(ckSelectAll);
        pnlConfig.add(lblSpeed);
        pnlConfig.add(txtSpeed);
    }

    /**********************************************************************
     * Method name:
     * getMoveAsString
     *
     * Description:
     * This method takes an integer value representing a player's move (0, 1, or 2)
     * and returns a string corresponding to that move. It converts the integer
     * value into a human-readable string such as "Rock", "Paper", or "Scissors".
     * If the integer value is not recognized (i.e., not 0, 1, or 2), it returns "Unknown".
     *
     * Parameters:
     * Integer move: The integer value representing the player's move.
     *               Possible values are 0 for Rock, 1 for Paper, and 2 for Scissors.
     *
     * Parameter Restrictions:
     * The move should be a valid integer (0, 1, or 2). Any other value will return "Unknown".
     *
     * Return:
     * String: The string representation of the move corresponding to the integer.
     *         Returns "Rock", "Paper", "Scissors", or "Unknown" if the value is invalid.
     *
     * Return Restrictions:
     * The return value will always be a string.
     **********************************************************************/
    private String getMoveAsString(Integer move) {

        /*
         * This enhanced switch statement maps the provided integer move to its corresponding string representation.
         * - Case 0: Returns "Rock" for move 0.
         * - Case 1: Returns "Paper" for move 1.
         * - Case 2: Returns "Scissors" for move 2.
         * - Default: Returns "Unknown" for any invalid move value.
         */
        return switch (move) {
            case 0 -> "Rock";
            case 1 -> "Paper";
            case 2 -> "Scissors";
            default -> "Unknown";
        }; /* end of Enhanced Switch */
    }

    /**********************************************************************
     * Method name:
     * updateWinRateDisplays
     *
     * Description:
     * This method updates the win rate display for both players during the
     * simulation. It calculates the win rates of both players as percentages
     * based on the total win rates, then updates a progress bar and labels to
     * visually represent the win rate of each player. The progress bar's
     * direction is set based on which player has a higher win rate, and the
     * color of the progress bar is also adjusted accordingly. The win rates
     * for both players are displayed in the corresponding labels in percentage
     * format.
     *
     * Parameters:
     * double playerAWinRate: The win rate of Player A, represented as a decimal
     *                         (e.g., 0.75 for 75%).
     * double playerBWinRate: The win rate of Player B, represented as a decimal
     *                         (e.g., 0.60 for 60%).
     *
     * Parameter Restrictions:
     * Both parameters should be valid win rates as decimals, where values range
     * from 0.0 (0%) to 1.0 (100%).
     *
     * Return:
     * None
     *
     * Return Restrictions:
     * No return value. This method updates the GUI components.
     **********************************************************************/
    private void updateWinRateDisplays(double playerAWinRate, double playerBWinRate) {
        if (frmMain == null || pbWinRates == null) return; // Safety check

        // Normalize win rates to a percentage
        double totalWinRate = playerAWinRate + playerBWinRate;
        int progressA = (int) Math.round((playerAWinRate / totalWinRate) * 100);
        int progressB = (int) Math.round((playerBWinRate / totalWinRate) * 100);

        /*
         * This if-else statement adjusts the progress bar's display based on which player has the higher win rate.
         * - If Player A has a higher win rate, the progress bar's direction is set to left-to-right and the color is set to red.
         * - If Player B has a higher win rate, the progress bar's direction is set to right-to-left and the color is set to blue.
         * This ensures the visual representation matches the player's performance.
         */
        if (playerAWinRate > playerBWinRate) {
            // Player A has a higher win rate, set progress to grow left-to-right
            pbWinRates.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            pbWinRates.setValue(progressA); // Set the progress as Player A's win rate
            pbWinRates.setForeground(Color.RED); // Color for Player A (example)
        } else {
            // Player B has a higher win rate, set progress to grow right-to-left
            pbWinRates.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            pbWinRates.setValue(progressB); // Set the progress as Player B's win rate
            pbWinRates.setForeground(Color.BLUE); // Color for Player B (example)
        } /* end of if-else block */

        lblWinRateA.setText("<html><h1>" + progressA + "%</h1></html>");
        lblWinRateB.setText("<html><h1>" + progressB + "%</h1></html>");

        // Optionally repaint/refresh the GUI to show updates
        pbWinRates.repaint();
    }

    /**********************************************************************
     * Method name:
     * initSimulationPanel
     *
     * Description:
     * This method initializes and sets up the graphical user interface (GUI) for the simulation panel.
     * It creates the necessary components such as labels, buttons, and progress bars to display simulation data.
     * It also defines the layout and positions for each component within the panel.
     * Additionally, it adds action listeners for the "Pause" and "End Game" buttons to control the flow of the simulation.
     * The method finishes by adding all components to the simulation panel for display.
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
    private void initSimulationPanel() {
        pnlSimulation = new JPanel();
        pnlSimulation.setLayout(null);
        pnlSimulation.setBounds(0, 0, CONTAINER_W, CONTAINER_H);

        lblTitle.setText("<html><h1>Simulation</h1></html>");
        lblPlayerA.setText("<html><p color=\"red\"> PLAYER A</p><html>");
        lblPlayerB.setText("<html><p color=\"blue\"> PLAYER B</p><html>");
        lblTrials = new JLabel("Trial #: 0");
        lblResultA = new JLabel("<html><h2></h2></html>");
        lblResultB = new JLabel("<html><h2></h2></html>");
        lblMoveA = new JLabel("<html><i></i></html>");
        lblMoveB = new JLabel("<html><i></i></html>");
        lblWinRateA = new JLabel("<html><h1>0%</h1></html>");
        lblWinRateB = new JLabel("<html><h1>0%</h1></html>");
        pbWinRates = new JProgressBar();
        btnEndTest = new JButton("End Game");
        btnPause = new JButton("Pause");

        lblTitle.setBounds(100, 5, 300, 30);
        lblPlayerB.setBounds(450, 180, 100, 30);
        lblPlayerA.setBounds(40, 180, 100, 30);
        lblTrials.setBounds(249, 210, 100, 30);
        lblResultA.setBounds(40, 150, 100, 30);
        lblResultB.setBounds(450, 150, 100, 30);
        lblMoveA.setBounds(40, 200, 100, 30);
        lblMoveB.setBounds(450, 200, 100, 30);
        lblWinRateA.setBounds(40, 250, 300, 30);
        lblWinRateB.setBounds(450, 250, 300, 30);
        pbWinRates.setBounds(125, 180, 300, 30);
        btnEndTest.setBounds(300, 270, 100, 30);
        btnPause.setBounds(150, 270, 100, 30);
        //pbWinRates.setStringPainted(true);

        /*
         * This action listener is attached to the "Pause" button.
         * - If the button's text is "Pause", it stops the win rate timer and changes the text to "Resume".
         * - If the button's text is "Resume", it starts the win rate timer and changes the text back to "Pause".
         * This controls the simulation's pause/resume functionality.
         */
        btnPause.addActionListener(e -> {

            if (btnPause.getText().equals("Pause")) {

                tmrWinRates.stop();
                btnPause.setText("Resume");

            } else {

                tmrWinRates.start();
                btnPause.setText("Pause");

            }

        });/* end of event handler code block */

        btnEndTest.addActionListener(e -> {

            initReportPanel();
            tmrWinRates.stop();
            frmMain.remove(pnlSimulation);
            frmMain.add(pnlReport);
            frmMain.revalidate();
            frmMain.repaint();

        });

        /*
         * This line enables or disables the "End Game" button based on the value of wintStopAtTrial.
         * - If wintStopAtTrial is not null, the "End Game" button is disabled (until all trials are completed).
         * - If wintStopAtTrial is null, the button remains enabled (since null means endless).
         */
        btnEndTest.setEnabled(wintStopAtTrial == null);

        pnlSimulation.add(lblTitle);
        pnlSimulation.add(lblPlayerA);
        pnlSimulation.add(lblPlayerB);
        pnlSimulation.add(lblTrials);
        pnlSimulation.add(lblResultA);
        pnlSimulation.add(lblResultB);
        pnlSimulation.add(lblMoveA);
        pnlSimulation.add(lblMoveB);
        pnlSimulation.add(pbWinRates);
        pnlSimulation.add(btnEndTest);
        pnlSimulation.add(btnPause);
        pnlSimulation.add(lblWinRateA);
        pnlSimulation.add(lblWinRateB);

    }

    /**********************************************************************
     * Method name:
     * initReportPanel
     *
     * Description:
     * This method initializes and sets up the graphical user interface (GUI) for the report panel.
     * It creates and configures the necessary components such as labels, buttons, and a table to display
     * the simulation results. The method handles the creation of rows with various statistical data
     * (such as wins, losses, tie rates, and player strategies) and adds them to a table that will be
     * displayed in the report panel. It also adds action listeners to buttons, such as a button to start
     * a new test, which resets the simulation and opens a fresh instance of the GUI.
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
    private void initReportPanel() {
        pnlReport = new JPanel();
        pnlReport.setLayout(null);
        pnlReport.setBounds(0, 0, CONTAINER_W, CONTAINER_H);

        lblTitle.setText("<html><h1>Report</h1></html>");
        lblTitle.setBounds(100, 5, 300, 30);

        btnNewTest = new JButton("+ New Test");
        btnNewTest.setBounds(247, 310, 100, 30);

        pnlReport.add(lblTitle);
        pnlReport.add(btnNewTest);

        /*
         * This action listener is attached to the "+ New Test" button.
         * - When clicked, it disposes of the main frame, nullifies the objRPS object, and reinitializes the GUI
         *   by calling initGUI(), effectively starting a new simulation.
         */
        btnNewTest.addActionListener(e -> {

            frmMain.dispose();
            objRPS = null;
            initGUI();

        });/* end of event handler code block */

        // Base rows: Total Rounds, Wins, Losses, Ties, Win Rate (5 rows always included)
        int baseRows = 6;
        int optionalRows = 0;
        if (blnTieRate) {
            optionalRows += 1;
        }
        if (blnChoicePickRate) {
            optionalRows += 3;
        }
        int extraRows = 4; // For Max Win Streak, Max Lose Streak, Max Tie Streak, and Entropy
        int totalRows = baseRows + optionalRows + extraRows;
        String[][] tableData = new String[totalRows][3];

        /*
         * These block populates the table with core statistics: completed trials, wins, losses, ties, and win rates.
         * - Each row in the table represents a specific statistic, with values for both players (Player A and Player B).
         * - The data is gathered from objRPS fields and formatted accordingly.
         */
        int rowIndex = 0;

        tableData[rowIndex][0] = "Completed Trials";
        tableData[rowIndex][1] = String.valueOf((int) objRPS.dblTotalRounds);
        tableData[rowIndex][2] = String.valueOf((int) objRPS.dblTotalRounds);
        rowIndex++;

        tableData[rowIndex][0] = "Wins";
        tableData[rowIndex][1] = String.valueOf((int) objRPS.dblWinsA);
        tableData[rowIndex][2] = String.valueOf((int) objRPS.dblWinsB);
        rowIndex++;

        tableData[rowIndex][0] = "Losses";
        tableData[rowIndex][1] = String.valueOf(objRPS.intLossesA);
        tableData[rowIndex][2] = String.valueOf(objRPS.intLossesB);
        rowIndex++;

        tableData[rowIndex][0] = "Ties";
        tableData[rowIndex][1] = String.valueOf(objRPS.intTies);
        tableData[rowIndex][2] = String.valueOf(objRPS.intTies);
        rowIndex++;

        tableData[rowIndex][0] = "Win Rate";
        tableData[rowIndex][1] = String.format("%.2f%%", objRPS.dblWinRateA * 100);
        tableData[rowIndex][2] = String.format("%.2f%%", objRPS.dblWinRateB * 100);
        rowIndex++;

        tableData[rowIndex][0] = "Strategy";
        tableData[rowIndex][1] = objRPS.strStratA;
        tableData[rowIndex][2] = objRPS.strStratB;
        rowIndex++;
        /* end of code block */

        /*
         * This block optionally populates the table with additional statistics if enabled, such as tie rates and
         * player move pick rates (Rock, Paper, Scissors).
         * - If blnTieRate or blnChoicePickRate are true, the corresponding rows are added to the table.
         */
        if (blnTieRate) {
            tableData[rowIndex][0] = "Tie Rate";
            tableData[rowIndex][1] = String.format("%.2f%%", objRPS.dblTieRate * 100);
            tableData[rowIndex][2] = String.format("%.2f%%", objRPS.dblTieRate * 100);
            rowIndex++;
        }

        // Optional: Player Move Rates
        if (blnChoicePickRate) {
            tableData[rowIndex][0] = "Rock Pick Rate";
            tableData[rowIndex][1] = String.format("%.2f%%", objRPS.dblRockPickRateA * 100);
            tableData[rowIndex][2] = String.format("%.2f%%", objRPS.dblRockPickRateB * 100);
            rowIndex++;

            tableData[rowIndex][0] = "Paper Pick Rate";
            tableData[rowIndex][1] = String.format("%.2f%%", objRPS.dblPaperPickRateA * 100);
            tableData[rowIndex][2] = String.format("%.2f%%", objRPS.dblPaperPickRateB * 100);
            rowIndex++;

            tableData[rowIndex][0] = "Scissors Pick Rate";
            tableData[rowIndex][1] = String.format("%.2f%%", objRPS.dblScissorsPickRateA * 100);
            tableData[rowIndex][2] = String.format("%.2f%%", objRPS.dblScissorsPickRateB * 100);
            rowIndex++;
        }
        /* end of code block */

        /*
         * These rows populate the table with additional statistics like max win streak, max lose streak, and entropy.
         * - These values are obtained from objRPS fields and added to the report table model.
         */
        tableData[rowIndex][0] = "Max Win Streak";
        tableData[rowIndex][1] = String.valueOf(objRPS.intMaxWinStreakA);
        tableData[rowIndex][2] = String.valueOf(objRPS.intMaxWinStreakB);
        rowIndex++;

        tableData[rowIndex][0] = "Max Lose Streak";
        tableData[rowIndex][1] = String.valueOf(objRPS.intMaxLoseStreakA);
        tableData[rowIndex][2] = String.valueOf(objRPS.intMaxLoseStreakB);
        rowIndex++;

        tableData[rowIndex][0] = "Max Tie Streak";
        tableData[rowIndex][1] = String.valueOf(objRPS.intMaxTieStreak);
        tableData[rowIndex][2] = String.valueOf(objRPS.intMaxTieStreak);
        rowIndex++;

        tableData[rowIndex][0] = "Entropy";
        tableData[rowIndex][1] = String.format("%.2f", objRPS.dblEntropyA);
        tableData[rowIndex][2] = String.format("%.2f", objRPS.dblEntropyB);
        /* end of code block */

        // Create the table with header columns "Player A" and "Player B".
        String[] columnNames = {"STAT", "Player A", "Player B"};
        tblReport = new JTable(tableData, columnNames);
        tblReport.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(tblReport);
        scrollPane.setBounds(100, 50, 400, 300);
        pnlReport.add(scrollPane);

    }

}
