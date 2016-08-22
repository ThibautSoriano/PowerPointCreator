package main.java.gui;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import main.java.gui.windowbuilder.MainWindowZhengqin;
import main.java.utils.FileType;
import main.java.utils.Internationalization;
import main.java.utils.Language;

@SuppressWarnings("serial")
public class ExcelChoicePanel extends SettingsChoicePanel {

    private List<JTextField> fields;

    private JTextField txtExcel;
    private JTextField txtExcel2;
    private JComboBox<Language> comboBox;

    public List<JTextField> getFields() {
        return fields;
    }

    public JTextField getTxtExcel() {
        return txtExcel;
    }

    public JTextField getTxtExcel2() {
        return txtExcel2;
    }

    public void setTxtExcel(JTextField txtExcel) {
        this.txtExcel = txtExcel;
    }

    public ExcelChoicePanel() {

        super(Internationalization.getKey("Choose excel file(s)"));

        fields = new ArrayList<JTextField>();

//        JLabel lblChooseExcel = new JLabel(
//                Internationalization.getKey("Choose an excel file"));
//        lblChooseExcel.setHorizontalAlignment(SwingConstants.CENTER);
//        lblChooseExcel.setBounds(30, 70, 400, 22);
//        add(lblChooseExcel);
//
//        lblChooseExcel.setFont(new Font("Tahoma", Font.PLAIN, 16));

        txtExcel = new JTextField();
        txtExcel.setBounds(30, 118, 389, 20);
        txtExcel.setColumns(10);

        

        add(txtExcel);
        fields.add(txtExcel);

        JButton btnExcel = new JButton(Internationalization.getKey("Browse"));
        btnExcel.setBounds(444, 118, 130, 23);
        add(btnExcel);
        btnExcel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MainWindow.openFileChooser(FileType.EXCEL, txtExcel);
            }
        });

//        JLabel lblExcel2 = new JLabel(
//                Internationalization.getKey("Choose an excel file"));
//        lblExcel2.setHorizontalAlignment(SwingConstants.CENTER);
//        lblExcel2.setBounds(30, 180, 400, 22);
//
//        lblExcel2.setFont(new Font("Tahoma", Font.PLAIN, 16));
//        add(lblExcel2);

        txtExcel2 = new JTextField();
        txtExcel2.setBounds(30, 220, 389, 20);
        add(txtExcel2);
        fields.add(txtExcel2);

        txtExcel2.setColumns(10);

        JButton btnExcel2 = new JButton(Internationalization.getKey("Browse"));
        btnExcel2.setBounds(444, 220, 130, 23);
        add(btnExcel2);

        btnExcel2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MainWindowZhengqin.openFileChooser(FileType.EXCEL, txtExcel2);
            }
        });
        
        comboBox = new JComboBox<>();
		comboBox.setBounds(390, 295, 177, 23);
        add(comboBox);
        
        
        for (Language lang : Language.values()) {
            comboBox.addItem(lang);
        }
                
        JLabel lblChoosePdfOutput = new JLabel("Choose pdf output language");
        lblChoosePdfOutput.setBounds(27, 295, 400, 23);
        lblChoosePdfOutput.setHorizontalAlignment(SwingConstants.CENTER);
        lblChoosePdfOutput.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblChoosePdfOutput);

    }

    @Override
    public boolean isEveryThingOk() {
        boolean ok = false;

        String txt = txtExcel.getText();
        String txt2 = txtExcel2.getText();

        File f = null,f2 = null;
        
        
        if (txt.isEmpty() && txt2.isEmpty()) {

            JOptionPane.showMessageDialog(null, Internationalization.getKey("No file submitted"), Internationalization.getKey("ERROR"),JOptionPane.ERROR_MESSAGE);
        }

        else if (!txt.contains(".xls") && !txt2.contains(".xls")) {
            JOptionPane.showMessageDialog(null, Internationalization.getKey("Wrong format"), Internationalization.getKey("ERROR"),JOptionPane.ERROR_MESSAGE);
        } else {

             f = new File(txt);
            f2 = new File(txt2);

            if (!f.exists() && !f2.exists()) {
                
                JOptionPane.showMessageDialog(null, Internationalization.getKey("None of the given files exist"), Internationalization.getKey("ERROR"),JOptionPane.ERROR_MESSAGE);
            }
            else if (!f.exists()){
                if (f.getName().isEmpty()) {
                    if (JOptionPane.showConfirmDialog(null, Internationalization.getKey("Only one file submitted.")+Internationalization.getKey("Continue anyway?"), Internationalization.getKey("Warning"), JOptionPane.YES_NO_OPTION)== JOptionPane.OK_OPTION)
                        ok = true;
                }
                else {
                JOptionPane.showMessageDialog(null, "\""+f+"\" "+Internationalization.getKey("does not exist."), Internationalization.getKey("Warning"), JOptionPane.ERROR_MESSAGE);
                txtExcel.setText("");   
                ok = false;
                }
            }
            else if (!f2.exists()) {
                if (f2.getName().isEmpty()) {
                    if (JOptionPane.showConfirmDialog(null, Internationalization.getKey("Only one file submitted.")+Internationalization.getKey("Continue anyway?"), Internationalization.getKey("Warning"), JOptionPane.YES_NO_OPTION)== JOptionPane.OK_OPTION)
                        ok = true;
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "\""+f2+"\" "+Internationalization.getKey("does not exist."), Internationalization.getKey("Warning"), JOptionPane.ERROR_MESSAGE);
                    txtExcel2.setText("");
                    ok = false;
                }
            }
            else {
                ok = true;
            }

        }
        
        MainWindow.setRankings(false);
        MainWindow.setTechnical(false);
        
        if (ok) { 
            
            if (f.getName().contains("Rankings") || f2.getName().contains("Rankings"))
                MainWindow.setRankings(true);
            
            if (f.getName().contains("Technical") || f2.getName().contains("Technical"))
                MainWindow.setTechnical(true);
        }
        
        
        return ok;
    }

    @Override
    public SettingsChoicePanel getNewInstance() {

        return new ExcelChoicePanel();
    }

  
    public Language getSelectedLanguage() {
	    return (Language) comboBox.getSelectedItem();
	}
        

    

}
