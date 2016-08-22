package main.java.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import main.java.utils.Language;

@SuppressWarnings("serial")
public class ModulesSettingsPanel extends SettingsChoicePanel {
    private JCheckBox chckbxSummary;

    private JCheckBox chckbxRankings;

    private JCheckBox chckbxTechnical;

    private JComboBox<Language> comboBox;

    private JCheckBox chckbxMonthlyRankings;

    private JCheckBox chckbxWeeklyRankings;

    private JCheckBox chckbxMonthlyCreative;

    private JCheckBox chckbxWeeklyCreative;

    private JCheckBox chckbxCreative;

    private JCheckBox chckbxWholeTotal;

    private JCheckBox chckbxTimePeriodTotal;

    public JCheckBox getChckbxMonthlyRankings() {
        return chckbxMonthlyRankings;
    }

    public JCheckBox getChckbxWeeklyRankings() {
        return chckbxWeeklyRankings;
    }

    public JCheckBox getChckbxMonthlyCreative() {
        return chckbxMonthlyCreative;
    }

    public JCheckBox getChckbxWeeklyCreative() {
        return chckbxWeeklyCreative;
    }

    public JCheckBox getChckbxCreative() {
        return chckbxCreative;
    }

    public JCheckBox getChckbxSummary() {
        return chckbxSummary;
    }

    public JCheckBox getChckbxRankings() {
        return chckbxRankings;
    }

    public JCheckBox getChckbxTechnical() {
        return chckbxTechnical;
    }

    public ModulesSettingsPanel() {
        super("Choose modules");

  
        chckbxSummary = new JCheckBox("Summary");
        chckbxSummary.setSelected(true);
        chckbxSummary.setBounds(93, 64, 203, 23);
        chckbxSummary.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent e) {
                if (!checkOneBoxFilled())
                    chckbxSummary.setSelected(true);

            }
        });

        add(chckbxSummary);

        JSeparator separator_1 = new JSeparator();
        separator_1.setBounds(93, 57, 397, 2);
        add(separator_1);

        chckbxRankings = new JCheckBox("General");
        chckbxRankings.setSelected(true);
        chckbxRankings.setBounds(93, 165, 141, 23);
        chckbxRankings.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent e) {

                if (!checkOneBoxFilled())
                    chckbxRankings.setSelected(true);

            }
        });

        add(chckbxRankings);

        chckbxTechnical = new JCheckBox("Technical");
        chckbxTechnical.setSelected(true);
        chckbxTechnical.setBounds(93, 96, 203, 23);
        chckbxTechnical.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent e) {
                if (!checkOneBoxFilled())
                    chckbxTechnical.setSelected(true);

            }
        });
        add(chckbxTechnical);

        JSeparator separator_3 = new JSeparator();
        separator_3.setBounds(93, 360, 397, 2);
        add(separator_3);

        comboBox = new JComboBox<Language>();
        comboBox.setBounds(313, 314, 177, 23);
        add(comboBox);

        for (Language lang : Language.values()) {
            comboBox.addItem(lang);
        }

        JLabel lblChoosePdfOutput = new JLabel("Choose pdf output language");
        lblChoosePdfOutput.setBounds(93, 314, 199, 23);
        add(lblChoosePdfOutput);

        chckbxMonthlyRankings = new JCheckBox("Monthly");
        chckbxMonthlyRankings.setBounds(262, 165, 103, 23);
        chckbxMonthlyRankings.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!checkOneBoxFilled())
                    chckbxMonthlyRankings.setSelected(true);

            }
        });
        add(chckbxMonthlyRankings);

        chckbxWeeklyRankings = new JCheckBox("Weekly");
        chckbxWeeklyRankings.setBounds(418, 165, 128, 23);
        chckbxWeeklyRankings.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!checkOneBoxFilled())
                    chckbxWeeklyRankings.setSelected(true);

            }
        });
        add(chckbxWeeklyRankings);

        chckbxCreative = new JCheckBox("General");
        chckbxCreative.setSelected(true);
        chckbxCreative.setBounds(93, 248, 141, 23);
        chckbxCreative.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
               
                if (!checkOneBoxFilled())
                    chckbxCreative.setSelected(true);

            }
        });
        add(chckbxCreative);

        chckbxMonthlyCreative = new JCheckBox("Monthly");
        chckbxMonthlyCreative.setBounds(262, 248, 97, 23);
        chckbxMonthlyCreative.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
               
                if (!checkOneBoxFilled())
                    chckbxMonthlyCreative.setSelected(true);

            }
        });
        add(chckbxMonthlyCreative);

        chckbxWeeklyCreative = new JCheckBox("Weekly");
        chckbxWeeklyCreative.setBounds(418, 248, 97, 23);
        chckbxWeeklyCreative.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!checkOneBoxFilled())
                    chckbxWeeklyCreative.setSelected(true);
                
            }
        });

        add(chckbxWeeklyCreative);

        JSeparator separator = new JSeparator();
        separator.setBounds(93, 291, 397, 2);
        add(separator);

        JLabel lblRankings = new JLabel("Rankings");
        lblRankings.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblRankings.setBounds(103, 136, 110, 14);
        add(lblRankings);

        JSeparator separator_2 = new JSeparator();
        separator_2.setBounds(93, 203, 397, 2);
        add(separator_2);

        JLabel lblCreativeData = new JLabel("Creative data");
        lblCreativeData.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblCreativeData.setBounds(103, 216, 97, 25);
        add(lblCreativeData);

        JSeparator separator_4 = new JSeparator();
        separator_4.setBounds(93, 123, 397, 2);
        add(separator_4);
        
        
         chckbxWholeTotal = new JCheckBox("Whole Total");
        chckbxWholeTotal.setBounds(308, 64, 182, 23);
        add(chckbxWholeTotal);
        
         chckbxTimePeriodTotal = new JCheckBox("Time Period Total");
        chckbxTimePeriodTotal.setBounds(308, 96, 182, 23);
        add(chckbxTimePeriodTotal);

    }

    public JCheckBox getChckbxWholeTotal() {
        return chckbxWholeTotal;
    }

    public JCheckBox getChckbxTimePeriodTotal() {
        return chckbxTimePeriodTotal;
    }

    @Override
    public boolean isEveryThingOk() {
        MainWindow.setRankings(detectRankings());
        MainWindow.setTechnical(chckbxTechnical.isSelected());
        MainWindow.setCreative(detectCreative());
        MainWindow.setTimePeriodTotal(chckbxTimePeriodTotal.isSelected());
        MainWindow.setSummary(chckbxSummary.isSelected());
        return true;
    }

    @Override
    public SettingsChoicePanel getNewInstance() {
        return new ModulesSettingsPanel();
    }

    private boolean checkOneBoxFilled() {
        return detectCreative() || detectRankings()
                || chckbxTechnical.isSelected() || chckbxSummary.isSelected();

    }

    public Language getSelectedLanguage() {
        return (Language) comboBox.getSelectedItem();
    }

    public static void main(String[] args) {
        JFrame a = new JFrame();
        a.setBounds(200, 100, MainWindow.WINDOW_WIDTH,
                MainWindow.WINDOW_HEIGHT);

        a.setLayout(null);
        a.getContentPane().setLayout(null);

        a.getContentPane().add(new ModulesSettingsPanel());
        a.setVisible(true);
    }

    public boolean detectRankings() {
        return chckbxRankings.isSelected() || chckbxMonthlyRankings.isSelected()
                || chckbxWeeklyRankings.isSelected();
    }

    public boolean detectCreative() {
        return chckbxCreative.isSelected() || chckbxMonthlyCreative.isSelected()
                || chckbxWeeklyCreative.isSelected();
    }
}