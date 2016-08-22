package main.java.gui.windowbuilder;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.java.utils.FileType;
import main.java.utils.Internationalization;
import main.java.utils.Utils;
import javax.swing.JComboBox;

public class MainWindowZhengqin {

        public final static int  WINDOW_HEIGHT = 500;
        public final static int WINDOW_WIDTH = 800;
    
    
	private JFrame frmConverter;
	private JTextField txtExcel;
	private JTextField txtLogo;
	private JPanel panelMainWindow;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// set for file chooser look
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
				}
				try {
					MainWindowZhengqin window = new MainWindowZhengqin();
					window.frmConverter.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindowZhengqin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmConverter = new JFrame();
		frmConverter.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\user\\Documents\\Polytech\\SI4\\Hongrie\\ExcelToPdf\\src\\main\\resources\\icon.png"));
		frmConverter.setTitle(Internationalization.getKey("Converter"));
		frmConverter.setBounds(200, 100, 600, 500);
		
		frmConverter.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmConverter.getContentPane().setLayout(null);
		
		panelMainWindow = new JPanel();
		panelMainWindow.setBounds(0, 0, 600, 441);
		frmConverter.getContentPane().add(panelMainWindow);
		panelMainWindow.setLayout(null);
		
		JLabel lblChooseExcel = new JLabel(Internationalization.getKey("Choose an excel file"));
		lblChooseExcel.setHorizontalAlignment(SwingConstants.CENTER);
		lblChooseExcel.setBounds(27, 64, 389, 22);
		panelMainWindow.add(lblChooseExcel);
		lblChooseExcel.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		
		txtExcel = new JTextField();
		txtExcel.getDocument().addDocumentListener(new DocumentListener() {
                    
                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        System.out.println("fils de yu");
                        
                    }
                    
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        System.out.println("fils de merg");
                        
                    }
                    
                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        System.out.println("fils de clop");
                        
                    }
                });
		txtExcel.addKeyListener(new KeyAdapter() {
		    @Override
		    public void keyTyped(KeyEvent e) {
		        System.out.println("junior");
		    }
		});
		txtExcel.setBounds(27, 113, 389, 20);
		panelMainWindow.add(txtExcel);
		txtExcel.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		txtExcel.setColumns(10);
		
		JLabel lblLogo = new JLabel(Internationalization.getKey("Specify your logo (optional)"));
		lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogo.setBounds(27, 163, 389, 22);
		panelMainWindow.add(lblLogo);
		lblLogo.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		
		txtLogo = new JTextField();
		txtLogo.setBounds(27, 211, 389, 20);
		panelMainWindow.add(txtLogo);
		txtLogo.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		txtLogo.setColumns(10);
		
		JButton btnLogo = new JButton(Internationalization.getKey("Browse"));
		btnLogo.setBounds(441, 110, 119, 23);
		panelMainWindow.add(btnLogo);
		btnLogo.setFont(new Font("Times New Roman", Font.PLAIN, 18));

		JButton btnExcel = new JButton(Internationalization.getKey("Browse"));
		btnExcel.setBounds(441, 208, 119, 23);
		panelMainWindow.add(btnExcel);		
		btnExcel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				openFileChooser(FileType.EXCEL,txtExcel);
			}
		});
		btnExcel.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		
		JLabel lblChooseOutput = new JLabel("Choose pdf output language");
		lblChooseOutput.setBounds(27, 291, 199, 23);
		panelMainWindow.add(lblChooseOutput);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(247, 291, 177, 23);
		panelMainWindow.add(comboBox);
		
		JMenuBar menuBar = new JMenuBar();
		frmConverter.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);
		
		JMenu mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);
		
		JMenu mnLanguage = new JMenu("Language");
		mnSettings.add(mnLanguage);
		
		JRadioButtonMenuItem rdbtnmntmEnglish = new JRadioButtonMenuItem("English");
		rdbtnmntmEnglish.setSelected(true);
		mnLanguage.add(rdbtnmntmEnglish);
		btnLogo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				openFileChooser(FileType.LOGO,txtLogo);
			}
		});
	}
	
	
	
	
	
	public  static  void openFileChooser(final FileType fileType,final JTextField field) {
        final JFrame frame = new JFrame(Internationalization.getKey("JFileChooser Popup"));
        Container contentPane = frame.getContentPane();
        
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setControlButtonsAreShown(true);
        contentPane.add(fileChooser, BorderLayout.CENTER);
        
        
        fileChooser.setAcceptAllFileFilterUsed(false);
        
        
        FileFilter filter = new FileNameExtensionFilter(fileType.getDescription(), fileType.getAcceptedExtensions());
        fileChooser.setFileFilter(filter);
        
        
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
              JFileChooser theFileChooser = (JFileChooser) actionEvent.getSource();
              String command = actionEvent.getActionCommand();
              if (command.equals(JFileChooser.APPROVE_SELECTION)) {
                  
                File selectedFile = theFileChooser.getSelectedFile();
                	
                field.setText(selectedFile.getAbsolutePath());
               
                	
                
                
                frame.dispose();
              }
              else if (command.equals(JFileChooser.CANCEL_SELECTION)) {
            	  frame.dispose();
              }
            }
          };
          
          fileChooser.addActionListener(actionListener);

          frame.pack();
          frame.setVisible(true);
    }
}
