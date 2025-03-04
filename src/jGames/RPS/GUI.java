package jGames.RPS;

import javax.swing.*;
import java.awt.*;

public class GUI {

    JFrame frmMain;
    final int FRAME_WIDTH = 550, FRAME_HEIGHT = 450;
    JCheckBox[][] arrCkStats;
    JCheckBox ckAll;
    JPanel pnlConfig, pnlSimulation, pnlReport;
    JButton btnStart, btnPause, btnExit, btnEndTest, btnNewTest;
    JComboBox<String> cbA, cbB;
    JRadioButton rbEndless, rbEnding;
    JLabel lblTitle, lblRealTime, lblReport, lblA, lblB, lblSpeed, lblDuration, lblStats, lblTrials, lblResultA, lblResultB, lblMoveA, lblMoveB, lblWinRateA, lblWinRateB;
    JTextField txtTrials, txtSpeed;
    JProgressBar pbWinRates;
    JTable tblStats;
    boolean blnImmediateWinner, blnPlayerChoices, blnChoicePickRate, blnTieRate;
    Timer tmrWinRates;
    RPS objTest;
    Integer IntIntervalDelay;
    Integer IntStopAtTrial; int trial;

    void initGUI() {
        frmMain = new JFrame("GameTest™");
        frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmMain.setBounds(10, 10, FRAME_WIDTH, FRAME_HEIGHT);

        initConfigPanel();

        frmMain.add(pnlConfig);
        frmMain.setVisible(true);
    }

    void initConfigPanel() {
        pnlConfig = new JPanel();
        pnlConfig.setLayout(null);
        pnlConfig.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);

        lblTitle = new JLabel("<html><h1>Configuration</h1></html>");
        lblRealTime = new JLabel("<html><i>Real Time</i></html>");
        lblReport = new JLabel("<html><i>Report</i></html>");
        lblA = new JLabel("<html><i>Player A Strategy</html></i>");
        lblB = new JLabel("<html><i>Player B Strategy</html></i>");
        lblSpeed = new JLabel("<html><h3>SPEED:</h3></html>");
        lblDuration = new JLabel("<html><h3>DURATION:</h3></html>");
        lblStats = new JLabel("<html><h3>STATS:</h3></html>");
        rbEndless = new JRadioButton("Endless");
        rbEnding = new JRadioButton("Ending");
        cbA = new JComboBox<>(new String[]{"Random", "Human", "Against Human", "Adaptive"});
        cbB = new JComboBox<>(new String[]{"Random", "Human", "Against Human", "Adaptive"});
        txtTrials = new JTextField(10);
        txtSpeed = new JTextField(10);
        btnStart = new JButton("Start Test");
        btnExit = new JButton("Exit");

        ckAll = new JCheckBox("<html>Select All. <i>*Some stats cannot be disabled!</i></html>");
        ckAll.setBounds(100, 340, 200, 30);
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

        ckAll.addActionListener(e -> {

            for (JCheckBox[] arrCkStat : arrCkStats) {
                for (JCheckBox jCheckBox : arrCkStat) {
                    jCheckBox.setSelected(true);
                }
            }

            ckAll.setSelected(false);

        });

        lblTitle.setBounds(100, 5, 300, 30);
        lblRealTime.setBounds(30, 205, 100, 30);
        lblReport.setBounds(30, 255, 100, 30);
        lblA.setBounds(300, 70, 100, 30);
        lblB.setBounds(300, 150, 100, 30);
        lblDuration.setBounds(100, 75, 100, 30);
        lblSpeed.setBounds(300, 320, 100, 30);
        lblStats.setBounds(100, 165, 100, 30);
        cbA.setBounds(300, 100, 100, 30);
        cbB.setBounds(300, 180, 100, 30);
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
                IntIntervalDelay = Integer.parseInt(txtSpeed.getText());
                if (IntIntervalDelay <= 0) {
                    throw new NumberFormatException("Speed must be > 0");
                }
            } catch (NumberFormatException exc) {
                JOptionPane.showMessageDialog(pnlConfig, "Invalid speed");
                return;
            }

// Next, validate and parse the trials input (allowing endless mode)
            if (txtTrials.getText().isEmpty()) {
                IntStopAtTrial = null;  // Endless mode
            } else {
                try {
                    IntStopAtTrial = Integer.parseInt(txtTrials.getText());
                    if (IntStopAtTrial <= 0) {
                        throw new NumberFormatException("Trials must be > 0");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(pnlConfig, "Invalid number of trials");
                    return;
                }
            }


            initSimulationPanel();
            objTest = new RPS();
            objTest.strStratA = cbA.getSelectedItem().toString();
            objTest.strStratB = cbB.getSelectedItem().toString();
            blnImmediateWinner = arrCkStats[0][0].isSelected();
            blnPlayerChoices = arrCkStats[0][1].isSelected();
            blnChoicePickRate = arrCkStats[1][0].isSelected();
            blnTieRate = arrCkStats[1][1].isSelected();

            tmrWinRates = new Timer(IntIntervalDelay, ae -> {

                if (trial == (IntStopAtTrial != null ? IntStopAtTrial : -1)) {

                    tmrWinRates.stop();
                    btnEndTest.setEnabled(true);
                    btnPause.setEnabled(false);
                    return;

                }

                objTest.simulateTrial();

                lblTrials.setText("Trial #: " + (int) objTest.totalRounds);

                if (blnImmediateWinner) {
                    if (objTest.blnIsWinnerA != null) {
                        lblResultA.setText((objTest.blnIsWinnerA) ? "W" : "L");
                        lblResultB.setText((!objTest.blnIsWinnerA) ? "W" : "L");
                    } else {
                        lblResultA.setText("T");
                        lblResultB.setText("T");
                    }
                }

                if (blnPlayerChoices) {
                    lblMoveA.setText(getMoveAsString(objTest.intMoveA));
                    lblMoveB.setText(getMoveAsString(objTest.intMoveB));
                }

                double playerAWinRate = objTest.dblWinRateA;
                double playerBWinRate = objTest.dblWinRateB;

                updateWinRateDisplays(playerAWinRate, playerBWinRate);

                trial++;

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

        pnlConfig.add(cbA);
        pnlConfig.add(cbB);
        pnlConfig.add(lblTitle);
        pnlConfig.add(lblRealTime);
        pnlConfig.add(lblReport);
        pnlConfig.add(lblA);
        pnlConfig.add(lblB);
        pnlConfig.add(lblDuration);
        pnlConfig.add(lblStats);
        pnlConfig.add(txtTrials);
        pnlConfig.add(btnStart);
        pnlConfig.add(btnExit);
        pnlConfig.add(rbEndless);
        pnlConfig.add(rbEnding);
        pnlConfig.add(ckAll);
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

    public void updateWinRateDisplays(double playerAWinRate, double playerBWinRate) {
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

    void initSimulationPanel() {
        pnlSimulation = new JPanel();
        pnlSimulation.setLayout(null);
        pnlSimulation.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);

        lblTitle.setText("<html><h1>Simulation</h1></html>");
        lblA.setText("<html><p color=\"red\"> PLAYER A</p><html>");
        lblB.setText("<html><p color=\"blue\"> PLAYER B</p><html>");
        lblTrials = new JLabel("Trial #: 0");
        lblResultA = new JLabel("<html><h2></h2></html>");
        lblResultB = new JLabel("<html><h2></h2></html>");
        lblMoveA = new JLabel("<html><i></i></html>");
        lblMoveB = new JLabel("<html><i></i></html>");
        lblWinRateA = new JLabel("<html><h1>0%</h1></html>");
        lblWinRateB = new JLabel("<html><h1>0%</h1></html>");
        pbWinRates = new JProgressBar();
        btnEndTest = new JButton("End Test");
        btnPause = new JButton("Pause");

        lblTitle.setBounds(100, 5, 300, 30);
        lblB.setBounds(450,180,100,30);
        lblA.setBounds(40,180,100,30);
        lblTrials.setBounds(249,210,100,30);
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

        btnEndTest.setEnabled(IntStopAtTrial == null);

        pnlSimulation.add(lblTitle);
        pnlSimulation.add(lblA);
        pnlSimulation.add(lblB);
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

    void initReportPanel() {
        pnlReport = new JPanel();
        pnlReport.setLayout(null);
        pnlReport.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);

        lblTitle.setText("<html><h1>Report</h1></html>");
        lblTitle.setBounds(100, 5, 300, 30);

        btnNewTest = new JButton("+ New Test");
        btnNewTest.setBounds(247, 310, 100, 30);
        //scrollPane.setBounds(100, 50, 400, 300);

        pnlReport.add(lblTitle);
        pnlReport.add(btnNewTest);

        btnNewTest.addActionListener(e -> {

            frmMain.dispose();
            objTest = null;
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

// Total Rounds row
        tableData[rowIndex][0] = "Completed Trials";
        tableData[rowIndex][1] = String.valueOf((int) objTest.totalRounds);
        tableData[rowIndex][2] = String.valueOf((int) objTest.totalRounds);
        rowIndex++;

// Wins row
        tableData[rowIndex][0] = "Wins";
        tableData[rowIndex][1] = String.valueOf((int) objTest.winsA);
        tableData[rowIndex][2] = String.valueOf((int) objTest.winsB);
        rowIndex++;

// Losses row
        tableData[rowIndex][0] = "Losses";
        tableData[rowIndex][1] = String.valueOf(objTest.lossesA);
        tableData[rowIndex][2] = String.valueOf(objTest.lossesB);
        rowIndex++;

// Ties row
        tableData[rowIndex][0] = "Ties";
        tableData[rowIndex][1] = String.valueOf(objTest.ties);
        tableData[rowIndex][2] = String.valueOf(objTest.ties);
        rowIndex++;

// Win Rate row
        tableData[rowIndex][0] = "Win Rate";
        tableData[rowIndex][1] = String.format("%.2f%%", objTest.dblWinRateA * 100);
        tableData[rowIndex][2] = String.format("%.2f%%", objTest.dblWinRateB * 100);
        rowIndex++;

// Strategies row
        tableData[rowIndex][0] = "Strategy";
        tableData[rowIndex][1] = objTest.strStratA;
        tableData[rowIndex][2] = objTest.strStratB;
        rowIndex++;

// Optional: Tie Rate row
        if (blnTieRate) {
            tableData[rowIndex][0] = "Tie Rate";
            tableData[rowIndex][1] = String.format("%.2f%%", objTest.dblTieRate * 100);
            tableData[rowIndex][2] = String.format("%.2f%%", objTest.dblTieRate * 100);
            rowIndex++;
        }

// Optional: Player Move Rates
        if (blnChoicePickRate) {
            tableData[rowIndex][0] = "Rock Pick Rate";
            tableData[rowIndex][1] = String.format("%.2f%%", objTest.dblRockPickRateA * 100);
            tableData[rowIndex][2] = String.format("%.2f%%", objTest.dblRockPickRateB * 100);
            rowIndex++;

            tableData[rowIndex][0] = "Paper Pick Rate";
            tableData[rowIndex][1] = String.format("%.2f%%", objTest.dblPaperPickRateA * 100);
            tableData[rowIndex][2] = String.format("%.2f%%", objTest.dblPaperPickRateB * 100);
            rowIndex++;

            tableData[rowIndex][0] = "Scissors Pick Rate";
            tableData[rowIndex][1] = String.format("%.2f%%", objTest.dblScissorsPickRateA * 100);
            tableData[rowIndex][2] = String.format("%.2f%%", objTest.dblScissorsPickRateB * 100);
            rowIndex++;
        }

        // Max Win Streak row.
        tableData[rowIndex][0] = "Max Win Streak";
        tableData[rowIndex][1] = String.valueOf(objTest.maxWinStreakA);
        tableData[rowIndex][2] = String.valueOf(objTest.maxWinStreakB);
        rowIndex++;

// Max Lose Streak row.
        tableData[rowIndex][0] = "Max Lose Streak";
        tableData[rowIndex][1] = String.valueOf(objTest.maxLoseStreakA);
        tableData[rowIndex][2] = String.valueOf(objTest.maxLoseStreakB);
        rowIndex++;

// Max Tie Streak row.
        tableData[rowIndex][0] = "Max Tie Streak";
        tableData[rowIndex][1] = String.valueOf(objTest.maxTieStreak);
        tableData[rowIndex][2] = String.valueOf(objTest.maxTieStreak);
        rowIndex++;

// Entropy row.
        tableData[rowIndex][0] = "Entropy";
        tableData[rowIndex][1] = String.format("%.2f", objTest.entropyA);
        tableData[rowIndex][2] = String.format("%.2f", objTest.entropyB);
        rowIndex++;



        // Create the table with header columns "Player A" and "Player B".
        String[] columnNames = {"STAT", "Player A", "Player B"};
        tblStats = new JTable(tableData, columnNames);
        tblStats.setEnabled(false);
        //tblStats.setDefaultEditor(Object.class, null);
        JScrollPane scrollPane = new JScrollPane(tblStats);
        scrollPane.setBounds(100, 50, 400, 300);
        pnlReport.add(scrollPane);

    }


}
