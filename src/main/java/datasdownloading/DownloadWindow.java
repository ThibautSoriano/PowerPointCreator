package main.java.datasdownloading;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class DownloadWindow {

	private JFrame frame;
	private JTable table;
	private JTextField txtFilter;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
	    try {
	            // set for file chooser look
	            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	            //
	        } catch (Exception e1) {
	            e1.printStackTrace();
	        }
	    
	    new DownloadWindow();
	}

	/**
	 * Create the application.
	 */
	public DownloadWindow() {
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
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 20, 584, 380);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		
		
		

//
              Vector<String> columnNames = new Vector<String>();
		    columnNames.addElement("Column One");
		    columnNames.addElement("Column Two");
		    columnNames.addElement("Column Three");
//
		    Vector<String> rowOne = new Vector<String>();
		    rowOne.addElement("Row1-Column1");
		    rowOne.addElement("Row1-Column2");
		    rowOne.addElement("Row1-Column3");
		    
		    Vector<String> rowTwo = new Vector<String>();
		    rowTwo.addElement("Row2-Column1");
		    rowTwo.addElement("Row2-Column2");
		    rowTwo.addElement("Row2-Column3");
		    
		    Vector<Vector> rowData = new Vector<Vector>();
		    rowData.addElement(rowOne);
		    rowData.addElement(rowTwo);

                
		    for (int i = 0;i<100;i++) {
		        Vector<String> row = new Vector<String>();
	                    row.addElement("Row2-Column1");
	                    row.addElement("Row2-Column2");
	                    row.addElement("Row2-Column3");
	                    rowData.addElement(row);
		    }
		    
                
                table = new JTable(rowData,columnNames);

//                table.setBounds(77, 86, 429, 256);
//                panel.add(table);
                
                JScrollPane scrollPane = new JScrollPane( table );
                scrollPane.setBounds(46, 99, 495, 211);
                scrollPane.add(table);
                scrollPane.setViewportView(table);
                panel.add(scrollPane);
                
                JLabel lblSelectACampaign = new JLabel("Select a campaign");
                lblSelectACampaign.setFont(new Font("Tahoma", Font.BOLD, 14));
                lblSelectACampaign.setBounds(54, 60, 221, 22);
                panel.add(lblSelectACampaign);
                
                JCheckBox chckbxRankings = new JCheckBox("Rankings");
                chckbxRankings.setSelected(true);
                chckbxRankings.setBounds(178, 334, 97, 23);
                panel.add(chckbxRankings);
                
                JCheckBox chckbxTechnical = new JCheckBox("Technical");
                chckbxTechnical.setSelected(true);
                chckbxTechnical.setBounds(368, 334, 97, 23);
                panel.add(chckbxTechnical);
                
                txtFilter = new JTextField();
                txtFilter.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent arg0) {
                    }
                });
                
                txtFilter.setBounds(310, 61, 231, 22);
                panel.add(txtFilter);
                txtFilter.setColumns(10);
                panel.setVisible(true);
                frame.setVisible(true);
	}
}
