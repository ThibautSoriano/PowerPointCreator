package main.java.gui;

import java.awt.Font;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import main.java.utils.Internationalization;
import main.java.utils.JTextFieldLimit;

@SuppressWarnings("serial")
public class TitleSettingsPanel extends SettingsChoicePanel{

    private JTextField txtLogo;
    private JTextField txtBottomLeftText;
    private JCheckBox chckbxSeparatorLineAboveFooter;
    private JCheckBox chckbxSeparatorLineBelowHeader;
    private JCheckBox chckbxHeader;
    private JCheckBox chckbxFooter;
    private JTextArea txtrBelowTitle;

    public JCheckBox getChckbxSeparatorLineAboveFooter() {
        return chckbxSeparatorLineAboveFooter;
    }

    public JCheckBox getChckbxSeparatorLineBelowHeader() {
        return chckbxSeparatorLineBelowHeader;
    }

    public JCheckBox getChckbxHeader() {
        return chckbxHeader;
    }

    public JCheckBox getChckbxFooter() {
        return chckbxFooter;
    }

    public JTextField getTxtLogo() {
        return txtLogo;
    }

    public JTextField getTxtBottomLeftText() {
        return txtBottomLeftText;
    }

    public JTextArea getTxtrBelowTitle() {
        return txtrBelowTitle;
    }

    public TitleSettingsPanel() {

        super(Internationalization.getKey("Title page settings"));
        
        chckbxSeparatorLineAboveFooter = new JCheckBox(Internationalization.getKey("Separator line above footer"));
        chckbxSeparatorLineAboveFooter.setSelected(true);
        chckbxSeparatorLineAboveFooter.setBounds(284, 143, 300, 23);
        add(chckbxSeparatorLineAboveFooter);
        
        chckbxSeparatorLineBelowHeader = new JCheckBox(Internationalization.getKey("Separator line below header"));
        chckbxSeparatorLineBelowHeader.setSelected(true);
        chckbxSeparatorLineBelowHeader.setBounds(284, 71, 300, 23);
        add(chckbxSeparatorLineBelowHeader);
        
        JSeparator separator = new JSeparator();
        separator.setBounds(93, 57, 397, 2);
        add(separator);
        
        JSeparator separator_1 = new JSeparator();
        separator_1.setBounds(93, 111, 397, 2);
        add(separator_1);
        
        chckbxHeader = new JCheckBox(Internationalization.getKey("Header"));
        chckbxHeader.setBounds(93, 71, 200, 23);
        add(chckbxHeader);
        
        chckbxFooter = new JCheckBox(Internationalization.getKey("Footer"));
        chckbxFooter.setBounds(93, 143, 200, 23);
        add(chckbxFooter);
        
        JLabel lblTextBelowTitle = new JLabel(Internationalization.getKey("Text below title"));
        lblTextBelowTitle.setBounds(93, 210, 159, 19);
        add(lblTextBelowTitle);
        
        JSeparator separator_2 = new JSeparator();
        separator_2.setBounds(93, 189, 397, 2);
        add(separator_2);
        
        JSeparator separator_3 = new JSeparator();
        separator_3.setBounds(93, 297, 397, 2);
        add(separator_3);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(238, 209, 243, 71);
        
        add(scrollPane);
        
        txtrBelowTitle = new JTextArea();
        scrollPane.setViewportView(txtrBelowTitle);
        txtrBelowTitle.setFont(new Font("Tahoma", Font.PLAIN, 11));
        txtrBelowTitle.setDocument(new JTextFieldLimit(50));
        txtrBelowTitle.setText("Online kamp\u00E1ny elemz\u00E9se");
        txtrBelowTitle.setWrapStyleWord(true);
        txtrBelowTitle.setLineWrap(true);
        txtrBelowTitle.setVisible(true);
    }
    
    @Override
    public boolean isEveryThingOk() {
        return true;
    }

    @Override
    public SettingsChoicePanel getNewInstance() {
        return new TitleSettingsPanel();
    }
    
    

}
