package main.java.gui.windowbuilder;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import java.awt.Font;
import java.awt.Color;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.SwingConstants;

public class MainMenuWindow {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					MainMenuWindow window = new MainMenuWindow();
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
	public MainMenuWindow() {
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
        panel.setBackground(UIManager.getColor("TextArea.inactiveBackground"));
        panel.setBounds(0, 0, 584, 474);
        frame.getContentPane().add(panel);
        panel.setLayout(null);
        
        JButton btnFromAnExcel = new JButton("From an excel file on your computer");
        btnFromAnExcel.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent arg0) {
        	}
        });
        btnFromAnExcel.setBounds(44, 241, 224, 51);
        panel.add(btnFromAnExcel);
        
        JButton btnByDownloadingDatas = new JButton("By downloading datas");
        btnByDownloadingDatas.addPropertyChangeListener("font", new PropertyChangeListener() {
            
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                System.out.println("pute");
                
            }
        });
        
//        btnByDownloadingDatas.getDo
        btnByDownloadingDatas.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        	    btnByDownloadingDatas.setFont(new Font("Tahoma",Font.BOLD,13));
        	}
        });
        btnByDownloadingDatas.setBounds(311, 241, 224, 51);
        panel.add(btnByDownloadingDatas);
        
        JLabel lblChooseMode = new JLabel("Select how to import your files");
        lblChooseMode.setForeground(Color.BLACK);
        lblChooseMode.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblChooseMode.setBounds(44, 130, 308, 67);
        panel.add(lblChooseMode);
        
        
        JLabel lblPdfGenerator = new JLabel("Pdf Generator");
        lblPdfGenerator.setHorizontalAlignment(SwingConstants.CENTER);
        lblPdfGenerator.setFont(new Font("Narkisim", Font.PLAIN, 45));
        lblPdfGenerator.setBounds(44, 34, 504, 98);
        panel.add(lblPdfGenerator);
        
        JLabel lblBackground = new JLabel("");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
       
        lblBackground.setIcon(new ImageIcon(getClass().getResource("/background2.png")));
        lblBackground.setBounds(0, 0, 584, 463);
        panel.add(lblBackground);
        
       
	}
}
