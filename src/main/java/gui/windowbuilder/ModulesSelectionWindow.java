package main.java.gui.windowbuilder;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import main.java.gui.MainWindow;
import main.java.utils.FileType;
import main.java.utils.Language;

import javax.swing.JComboBox;
import java.awt.Font;

public class ModulesSelectionWindow {

	private JFrame frame;
	private JPanel panelDefineStructure;

	public JPanel getPanelDefineStructure() {
		return panelDefineStructure;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ModulesSelectionWindow window = new ModulesSelectionWindow();
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
	public ModulesSelectionWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(240, 240, 240));
		frame.getContentPane().setLayout(null);
		frame.setBounds(200, 100, 450, 300);
		frame.setSize(600, 500);
		
		panelDefineStructure = new JPanel();
		panelDefineStructure.setBounds(0, 20, 584, 380);
		frame.getContentPane().add(panelDefineStructure);
		panelDefineStructure.setLayout(null);
		
		ButtonGroup pagesNumGroup = new ButtonGroup();
		
		JCheckBox chckbxSummary = new JCheckBox("Summary");
		chckbxSummary.setSelected(true);
		chckbxSummary.setBounds(93, 64, 203, 23);
		panelDefineStructure.add(chckbxSummary);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(93, 57, 397, 2);
		panelDefineStructure.add(separator_1);
		
		JCheckBox chckbxRankings = new JCheckBox("General");
		chckbxRankings.setSelected(true);
		chckbxRankings.setBounds(93, 165, 141, 23);
		panelDefineStructure.add(chckbxRankings);
		
		JCheckBox chckbxTechnical = new JCheckBox("Technical");
		chckbxTechnical.setSelected(true);
		chckbxTechnical.setBounds(93, 96, 203, 23);
		panelDefineStructure.add(chckbxTechnical);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(93, 360, 397, 2);
		panelDefineStructure.add(separator_3);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(313, 314, 177, 23);
		panelDefineStructure.add(comboBox);
		
		for (Language lang : Language.values()) {
                    comboBox.addItem(lang);
                }
		
		
		JLabel lblChoosePdfOutput = new JLabel("Choose pdf output language");
		lblChoosePdfOutput.setBounds(93, 314, 199, 23);
		panelDefineStructure.add(lblChoosePdfOutput);
		
		JCheckBox chckbxMonthly = new JCheckBox("Monthly");
		chckbxMonthly.setBounds(262, 165, 103, 23);
		panelDefineStructure.add(chckbxMonthly);
		
		JCheckBox chckbxWeekly = new JCheckBox("Weekly");
		chckbxWeekly.setBounds(418, 165, 128, 23);
		panelDefineStructure.add(chckbxWeekly);
		
		JCheckBox chckbxCreativeDatageneral = new JCheckBox("General");
		chckbxCreativeDatageneral.setSelected(true);
		chckbxCreativeDatageneral.setBounds(93, 248, 141, 23);
		panelDefineStructure.add(chckbxCreativeDatageneral);
		
		JCheckBox chckbxMonthly_1 = new JCheckBox("Monthly");
		chckbxMonthly_1.setBounds(262, 248, 97, 23);
		panelDefineStructure.add(chckbxMonthly_1);
		
		JCheckBox chckbxWeekly_1 = new JCheckBox("Weekly");
		chckbxWeekly_1.setBounds(418, 248, 97, 23);
		panelDefineStructure.add(chckbxWeekly_1);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(93, 291, 397, 2);
		panelDefineStructure.add(separator);
		
		JLabel lblRankings = new JLabel("Rankings");
		lblRankings.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblRankings.setBounds(103, 136, 110, 14);
		panelDefineStructure.add(lblRankings);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(93, 203, 397, 2);
		panelDefineStructure.add(separator_2);
		
		JLabel lblCreativeData = new JLabel("Creative data");
		lblCreativeData.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCreativeData.setBounds(103, 216, 97, 25);
		panelDefineStructure.add(lblCreativeData);
		
		JSeparator separator_4 = new JSeparator();
		separator_4.setBounds(93, 123, 397, 2);
		panelDefineStructure.add(separator_4);
		
		JCheckBox chckbxWholeTotal = new JCheckBox("Whole Total");
		chckbxWholeTotal.setBounds(308, 64, 182, 23);
		panelDefineStructure.add(chckbxWholeTotal);
		
		JCheckBox chckbxTimePeriodTotal = new JCheckBox("Time Period Total");
		chckbxTimePeriodTotal.setBounds(308, 96, 182, 23);
		panelDefineStructure.add(chckbxTimePeriodTotal);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
