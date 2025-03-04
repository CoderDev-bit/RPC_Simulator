package app;

import javax.swing.*;

public class GUI {

    JFrame frmMain;
    final int FRAME_WIDTH = 550, FRAME_HEIGHT = 450;
    JCheckBox[][] arrCkStats;
    JPanel pnlConfig, pnlSimulation, pnlReport;
    JButton btnStart, btnBack;
    JComboBox<String> cbA, cbB;
    JRadioButton rbEndless, rbEnding;
    JLabel lblTitle, lblRealTime, lblA, lblB, lblDuration, lblStats, lblTrials;
    JTextField txtTrials;
    JProgressBar pbWinRates;
    Timer tmrWinRates;
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
        btnBack = new JButton("Exit");

        arrCkStats = new JCheckBox[2][];
        arrCkStats[0] = new JCheckBox[2];
        arrCkStats[1] = new JCheckBox[5];
        String[] arrStatNames = {"Immediate Winner", "Player Choices", "Overall Winner", "Choice Win Rate", "Choice Pick Rate", "Tie Rate", "Streaks"};

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
        btnBack.setBounds(300, 270, 100, 30);

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

            tmrWinRates = new Timer(500, ae -> {

                objTest.simulateTrial();
                pbWinRates.setValue((int) (objTest.dblWinRateA * 100));
                lblTrials.setText("" + objTest.totalRounds);

            });

            frmMain.remove(pnlConfig);
            frmMain.add(pnlSimulation);
            frmMain.revalidate();
            frmMain.repaint();

            tmrWinRates.start();

        });

        btnBack.addActionListener(e -> System.exit(0));

        rbEndless.setSelected(true);
        txtTrials.setVisible(false);
        txtTrials.setText("");

        pnlConfig.add(rbEndless);
        pnlConfig.add(rbEnding);
        pnlConfig.add(cbA);
        pnlConfig.add(cbB);
        pnlConfig.add(lblTitle);
        pnlConfig.add(lblRealTime);
        pnlConfig.add(lblA);
        pnlConfig.add(lblB);
        pnlConfig.add(lblDuration);
        pnlConfig.add(lblStats);
        pnlConfig.add(txtTrials);
        pnlConfig.add(btnStart);
        pnlConfig.add(btnBack);
    }

    void initSimulationPanel() {
        pnlSimulation = new JPanel();
        pnlSimulation.setLayout(null);
        pnlSimulation.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);

        lblTitle.setText("<html><h1>Simulation</h1></html>");
        lblA.setText("Player A");
        lblB.setText("Player B");
        lblTrials = new JLabel("0");
        pbWinRates = new JProgressBar();

        lblTitle.setBounds(100, 5, 300, 30);
        lblB.setBounds(450,180,100,30);
        lblA.setBounds(40,180,100,30);
        lblTrials.setBounds(200,200,100,30);
        pbWinRates.setBounds(125, 180, 300, 30);

        pnlSimulation.add(lblTitle);
        pnlSimulation.add(lblA);
        pnlSimulation.add(lblB);
        pnlSimulation.add(lblTrials);
        pnlSimulation.add(pbWinRates);
    }
}
