package main.java.gui.windowbuilder;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class NavigationWindow {

	private JFrame frame;
	private JPanel panelNavigation;
	private JButton btnNext;
	private JButton btnPrevious;
	private JScrollPane scrollPane;
	private JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NavigationWindow window = new NavigationWindow();
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
	public NavigationWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setSize(800, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		panelNavigation = new JPanel();
		panelNavigation.setLayout(null);
		panelNavigation.setBounds(41, 317, 716, 109);
		frame.getContentPane().add(panelNavigation);
		
		btnNext = new JButton("Next");
		btnNext.setBounds(606, 43, 100, 23);
		panelNavigation.add(btnNext);
		
		btnPrevious = new JButton("Previous");
		btnPrevious.setBounds(32, 43, 100, 23);
		panelNavigation.add(btnPrevious);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(41, 59, 285, 128);
		frame.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		scrollPane.setViewportView(textArea);
	}

	public JPanel getPanelNavigation() {
		return panelNavigation;
	}

	public JButton getBtnNext() {
		return btnNext;
	}

	public JButton getBtnPrevious() {
		return btnPrevious;
	}
}
