package main.java.gui;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import main.java.utils.FileType;

@SuppressWarnings("serial")
public class ExcelChoicePanel extends JPanel {

    public static final int PANEL_WIDTH = 500;
    public static final int PANEL_HEIGHT = 380;

    private JTextField txtExcel;
    private JLabel title;

    public JTextField getTxtExcel() {
        return txtExcel;
    }

    public void setTxtExcel(JTextField txtExcel) {
        this.txtExcel = txtExcel;
    }

    public ExcelChoicePanel() {

        setOpaque(false); // for the background image of the main window
        setLayout(null);
        setBounds(0, 20, PANEL_WIDTH, PANEL_HEIGHT);

        title = new JLabel();
        title.setBounds(0, 30, PANEL_WIDTH, 30);
        title.setText("Choose an excel file");
        title.setHorizontalAlignment(SwingConstants.CENTER);

        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 25));

        add(title);

        txtExcel = new JTextField();
        txtExcel.setBounds(30, 118, 289, 20);
        txtExcel.setColumns(10);

        add(txtExcel);

        JButton btnExcel = new JButton("Browse");
        btnExcel.setBounds(344, 118, 130, 23);
        add(btnExcel);
        btnExcel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MainWindow.openFileChooser(FileType.EXCEL, txtExcel);
            }
        });

       

    }

    public boolean isEveryThingOk() {
        boolean ok = false;

        String txt = txtExcel.getText();

        if (txt.isEmpty()) {

            JOptionPane.showMessageDialog(null, "No file submitted", "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        else if (!txt.contains(".xls")) {
            JOptionPane.showMessageDialog(null, "Wrong format", "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            File f = new File(txt);
            if (!f.exists()) {
                JOptionPane.showMessageDialog(null,
                        "The given file does not exists.", "ERROR",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;

    }

}
