package main.java.gui.windowbuilder;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;

public class LoginWindow {

	private JFrame frame;
	private JTextField txtLogin;
	private JPasswordField txtPassword;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginWindow window = new LoginWindow();
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
	public LoginWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
        frame.setBounds(300, 100, 600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBounds(0, 20, 584, 380);
        frame.getContentPane().add(panel);
        panel.setLayout(null);
        
        JSeparator separator = new JSeparator();
        separator.setBounds(93, 57, 397, 2);
        panel.add(separator);
        
        JSeparator separator_3 = new JSeparator();
        separator_3.setBounds(93, 297, 397, 2);
        panel.add(separator_3);
        
        JLabel lblLogin = new JLabel("Login name");
        lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogin.setBounds(211, 106, 173, 19);
        panel.add(lblLogin);
        
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
        lblPassword.setBounds(211, 167, 173, 19);
        panel.add(lblPassword);
        
        txtLogin = new JTextField();
        txtLogin.setBounds(211, 136, 173, 20);
        panel.add(txtLogin);
        txtLogin.setColumns(10);
        
        txtPassword = new JPasswordField();
        txtPassword.setColumns(10);
        txtPassword.setBounds(211, 197, 173, 20);
        panel.add(txtPassword);
        
        JCheckBox chckbxRememberTheLogin = new JCheckBox("Remember the login");
        chckbxRememberTheLogin.setBounds(211, 247, 173, 23);
        panel.add(chckbxRememberTheLogin);
	}
}
