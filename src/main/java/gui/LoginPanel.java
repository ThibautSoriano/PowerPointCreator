package main.java.gui;

import java.awt.Cursor;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import main.java.datasdownloading.HttpDownload;
import main.java.datasdownloading.HttpMessage;
import main.java.utils.SaveSettings;
import main.java.utils.Utils;

@SuppressWarnings("serial")
public class LoginPanel extends SettingsChoicePanel {

    private boolean ok;

    private JTextField txtLogin;
    private JPasswordField txtPassword;
    private JCheckBox chckbxRememberTheLogin;

    private MainWindow mainFrame;

    public LoginPanel(MainWindow mainFrame) {
        super("Login");
        this.mainFrame = mainFrame;

        JSeparator separator = new JSeparator();
        separator.setBounds(93, 57, 397, 2);
        add(separator);

        JSeparator separator_3 = new JSeparator();
        separator_3.setBounds(93, 297, 397, 2);
        add(separator_3);

        JLabel lblLogin = new JLabel("Login name");
        lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogin.setBounds(200, 106, 200, 19);
        add(lblLogin);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
        lblPassword.setBounds(200, 167, 200, 19);

        add(lblPassword);

        txtLogin = new JTextField();
        txtLogin.setBounds(211, 136, 173, 20);
        add(txtLogin);

        if (SaveSettings.loginHasToBeRemembered())
            txtLogin.setText(SaveSettings.getLogin());

        txtLogin.setColumns(10);

        txtPassword = new JPasswordField();
        txtPassword.setColumns(10);
        txtPassword.setBounds(211, 197, 173, 20);
        txtPassword.setText("ad12dac");
        add(txtPassword);

        chckbxRememberTheLogin = new JCheckBox("Remember the login");
        chckbxRememberTheLogin.setBounds(211, 247, 173, 23);
        chckbxRememberTheLogin.setOpaque(false);
        chckbxRememberTheLogin.setSelected(true);
        add(chckbxRememberTheLogin);

    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isEveryThingOk() {

        String login = txtLogin.getText();
        String password = txtPassword.getText();

        // check form
        if ("".equals(login)) {
            JOptionPane.showMessageDialog(null, "No login name given", "ERROR",
                    JOptionPane.ERROR_MESSAGE);

            return false;
        } else if ("".equals(password)) {
            JOptionPane.showMessageDialog(null, "No password given", "ERROR",
                    JOptionPane.ERROR_MESSAGE);

            return false;
        }

        if (MainWindow.getSession() != null
                && MainWindow.getSession().isSameLogin(login, password))
            return true;

        SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {

            @Override
            protected Boolean doInBackground() throws Exception {
                try {

                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                    checkAndSaveSettings();
                    ok = createSession(login, password);

                } finally {
                    setCursor(Cursor.getDefaultCursor());
                }

                return true;
            }

            @Override
            protected void done() {
                try {
                    get();
                    if (ok)
                        mainFrame.showNextPanel();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };

        try {
            worker.execute();

        } catch (Exception e) {
            // e.printStackTrace();

            JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }

        return false;
    }

    @Override
    public SettingsChoicePanel getNewInstance() {
        // TODO Auto-generated method stub
        return new LoginPanel(mainFrame);
    }

    private void checkAndSaveSettings() {
        SaveSettings.changeLoginHasToBeRemembered(
                chckbxRememberTheLogin.isSelected());
        if (chckbxRememberTheLogin.isSelected()) {
            SaveSettings.saveLoginName(txtLogin.getText());
        }
    }

    private boolean createSession(String login, String password)
            throws Exception {

        HttpMessage m = HttpDownload.canDownloadDataFromServer();
        if (!m.isOk()) {
            JOptionPane.showMessageDialog(null, m.getErrorMessage(), "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        HttpDownload htpdl = new HttpDownload(login, password);
        MainWindow.setSession(htpdl);

        return true;
    }

}
