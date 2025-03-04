package app;

import javax.swing.*;
import java.awt.*;

import static java.awt.Color.red;

public class GUI {

    JFrame frmMain;
    final int FRAME_WIDTH = 550, FRAME_HEIGHT = 450;
    JCheckBox[][] arrCkStats;
    JPanel pnlConfig, pnlSimulation, pnlReport;
    JButton btnStart, btnExit, btnEndTest;
    JComboBox<String> cbA, cbB;
    JLabel lblTitle, lblRealTime, lblReport, lblA, lblB, lblDuration, lblStats, lblTrials, lblResultA, lblResultB, lblMoveA, lblMoveB, lblWinRateA, lblWinRateB;
    JTextField txtTrials;
    JProgressBar pbWinRates;
    JTable tblStats;
    boolean blnImmediateWinner, blnPlayerChoices, blnChoicePickRate, blnTieRate;
    Timer tmrWinRates;
    RPS objTest;

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
        lblDuration = new JLabel("<html><h3>DURATION:</h3></html>");
        lblStats = new JLabel("<html><h3>STATS:</h3></html>");
        cbA = new JComboBox<>(new String[]{"Random", "Human", "Against Human", "Adaptive"});
        cbB = new JComboBox<>(new String[]{"Random", "Human", "Against Human", "Adaptive"});
        txtTrials = new JTextField(10);
        btnStart = new JButton("Start Test");
        btnExit = new JButton("Exit");

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

        lblTitle.setBounds(100, 5, 300, 30);
        lblRealTime.setBounds(30, 205, 100, 30);
        lblReport.setBounds(30, 255, 100, 30);
        lblA.setBounds(300, 70, 100, 30);
        lblB.setBounds(300, 150, 100, 30);
        lblDuration.setBounds(100, 75, 100, 30);
        lblStats.setBounds(100, 165, 100, 30);
        cbA.setBounds(300, 100, 100, 30);
        cbB.setBounds(300, 180, 100, 30);
        txtTrials.setBounds(100, 130, 100, 30);
        btnStart.setBounds(300, 230, 100, 30);
        btnExit.setBounds(300, 270, 100, 30);


        btnStart.addActionListener(e -> {

            initSimulationPanel();
            objTest = new RPS();
            objTest.strStratA = cbA.getSelectedItem().toString();
            objTest.strStratB = cbB.getSelectedItem().toString();
            blnImmediateWinner = arrCkStats[0][0].isSelected();
            blnPlayerChoices = arrCkStats[0][1].isSelected();
            blnChoicePickRate = arrCkStats[1][0].isSelected();
            blnTieRate = arrCkStats[1][1].isSelected();

            tmrWinRates = new Timer(100, ae -> {

                objTest.simulateTrial();

                lblTrials.setText("Trial #: " + String.valueOf(objTest.totalRounds));

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
    }

    private String getMoveAsString(Integer move) {
        switch (move) {
            case 0:
                return "Rock";
            case 1:
                return "Paper";
            case 2:
                return "Scissors";
            default:
                return "Unknown";
        }
    }

    public void updateWinRateDisplays(double playerAWinRate, double playerBWinRate) {
        if (frmMain == null || pbWinRates == null) return; // Safety check

        // Normalize win rates to a percentage
        double totalWinRate = playerAWinRate + playerBWinRate;
        int progressA = (int) ((playerAWinRate / totalWinRate) * 100);
        int progressB = (int) ((playerBWinRate / totalWinRate) * 100);

        if (playerAWinRate > playerBWinRate) {
            // Player A has a higher win rate, set progress to grow left-to-right
            pbWinRates.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            pbWinRates.setValue(progressA); // Set the progress as Player A's win rate
            pbWinRates.setForeground(Color.BLUE); // Color for Player A (example)
        } else {
            // Player B has a higher win rate, set progress to grow right-to-left
            pbWinRates.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            pbWinRates.setValue(progressB); // Set the progress as Player B's win rate
            pbWinRates.setForeground(Color.RED); // Color for Player B (example)
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
        lblA.setText("<html><p color=\"red\"> Player A</p><html>");
        lblB.setText("<html><p color=\"blue\"> Player B</p><html>");
        lblTrials = new JLabel("Trial #: 0");
        lblResultA = new JLabel("<html><h2></h2></html>");
        lblResultB = new JLabel("<html><h2></h2></html>");
        lblMoveA = new JLabel("<html><i></i></html>");
        lblMoveB = new JLabel("<html><i></i></html>");
        lblWinRateA = new JLabel("<html><h1>0%</h1></html>");
        lblWinRateB = new JLabel("<html><h1>0%</h1></html>");
        pbWinRates = new JProgressBar();
        btnEndTest = new JButton("End Test");

        lblTitle.setBounds(100, 5, 300, 30);
        lblB.setBounds(450,180,100,30);
        lblA.setBounds(40,180,100,30);
        lblTrials.setBounds(200,230,100,30);
        lblResultA.setBounds(40, 150, 100, 30);
        lblResultB.setBounds(450, 150, 100, 30);
        lblMoveA.setBounds(40, 200, 100, 30);
        lblMoveB.setBounds(450, 200, 100, 30);
        lblWinRateA.setBounds(40, 250, 300, 30);
        lblWinRateB.setBounds(450, 250, 300, 30);
        pbWinRates.setBounds(125, 180, 300, 30);
        btnEndTest.setBounds(300, 270, 100, 30);

        btnEndTest.addActionListener(e -> {

            initReportPanel();
            frmMain.remove(pnlSimulation);
            frmMain.add(pnlReport);
            frmMain.revalidate();
            frmMain.repaint();

        });

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
        pnlSimulation.add(lblWinRateA);
        pnlSimulation.add(lblWinRateB);

    }

    void initReportPanel() {
        pnlReport = new JPanel();
        pnlReport.setLayout(null);
        pnlReport.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);

        lblTitle.setText("<html><h1>Report</h1></html>");
        lblTitle.setBounds(100, 5, 300, 30);
        pnlReport.add(lblTitle);

        // Base rows: Total Rounds, Wins, Losses, Ties, Win Rate (5 rows always included)
        int baseRows = 5;
        int optionalRows = 0;
        if (blnTieRate) {
            optionalRows += 1;
        }
        if (blnChoicePickRate) {
            optionalRows += 3;
        }
        int totalRows = baseRows + optionalRows;

        String[][] tableData = new String[totalRows][2];
        int rowIndex = 0;

        // Total Rounds row (common stat, so same value for both players)
        tableData[rowIndex][0] = "Total Rounds: " + objTest.totalRounds;
        tableData[rowIndex][1] = "Total Rounds: " + objTest.totalRounds;
        rowIndex++;

        // Wins row.
        tableData[rowIndex][0] = "Wins: " + objTest.winsA;
        tableData[rowIndex][1] = "Wins: " + objTest.winsB;
        rowIndex++;

        // Losses row.
        tableData[rowIndex][0] = "Losses: " + objTest.lossesA;
        tableData[rowIndex][1] = "Losses: " + objTest.lossesB;
        rowIndex++;

        // Ties row (common stat).
        tableData[rowIndex][0] = "Ties: " + objTest.ties;
        tableData[rowIndex][1] = "Ties: " + objTest.ties;
        rowIndex++;

        // Win Rate row.
        tableData[rowIndex][0] = "Win Rate: " + String.format("%.2f%%", objTest.dblWinRateA * 100);
        tableData[rowIndex][1] = "Win Rate: " + String.format("%.2f%%", objTest.dblWinRateB * 100);
        rowIndex++;

        // Optionally include Tie Rate if enabled.
        if (blnTieRate) {
            tableData[rowIndex][0] = "Tie Rate: " + String.format("%.2f%%", objTest.dblTieRate * 100);
            tableData[rowIndex][1] = "Tie Rate: " + String.format("%.2f%%", objTest.dblTieRate * 100);
            rowIndex++;
        }

        // Optionally include Player Move Rates if enabled.
        if (blnChoicePickRate) {
            // Rock Pick Rate row.
            tableData[rowIndex][0] = "Rock Pick Rate: " + String.format("%.2f%%", objTest.dblRockPickRateA * 100);
            tableData[rowIndex][1] = "Rock Pick Rate: " + String.format("%.2f%%", objTest.dblRockPickRateB * 100);
            rowIndex++;
            // Paper Pick Rate row.
            tableData[rowIndex][0] = "Paper Pick Rate: " + String.format("%.2f%%", objTest.dblPaperPickRateA * 100);
            tableData[rowIndex][1] = "Paper Pick Rate: " + String.format("%.2f%%", objTest.dblPaperPickRateB * 100);
            rowIndex++;
            // Scissors Pick Rate row.
            tableData[rowIndex][0] = "Scissors Pick Rate: " + String.format("%.2f%%", objTest.dblScissorsPickRateA * 100);
            tableData[rowIndex][1] = "Scissors Pick Rate: " + String.format("%.2f%%", objTest.dblScissorsPickRateB * 100);
            rowIndex++;
        }

        // Create the table with header columns "Player A" and "Player B".
        String[] columnNames = {"Player A", "Player B"};
        tblStats = new JTable(tableData, columnNames);
        JScrollPane scrollPane = new JScrollPane(tblStats);
        scrollPane.setBounds(100, 50, 400, 300);
        pnlReport.add(scrollPane);

    }


}
