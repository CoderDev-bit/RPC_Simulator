package app;

import javax.swing.*;
import java.awt.*;

public class GUI {

    JFrame frmMain;
    JCheckBox[][] arrCkStats;
    JPanel pnlMain, pnlConfig;
    JButton btnA, btnB, btnExit;
    JComboBox<String> cbA, cbB;
    JRadioButton rbEndless, rbEnding;
    JLabel lblTitle, lblRealTime, lblReport, lblA, lblB;
    JTextField txtTrials;
    RPS objRPS;

    void initGUI() {
        frmMain = new JFrame("GameTest™");
        frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmMain.setBounds(10, 10, 500, 350);

        initMainPanel();

        frmMain.add(pnlMain);
        frmMain.setVisible(true);
    }

    void initMainPanel() {
        pnlMain = new JPanel();
        pnlMain.setLayout(null);

        btnA = new JButton("Proceed");
        btnExit = new JButton("Exit");
        cbA = new JComboBox<>(new String[] { "RPS" });
        lblTitle = new JLabel("<html><h1>GameTest™</h1></html>");

        // Set bounds for components
        btnA.setBounds(100, 100, 100, 30);
        btnExit.setBounds(200, 100, 100, 30);
        cbA.setBounds(300, 100, 100, 30);
        lblTitle.setBounds(100, 5, 300, 30);

        // Add action listeners
        btnA.addActionListener(e -> proceed());
        btnExit.addActionListener(e -> System.exit(0));

        pnlMain.add(btnA);
        pnlMain.add(btnExit);
        pnlMain.add(cbA);
        pnlMain.add(lblTitle);
    }

    void proceed() {
        if (cbA.getSelectedItem().equals("RPS")) {
            objRPS = new RPS();
            initConfigPanel();
            frmMain.remove(pnlMain);
            frmMain.add(pnlConfig);
            frmMain.revalidate();
            frmMain.repaint();
        }
    }

    void initConfigPanel() {
        pnlConfig = new JPanel();
        pnlConfig.setLayout(null);
        pnlConfig.setBounds(0, 0, 500, 350);

        lblTitle.setText("<html><h1>Configuration</h1></html>");
        lblRealTime = new JLabel("Real Time");
        lblReport = new JLabel("Report");
        lblA = new JLabel("Player A Strategy");
        lblB = new JLabel("Player B Strategy");
        rbEndless = new JRadioButton("Endless");
        rbEnding = new JRadioButton("Ending");
        cbA = new JComboBox(new String[]{"Random", "Human", "Against Human", "Adaptive"});
        cbB = new JComboBox(new String[]{"Random", "Human", "Against Human", "Adaptive"});
        txtTrials = new JTextField(10);
        btnA = new JButton("Start Test");
        btnB = new JButton("Go Back");


        arrCkStats = new JCheckBox[2][];
        arrCkStats[0] = new JCheckBox[2];
        arrCkStats[1] = new JCheckBox[4];
        String[] arrStatNames = {"Immediate Winner", "Player Choices", "Overall Winner", "Choice Win Rate", "Choice Pick Rate", "Streaks"};

        int i = 0;
        for (int row = 0; row < arrCkStats.length; row++) {
            for (int col = 0; col < arrCkStats[row].length; col++) {
                arrCkStats[row][col] = new JCheckBox(arrStatNames[i]);
                arrCkStats[row][col].setBounds(100, 150 + (row * 30) + (col * 30), 200, 30);
                pnlConfig.add(arrCkStats[row][col]);
                i++;
            }
        }

        lblTitle.setBounds(100, 5, 300, 30);
        lblRealTime.setBounds(300, 300, 100, 30);
        lblReport.setBounds(300, 350, 100, 30);
        lblA.setBounds(300, 70, 100, 30);
        lblB.setBounds(300, 150, 100, 30);
        rbEndless.setBounds(100, 100, 100, 30);
        rbEnding.setBounds(200, 100, 100, 30);
        cbA.setBounds(300, 100, 100, 30);
        cbB.setBounds(300, 180, 100, 30);
        txtTrials.setBounds(300, 260, 100, 30);




        rbEndless.addActionListener(e -> {

            rbEnding.setSelected(false);
            txtTrials.setVisible(false);

        });

        rbEnding.addActionListener(e -> {

            rbEndless.setSelected(false);
            txtTrials.setVisible(true);

        });

        rbEndless.setSelected(true);
        txtTrials.setVisible(false);
        txtTrials.setText("# of trials");

        pnlConfig.add(rbEndless);
        pnlConfig.add(rbEnding);
        pnlConfig.add(cbA);
        pnlConfig.add(cbB);
        pnlConfig.add(lblTitle);
        pnlConfig.add(lblRealTime);
        pnlConfig.add(lblReport);
        pnlConfig.add(lblA);
        pnlConfig.add(lblB);
        pnlConfig.add(txtTrials);
        pnlConfig.add(btnA);
        pnlConfig.add(btnB);

    }
}
