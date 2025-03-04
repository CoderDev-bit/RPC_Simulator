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
    JRadioButton rbEndless, rbEnding;
    JLabel lblTitle, lblRealTime, lblReport, lblA, lblB, lblDuration, lblStats, lblTrials, lblResultA, lblResultB, lblMoveA, lblMoveB;
    JTextField txtTrials;
    JProgressBar pbWinRates;
    Timer tmrWinRates;
    JTable tblStats;
    RPS objTest;
    Integer IntStopAtTrial;

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
        rbEndless = new JRadioButton("Endless");
        rbEnding = new JRadioButton("Ending");
        cbA = new JComboBox<>(new String[]{"Random", "Human", "Against Human", "Adaptive"});
        cbB = new JComboBox<>(new String[]{"Random", "Human", "Against Human", "Adaptive"});
        txtTrials = new JTextField(10);
        btnStart = new JButton("Start Test");
        btnExit = new JButton("Exit");

        arrCkStats = new JCheckBox[2][];
        arrCkStats[0] = new JCheckBox[2];
        arrCkStats[1] = new JCheckBox[4];
        String[] arrStatNames = {"Immediate Winner", "Player Choices", "Overall Winner", "Choice Pick Rate", "Tie Rate", "Streaks"};

        int i = 0;
        for (int row = 0; row < arrCkStats.length; row++) {
            for (int col = 0; col < arrCkStats[row].length; col++) {
                arrCkStats[row][col] = new JCheckBox(arrStatNames[i]);
                arrCkStats[row][col].setBounds(100, 190 + (row * 30) + (col * 30), 200, 30);
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
        rbEndless.setBounds(100, 100, 100, 30);
        rbEnding.setBounds(200, 100, 100, 30);
        cbA.setBounds(300, 100, 100, 30);
        cbB.setBounds(300, 180, 100, 30);
        txtTrials.setBounds(100, 130, 100, 30);
        btnStart.setBounds(300, 230, 100, 30);
        btnExit.setBounds(300, 270, 100, 30);

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
            try {
                IntStopAtTrial = Integer.parseInt(txtTrials.getText());
                if (IntStopAtTrial <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                if (txtTrials.getText().equals("")) {
                    IntStopAtTrial = null;
                } else {
                    JOptionPane.showMessageDialog(pnlConfig, "Invalid number of trials");
                    return;
                }
            }

            initSimulationPanel();
            objTest = new RPS();
            objTest.strStratA = cbA.getSelectedItem().toString();
            objTest.strStratB = cbB.getSelectedItem().toString();
            //objTest.simulateGame(1000);

            tmrWinRates = new Timer(1, ae -> {

                objTest.simulateTrial();

                lblTrials.setText(String.valueOf(objTest.totalRounds));

                double playerAWinRate = objTest.dblWinRateA;
                double playerBWinRate = objTest.dblWinRateB;

                updateWinRateProgressBar(playerAWinRate, playerBWinRate);

            });

            frmMain.remove(pnlConfig);
            frmMain.add(pnlSimulation);
            frmMain.revalidate();
            frmMain.repaint();

            //tmrWinRates.start();

        });

        btnExit.addActionListener(e -> System.exit(0));

        rbEndless.setSelected(true);
        txtTrials.setVisible(false);
        txtTrials.setText("");

        pnlConfig.add(rbEndless);
        pnlConfig.add(rbEnding);
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

    public void updateWinRateProgressBar(double playerAWinRate, double playerBWinRate) {
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
        lblTrials = new JLabel("0");
        lblResultA = new JLabel("<html><h2>L</h2></html>");
        lblResultB = new JLabel("<html><h2>L</h2></html>");
        lblMoveA = new JLabel("<html><h3><i>Rock</h3></i></html>");
        lblMoveB = new JLabel("<html><h3><i>Paper</i></h3></html>");
        pbWinRates = new JProgressBar();
        btnEndTest = new JButton("End Test");

        lblTitle.setBounds(100, 5, 300, 30);
        lblB.setBounds(450,180,100,30);
        lblA.setBounds(40,180,100,30);
        lblTrials.setBounds(200,200,100,30);
        lblResultA.setBounds(40, 150, 100, 30);
        lblResultB.setBounds(450, 150, 100, 30);
        lblMoveA.setBounds(40, 100, 100, 30);
        lblMoveB.setBounds(450, 100, 100, 30);
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

    }

    void initReportPanel() {
        pnlReport = new JPanel();
        pnlReport.setLayout(null);
        pnlReport.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);

        tblStats = new JTable();
        lblTitle.setText("<html><h1>Report</h1></html>");

        lblTitle.setBounds(100, 5, 300, 30);
        tblStats.setBounds(100, 50, 400, 300);

        pnlReport.add(lblTitle);
        pnlReport.add(tblStats);
    }
}
