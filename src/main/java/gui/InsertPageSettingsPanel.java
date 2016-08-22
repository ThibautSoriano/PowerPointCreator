package main.java.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

import main.java.utils.Internationalization;
import main.java.utils.JTextFieldLimit;

@SuppressWarnings("serial")
public class InsertPageSettingsPanel extends SettingsChoicePanel {

    private JCheckBox chckbxSeparatorLineAbove;
    private JCheckBox chckbxSeparatorLineBelow;
    private JCheckBox chckbxHeader;
    private JCheckBox chckbxFooter;
    private JTextArea txtrCreatedBy;
    private JRadioButton rdbtnOn;
    private JRadioButton rdbtnOff;

   
    private JLabel lblCustomTextArea;
    private JSeparator separator_3;
    private JScrollPane scrollPane;
    private JSeparator separator_2;
    private JSeparator separator_1;

    public JCheckBox getChckbxSeparatorLineAbove() {
        return chckbxSeparatorLineAbove;
    }

    public JCheckBox getChckbxSeparatorLineBelow() {
        return chckbxSeparatorLineBelow;
    }

    public JCheckBox getChckbxHeader() {
        return chckbxHeader;
    }

    public JCheckBox getChckbxFooter() {
        return chckbxFooter;
    }

    public JTextArea getTxtrCreatedBy() {
        return txtrCreatedBy;
    }

    public JRadioButton getRdbtnOn() {
        return rdbtnOn;
    }

    public JRadioButton getRdbtnOff() {
        return rdbtnOff;
    }

    protected InsertPageSettingsPanel() {
        super(Internationalization.getKey("Insert Page Settings"));

        chckbxSeparatorLineAbove = new JCheckBox(
                Internationalization.getKey("Separator line above footer"));
        chckbxSeparatorLineAbove.setSelected(true);
        chckbxSeparatorLineAbove.setBounds(284, 211, 220, 23);
        add(chckbxSeparatorLineAbove);

        chckbxSeparatorLineBelow = new JCheckBox(
                Internationalization.getKey("Separator line below header"));
        chckbxSeparatorLineBelow.setSelected(true);
        chckbxSeparatorLineBelow.setBounds(284, 139, 220, 23);
        add(chckbxSeparatorLineBelow);

        JSeparator separator = new JSeparator();
        separator.setBounds(93, 125, 397, 2);
        add(separator);

         separator_1 = new JSeparator();
        separator_1.setBounds(93, 179, 397, 2);
        add(separator_1);

        chckbxHeader = new JCheckBox(Internationalization.getKey("Header"));
        chckbxHeader.setBounds(93, 139, 189, 23);
        add(chckbxHeader);

        chckbxFooter = new JCheckBox(Internationalization.getKey("Footer"));
        chckbxFooter.setBounds(93, 211, 189, 23);
        add(chckbxFooter);

        separator_2 = new JSeparator();
        separator_2.setBounds(93, 257, 397, 2);
        add(separator_2);

        lblCustomTextArea = new JLabel(
                Internationalization.getKey("Custom text area"));
        lblCustomTextArea.setBounds(93, 280, 127, 19);
        
        add(lblCustomTextArea);

        separator_3 = new JSeparator();
        separator_3.setBounds(93, 367, 397, 2);
        add(separator_3);

        txtrCreatedBy = new JTextArea();
        txtrCreatedBy.setWrapStyleWord(true);
        txtrCreatedBy.setFont(new Font("Tahoma", Font.PLAIN, 11));
        txtrCreatedBy.setDocument(new JTextFieldLimit(50));
        txtrCreatedBy.setText("k\u00E9sz\u00EDtette :");
        txtrCreatedBy.setLineWrap(true);
        txtrCreatedBy.setBounds(239, 280, 241, 69);
        add(txtrCreatedBy);

        scrollPane = new JScrollPane();
        scrollPane.setBounds(239, 280, 241, 69);
        scrollPane.setViewportView(txtrCreatedBy);
        add(scrollPane);

        JLabel lblInsertPage = new JLabel(
                Internationalization.getKey("Insert page"));
        lblInsertPage.setBounds(93, 81, 127, 19);
        add(lblInsertPage);

        rdbtnOn = new JRadioButton(Internationalization.getKey("On"));
        rdbtnOn.setBounds(282, 79, 77, 23);
        rdbtnOn.setSelected(true);
        rdbtnOn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setOptionsVisible(true);

            }
        });
        add(rdbtnOn);

        rdbtnOff = new JRadioButton(Internationalization.getKey("Off"));
        rdbtnOff.setBounds(384, 79, 77, 23);

        rdbtnOff.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setOptionsVisible(false);

            }
        });
        add(rdbtnOff);

        ButtonGroup g = new ButtonGroup();
        g.add(rdbtnOff);
        g.add(rdbtnOn);

        JSeparator separator_4 = new JSeparator();
        separator_4.setBounds(93, 57, 397, 2);
        add(separator_4);

    }

    @Override
    public boolean isEveryThingOk() {
        return true;
    }

    @Override
    public SettingsChoicePanel getNewInstance() {

        return new InsertPageSettingsPanel();
    }

    private void setOptionsVisible(boolean visibility) {
        chckbxSeparatorLineAbove.setVisible(visibility);
        chckbxSeparatorLineBelow.setVisible(visibility);
        chckbxHeader.setVisible(visibility);
        chckbxFooter.setVisible(visibility);
        txtrCreatedBy.setVisible(visibility);
        separator_2.setVisible(visibility);
        lblCustomTextArea.setVisible(visibility);
        separator_1.setVisible(visibility);
        scrollPane.setVisible(visibility);
        separator_3.setVisible(visibility);

    }

}
