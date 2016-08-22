package view;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import convertor.ExcelToPowerPointConverter;

/**
 * Class displaying the converter - Allows to convert a selected Excel file into
 * a selected destination Powerpoint file
 * 
 * @author Chloé & Pierre
 *
 */

public class ConverterWindow extends JFrame {
	private static final long serialVersionUID = -5132727210237028704L;

	private JTextField inputFileName; // The path of the excel file
	private JTextField outputFileName; // The path of the generated ppt file
	private JPanel mainPanel;
	private JProgressBar progressBar;
	// File choosers to select input and output:
	private JFileChooser chooserPpt;
	private JFileChooser chooserExcel;
	private String lastExcelPath; // Last folder opened by chooserExcel
	private String lastPptPath; // Last folder opened by chooserPpt

	public ConverterWindow() {

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		lastExcelPath = System.getProperty("user.home");
		lastPptPath = System.getProperty("user.home");

		this.setTitle("Excel to PowerPoint Converter");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(400, 200);
		this.setResizable(false);

		initialize();

		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	/**
	 * Creation of the main elements of the page
	 */
	private void initialize() {

		mainPanel = new JPanel(new GridLayout(4, 1));

		chooserExcel = new JFileChooser();
		chooserPpt = new JFileChooser();

		FileNameExtensionFilter filter = new FileNameExtensionFilter("PowerPoint 97-2003 (.ppt)", "ppt");
		chooserPpt.setFileFilter(filter);
		filter = new FileNameExtensionFilter("PowerPoint (.pptx)", "pptx");
		chooserPpt.setFileFilter(filter);

		JLabel labExcel = new JLabel("Excel File:");
		inputFileName = new JTextField();
		inputFileName.setPreferredSize(new Dimension(150, 20));
		JPanel excelPanel = new JPanel();
		JButton inputFileChooserButton = new JButton("Browse");
		inputFileChooserButton.addActionListener(new ExcelFileChooserListener());
		excelPanel.add(labExcel, BorderLayout.WEST);
		excelPanel.add(inputFileName, BorderLayout.CENTER);
		excelPanel.add(inputFileChooserButton, BorderLayout.EAST);

		JLabel labPpt = new JLabel("Output file:");
		outputFileName = new JTextField();
		outputFileName.setPreferredSize(new Dimension(150, 20));
		JPanel pptPanel = new JPanel();
		JButton outputFileChooserButton = new JButton("Browse");
		outputFileChooserButton.addActionListener(new PptChooserListener());
		pptPanel.add(labPpt, BorderLayout.WEST);
		pptPanel.add(outputFileName, BorderLayout.CENTER);
		pptPanel.add(outputFileChooserButton, BorderLayout.EAST);

		JButton convertButton = new JButton("Convert"); // Start the conversion
														// when clicked
		convertButton.addActionListener(new ConvertButtonListener());
		JPanel convertPanel = new JPanel();
		convertPanel.add(convertButton, BorderLayout.CENTER);

		progressBar = new JProgressBar();
		progressBar.setVisible(false);

		mainPanel.add(excelPanel);
		mainPanel.add(pptPanel);
		mainPanel.add(convertPanel);
		mainPanel.add(progressBar);
		this.setContentPane(mainPanel);
	}

	class ConvertButtonListener implements ActionListener, PropertyChangeListener {
		// When convert button is clicked, the conversion begins (after some
		// checks)
		@Override
		public void actionPerformed(ActionEvent e) {

			File inputFile = new File(inputFileName.getText());
			int i = outputFileName.getText().lastIndexOf("\\"); // index of the
																// end of the
																// output path
			// if there is no input/output
			if (inputFileName.getText().equals("") || outputFileName.getText().equals("")) {
				JOptionPane.showMessageDialog(null,
						"Please enter an excel file and a folder to save your new powerpoint", "Empty field",
						JOptionPane.WARNING_MESSAGE);
				return;
				// if the input doesn't exist
			} else if (!inputFile.exists()) {
				JOptionPane.showMessageDialog(null,
						"The excel file " + new File(inputFile.getAbsolutePath()).getName() + " doesn't exist",
						"Invalid file", JOptionPane.WARNING_MESSAGE);
				return;
				// if the output folder doesn't exist
			} else if (i != -1 && !new File(outputFileName.getText().substring(0, i)).isDirectory()) {
				JOptionPane.showMessageDialog(null,
						"The folder " + outputFileName.getText().substring(0, i) + " doesn't exist", "Invalid folder",
						JOptionPane.WARNING_MESSAGE);
				return;
				// if the output is not in a folder (not a full path entered)
			} else if (i == -1) {
				JOptionPane.showMessageDialog(null,
						"Please enter a correct destination for the generated PowerPoint file.", "Invalid file",
						JOptionPane.WARNING_MESSAGE);
				return;
				// if the output file name is not valid (contains special
				// characters)
			} else if (new File(outputFileName.getText()).getName().matches(".*[\\/*\"<>:|?].*")) {
				JOptionPane.showMessageDialog(null,
						"Please enter a valid file name for the generated PowerPoint file."
								+ System.getProperty("line.separator") + "Forbidden characters : / \\ * : ? < > \" |",
						"Invalid file name", JOptionPane.WARNING_MESSAGE);
				return;
				// if the input and the output are valid, we can start the
				// conversion
			} else {
				File f = new File(outputFileName.getText());
				if (f.exists()) { // if the output file already exists, we ask
									// before overwriting it
					int option = JOptionPane.showConfirmDialog(null,
							"The file " + f.getName() + " already exists, do you want to overwrite it?",
							"File already exists", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (option == JOptionPane.NO_OPTION) {
						return;
					}
				}
				ExcelToPowerPointConverter convertor = new ExcelToPowerPointConverter("template\\gMR_template.pptx",
						inputFileName.getText(), checkOutput(outputFileName.getText()));

				progressBar.setVisible(true);
				progressBar.setValue(0);
				progressBar.setStringPainted(true);
				progressBar.setString(null);
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				convertor.addPropertyChangeListener(this);
				convertor.execute();
			}
		}

		/**
		 * Manages the progressBar message
		 */
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if ("progress" == evt.getPropertyName()) {
				int progress = (Integer) evt.getNewValue();
				progressBar.setValue(progress);
				if (progress == 100) {
					setCursor(null);
					progressBar.setString("Done !");
				}
			}
		}
	}

	/**
	 * Class managing the events of the Excel file chooser
	 */
	class ExcelFileChooserListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			chooserExcel.setCurrentDirectory(new File(lastExcelPath));
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel (.xls, .xlsx)", "xls", "xlsx");
			chooserExcel.setFileFilter(filter);
			int retour = chooserExcel.showOpenDialog(mainPanel);
			if (retour == JFileChooser.APPROVE_OPTION) { // if the file chosen
															// is valid
				inputFileName.setText(chooserExcel.getSelectedFile().getAbsolutePath());
				lastExcelPath = chooserExcel.getSelectedFile().getParentFile().getAbsolutePath(); // updating
																									// the
																									// last
																									// folder
																									// opened
																									// in
																									// chooserExcel
			}
		}
	}

	/**
	 * Class managing the events of the Powerpoint file chooser
	 */
	class PptChooserListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			chooserPpt.setCurrentDirectory(new File(lastPptPath));
			chooserPpt.setAcceptAllFileFilterUsed(false);
			int retour = chooserPpt.showSaveDialog(mainPanel);
			if (retour == JFileChooser.APPROVE_OPTION) {
				outputFileName.setText(checkOutput(chooserPpt.getSelectedFile().getAbsolutePath()));
				lastPptPath = chooserPpt.getSelectedFile().getParentFile().getAbsolutePath();
			}
		}
	}

	/**
	 * 
	 * @param f
	 *            file
	 * @return the extension of the file f
	 */
	public String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	/**
	 * 
	 * @param output:
	 *            file path
	 * @return return a valid output form (with a good extension: ppt/pptx)
	 */
	public String checkOutput(String output) {
		File f = new File(output);
		String res = output;
		if (f != null) {
			if (getExtension(f) == null) { // if there is not extension, we add
											// .pptx
				res = output + ".pptx";
				if (!chooserPpt.getFileFilter().accept(new File(res))) // if
																		// .pptx
																		// was
																		// not
																		// the
																		// extension
																		// selected
																		// in
																		// the
																		// file
																		// chooser,
																		// we
																		// chand
																		// it
					res = output + ".ppt";
			} else if (!getExtension(f).equals("ppt") && !getExtension(f).equals("pptx")) { // if
																							// the
																							// extension
																							// is
																							// not
																							// valid
																							// (not
																							// .ppt
																							// or
																							// .pptx)
				res = output.substring(0, output.length() - getExtension(f).length()) + "pptx"; // we
																								// change
																								// it
																								// to
																								// pptx
				if (!chooserPpt.getFileFilter().accept(new File(res))) // if
																		// pptx
																		// was
																		// not
																		// the
																		// extension
																		// selected
					res = res.substring(0, res.length() - getExtension(f).length()) + "ppt"; // we
																								// change
																								// it
																								// to
																								// ppt
			} else // if the extension was already good
				res = output;
		}
		return res;
	}
}