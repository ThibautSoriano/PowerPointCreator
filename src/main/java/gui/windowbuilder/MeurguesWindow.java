package main.java.gui.windowbuilder;

import java.awt.EventQueue;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MeurguesWindow {

    private JFrame frame;
    private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MeurguesWindow window = new MeurguesWindow();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public MeurguesWindow() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        
        JPanel panelChooseColumns = new JPanel();
        panelChooseColumns.setBounds(0, 20, 584, 380);
        frame.getContentPane().add(panelChooseColumns);
        panelChooseColumns.setLayout(null);
        tabbedPane.setBounds(0, 66, 584, 314);
        panelChooseColumns.add(tabbedPane);
        
        JPanel panelRankings = new JPanel();
        tabbedPane.addTab("Rankings        ", null, panelRankings, null);
        panelRankings.setLayout(null);
        
        JCheckBox chckbxImpressionsRankings = new JCheckBox("Impressions");
        chckbxImpressionsRankings.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
            }
        });
        chckbxImpressionsRankings.setBounds(93, 57, 97, 23);
        panelRankings.add(chckbxImpressionsRankings);
        
        JCheckBox chckbxClicksRankings = new JCheckBox("Clicks");
        chckbxClicksRankings.setBounds(243, 57, 97, 23);
        panelRankings.add(chckbxClicksRankings);
        
        JCheckBox chckbxUniqueCTRRankings = new JCheckBox("Unique CTR");
        chckbxUniqueCTRRankings.setBounds(393, 57, 97, 23);
        panelRankings.add(chckbxUniqueCTRRankings);
        
        JCheckBox chckbxClickingUsersRankings = new JCheckBox("Clicking users");
        chckbxClickingUsersRankings.setBounds(243, 131, 97, 23);
        panelRankings.add(chckbxClickingUsersRankings);
        
        JCheckBox chckbxClickThroughRateRankings = new JCheckBox("Click through rate");
        chckbxClickThroughRateRankings.setBounds(243, 199, 120, 23);
        panelRankings.add(chckbxClickThroughRateRankings);
        
        JCheckBox chckbxFrequencyRankings = new JCheckBox("Frequency");
        chckbxFrequencyRankings.setBounds(93, 199, 97, 23);
        panelRankings.add(chckbxFrequencyRankings);
        
        JSeparator separator = new JSeparator();
        separator.setBounds(93, 33, 397, 2);
        panelRankings.add(separator);
        
        JCheckBox chckbxUniqueCookiesRankings = new JCheckBox("Unique cookies");
        chckbxUniqueCookiesRankings.setBounds(93, 131, 97, 23);
        panelRankings.add(chckbxUniqueCookiesRankings);
        
        JSeparator separator_1 = new JSeparator();
        separator_1.setBounds(93, 252, 397, 2);
        panelRankings.add(separator_1);
        
        JPanel panelTechnical = new JPanel();
        tabbedPane.addTab("Technical", null, panelTechnical, null);
        panelTechnical.setLayout(null);
        
        JCheckBox chckbxImpressionsTechnical = new JCheckBox("Impressions");
        chckbxImpressionsTechnical.setBounds(93, 57, 97, 23);
        panelTechnical.add(chckbxImpressionsTechnical);
        
        JCheckBox chckbxClicksTechnical = new JCheckBox("Clicks");
        chckbxClicksTechnical.setBounds(243, 57, 97, 23);
        panelTechnical.add(chckbxClicksTechnical);
        
        JCheckBox chckbxUniqueCTRTechnical = new JCheckBox("Unique CTR");
        chckbxUniqueCTRTechnical.setBounds(393, 57, 97, 23);
        panelTechnical.add(chckbxUniqueCTRTechnical);
        
        JCheckBox chckbxClickingUsersTechnical = new JCheckBox("Clicking users");
        chckbxClickingUsersTechnical.setBounds(243, 131, 97, 23);
        panelTechnical.add(chckbxClickingUsersTechnical);
        
        JCheckBox chckbxClickThroughRateTechnical = new JCheckBox("Click through rate");
        chckbxClickThroughRateTechnical.setBounds(243, 199, 120, 23);
        panelTechnical.add(chckbxClickThroughRateTechnical);
        
        JCheckBox chckbxFrequencyTechnical = new JCheckBox("Frequency");
        chckbxFrequencyTechnical.setBounds(93, 199, 97, 23);
        panelTechnical.add(chckbxFrequencyTechnical);
        
        JSeparator separator_2 = new JSeparator();
        separator_2.setBounds(93, 33, 397, 2);
        panelTechnical.add(separator_2);
        
        JCheckBox chckbxUniqueCookiesTechnical = new JCheckBox("Unique cookies");
        chckbxUniqueCookiesTechnical.setBounds(93, 131, 97, 23);
        panelTechnical.add(chckbxUniqueCookiesTechnical);
        
        JSeparator separator_3 = new JSeparator();
        separator_3.setBounds(93, 252, 397, 2);
        panelTechnical.add(separator_3);
    }
}
