package main.java.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import main.java.utils.FileType;
import main.java.utils.Internationalization;
import main.java.utils.JTextFieldLimit;

@SuppressWarnings("serial")
public class GeneralSettingsPanel extends SettingsChoicePanel {

    private JTextField txtLogo;
    private JTextField txtBottomLeftText;
    private JRadioButton rdbtnBottomCenter;
    private JRadioButton rdbtnBottomRight;
    private ButtonGroup pagesNumGroup;
    private JCheckBox chckbxFooterLine;
    private JCheckBox chckbxHeaderLine;
    private JTextField txtWebsite;
    private JCheckBox chckbxHeader;
    private JCheckBox chckbxFooter;
    private JRadioButton rdbtnTop;
    private JRadioButton rdbtnBottom;
    private JLabel lblTableOfContent;
    private JLabel lblPageNumerotation;
    private JLabel lblBottomLeftText;
    
    private boolean tableOfContent;

    public JRadioButton getRdbtnTop() {
        return rdbtnTop;
    }

    public JTextField getTxtLogo() {
        return txtLogo;
    }

    public JTextField getTxtBottomLeftText() {
        return txtBottomLeftText;
    }

    public JRadioButton getRdbtnBottomCenter() {
        return rdbtnBottomCenter;
    }

    public JRadioButton getRdbtnBottomRight() {
        return rdbtnBottomRight;
    }

    public ButtonGroup getPagesNumGroup() {
        return pagesNumGroup;
    }

    public JCheckBox getChckbxFooterLine() {
        return chckbxFooterLine;
    }

    public JCheckBox getChckbxHeaderLine() {
        return chckbxHeaderLine;
    }

    public JTextField getTxtWebsite() {
        return txtWebsite;
    }

    public JCheckBox getChckbxHeader() {
        return chckbxHeader;
    }

    public JCheckBox getChckbxFooter() {
        return chckbxFooter;
    }

    public GeneralSettingsPanel(boolean tableOfContent) {

        super(Internationalization.getKey("General settings"));

        this.tableOfContent = tableOfContent;
        rdbtnBottomCenter = new JRadioButton(
                Internationalization.getKey("Bottom center"));
        rdbtnBottomCenter.setBounds(245, 281, 152, 23);
        add(rdbtnBottomCenter);
        rdbtnBottomCenter.setOpaque(false);
        rdbtnBottomCenter.setSelected(true);

        rdbtnBottomRight = new JRadioButton(
                Internationalization.getKey("Bottom right"));
        rdbtnBottomRight.setBounds(399, 281, 152, 23);
        rdbtnBottomRight.setOpaque(false);
        add(rdbtnBottomRight);

        pagesNumGroup = new ButtonGroup();
        pagesNumGroup.add(rdbtnBottomRight);
        pagesNumGroup.add(rdbtnBottomCenter);

        chckbxFooterLine = new JCheckBox(
                Internationalization.getKey("Separator line above footer"));
        chckbxFooterLine.setSelected(true);
        chckbxFooterLine.setBounds(284, 201, 189, 23);
        chckbxFooterLine.setOpaque(false);
        add(chckbxFooterLine);

        chckbxHeaderLine = new JCheckBox(
                Internationalization.getKey("Separator line below header"));
        chckbxHeaderLine.setSelected(true);
        chckbxHeaderLine.setBounds(284, 70, 189, 23);
        chckbxHeaderLine.setOpaque(false);
        add(chckbxHeaderLine);

        JSeparator separator = new JSeparator();
        separator.setBounds(93, 57, 397, 2);
        add(separator);

        JSeparator separator_1 = new JSeparator();
        separator_1.setBounds(93, 57, 397, 2);
        add(separator_1);

        JSeparator separator_2 = new JSeparator();
        separator_2.setBounds(93, 183, 397, 2);
        add(separator_2);

        JLabel lblYourCompanyWebsite = new JLabel(
                Internationalization.getKey("Your company website"));
        lblYourCompanyWebsite.setBounds(93, 111, 159, 19);
        add(lblYourCompanyWebsite);

        txtWebsite = new JTextField();
        txtWebsite.setColumns(10);
        txtWebsite.setDocument(new JTextFieldLimit(30));
        txtWebsite.setText("www.gemius.hu");
        txtWebsite.setBounds(284, 110, 177, 20);
        add(txtWebsite);

        chckbxHeader = new JCheckBox(Internationalization.getKey("Header"));
        chckbxHeader.setSelected(true);
        chckbxHeader.setBounds(93, 70, 189, 23);
        chckbxHeader.setOpaque(false);
        add(chckbxHeader);

        chckbxFooter = new JCheckBox(Internationalization.getKey("Footer"));
        chckbxFooter.setSelected(true);
        chckbxFooter.setBounds(93, 201, 189, 23);
        chckbxFooter.setOpaque(false);
        add(chckbxFooter);

        chckbxFooter.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (chckbxFooter.isSelected())
                    setVisibilityFooterOptions(true);
                else
                    setVisibilityFooterOptions(false);

            }
        });

        JButton btnBrowse = new JButton("");
        btnBrowse.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent arg0) {
                MainWindow.openFileChooser(FileType.LOGO, txtLogo);
            }
        });
        btnBrowse.setIcon(new ImageIcon(getClass().getResource("/Browse.png")));
        btnBrowse.setBounds(471, 142, 25, 25);
        btnBrowse.setBorder(new EmptyBorder(0, 0, 0, 0));

        add(btnBrowse);

        txtLogo = new JTextField();
        txtLogo.setColumns(10);
        txtLogo.setBounds(284, 145, 177, 20);
        add(txtLogo);

        JLabel lblYourCompanyLogo = new JLabel(
                Internationalization.getKey("Your company logo"));
        lblYourCompanyLogo.setBounds(93, 145, 168, 22);
        add(lblYourCompanyLogo);

        lblBottomLeftText = new JLabel(
                Internationalization.getKey("Bottom left text"));
        lblBottomLeftText.setBounds(93, 245, 159, 19);

        add(lblBottomLeftText);

        txtBottomLeftText = new JTextField();
        txtBottomLeftText.setColumns(10);
        txtBottomLeftText.setBounds(284, 244, 177, 20);
        txtBottomLeftText.setDocument(new JTextFieldLimit(25));
        add(txtBottomLeftText);

        JSeparator separator_3 = new JSeparator();
        separator_3.setBounds(93, 360, 397, 2);
        add(separator_3);

        lblPageNumerotation = new JLabel(
                Internationalization.getKey("Page numerotation"));
        lblPageNumerotation.setBounds(93, 283, 115, 19);
        add(lblPageNumerotation);

        if (tableOfContent) {
            lblTableOfContent = new JLabel("Table of content position");
            lblTableOfContent.setBounds(93, 324, 159, 14);
            add(lblTableOfContent);

            rdbtnTop = new JRadioButton("Top");
            rdbtnTop.setBounds(287, 320, 109, 23);
            rdbtnTop.setSelected(true);
            rdbtnTop.setOpaque(false);
            add(rdbtnTop);

            rdbtnBottom = new JRadioButton("Bottom");
            rdbtnBottom.setBounds(398, 320, 109, 23);
            rdbtnBottom.setOpaque(false);
            add(rdbtnBottom);

            ButtonGroup groupTableOfContent = new ButtonGroup();
            groupTableOfContent.add(rdbtnTop);
            groupTableOfContent.add(rdbtnBottom);
        }
    }

    @Override
    public boolean isEveryThingOk() {
        return true;
    }

    @Override
    public SettingsChoicePanel getNewInstance() {

        return new GeneralSettingsPanel(tableOfContent);
    }

    private void setVisibilityFooterOptions(boolean visible) {
        lblTableOfContent.setVisible(visible);
        rdbtnTop.setVisible(visible);
        rdbtnBottom.setVisible(visible);

        lblBottomLeftText.setVisible(visible);
        txtBottomLeftText.setVisible(visible);

        lblPageNumerotation.setVisible(visible);
        rdbtnBottomCenter.setVisible(visible);
        rdbtnBottomRight.setVisible(visible);

    }

}
