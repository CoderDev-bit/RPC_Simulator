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

    void initGUI() {
        frmMain = new JFrame("GameTest™");
        frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmMain.setBounds(10, 10, CONTAINER_W, CONTAINER_H);

        initConfigPanel();

        frmMain.add(pnlConfig);
        frmMain.setVisible(true);
    }

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

        });

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

    private String getMoveAsString(Integer move) {
        return switch (move) {
            case 0 -> "Rock";
            case 1 -> "Paper";
            case 2 -> "Scissors";
            default -> "Unknown";
        };
    }

    private void updateWinRateDisplays(double playerAWinRate, double playerBWinRate) {
        if (frmMain == null || pbWinRates == null) return; // Safety check

        // Normalize win rates to a percentage
        double totalWinRate = playerAWinRate + playerBWinRate;
        int progressA = (int) Math.round((playerAWinRate / totalWinRate) * 100);
        int progressB = (int) Math.round((playerBWinRate / totalWinRate) * 100);

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
        }

        lblWinRateA.setText("<html><h1>" + progressA + "%</h1></html>");
        lblWinRateB.setText("<html><h1>" + progressB + "%</h1></html>");

        // Optionally repaint/refresh the GUI to show updates
        pbWinRates.repaint();
    }

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

        btnPause.addActionListener(e -> {

            if (btnPause.getText().equals("Pause")) {

                tmrWinRates.stop();
                btnPause.setText("Resume");

            } else {

                tmrWinRates.start();
                btnPause.setText("Pause");

            }

        });

        btnEndTest.addActionListener(e -> {

            initReportPanel();
            tmrWinRates.stop();
            frmMain.remove(pnlSimulation);
            frmMain.add(pnlReport);
            frmMain.revalidate();
            frmMain.repaint();

        });

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

        btnNewTest.addActionListener(e -> {

            frmMain.dispose();
            objRPS = null;
            initGUI();

        });

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


        // Create the table with header columns "Player A" and "Player B".
        String[] columnNames = {"STAT", "Player A", "Player B"};
        tblReport = new JTable(tableData, columnNames);
        tblReport.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(tblReport);
        scrollPane.setBounds(100, 50, 400, 300);
        pnlReport.add(scrollPane);

    }

}
