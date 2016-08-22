package main.java.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import main.java.utils.Internationalization;

@SuppressWarnings("serial")
public class MainMenuPanel extends SettingsChoicePanel {

    private MainWindow mainWindow;

    public MainMenuPanel(MainWindow mainWindow) {
        super("Pdf Generator");

        this.mainWindow = mainWindow;

        title.setBounds(0, 20, PANEL_WIDTH, 30);
        setBounds(0, 0, 600, 490);
        
        title.setFont(new Font("Narkisim", Font.PLAIN, 45));
        title.setBounds(44, 34, 504, 98);

        
        
        
        

        JButton btnFromAnExcel = new JButton(
                Internationalization.getKey("From an excel file on your computer"));
        btnFromAnExcel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                mainWindow.createPanelsExcel();
                MainMenuPanel.this.mainWindow.setDownload(false);
            }
        });
        btnFromAnExcel.setBounds(44, 241, 224, 51);
        add(btnFromAnExcel);

        JButton btnByDownloadingDatas = new JButton(Internationalization.getKey("From the Gemius Server"));
        btnByDownloadingDatas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainWindow.createPanelsInternet();
                MainMenuPanel.this.mainWindow.setDownload(true);
            }
        });

        
       
        btnByDownloadingDatas.setBounds(311, 241, 224, 51);
        add(btnByDownloadingDatas);

        JLabel lblChooseMode = new JLabel(Internationalization.getKey("Select how to import your files"));
        lblChooseMode.setForeground(Color.BLACK);
        lblChooseMode.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblChooseMode.setBounds(44, 150, 308, 67);
        add(lblChooseMode);

//        JLabel lblBackground = new JLabel("");

//        lblBackground.setIcon(
//                new ImageIcon(getClass().getResource("/background2.png")));
        
//        lblBackground.setIcon(
//              new ImageIcon(getClass().getResource("/blue.png")));
//        
//        lblBackground.setBounds(0, -23, 600, 490);
//        add(lblBackground);

    }

    @Override
    public boolean isEveryThingOk() {
        return true;
    }

    @Override
    public SettingsChoicePanel getNewInstance() {

        return new MainMenuPanel(mainWindow);
    }


}
