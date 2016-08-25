package main.java.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.java.chartcreator.ImageCreator;
import main.java.excelreader.ExcelReader;
import main.java.excelreader.Triplet;
import main.java.ppthandler.PPTReader;
import main.java.utils.FileType;
import main.java.utils.Utils;

@SuppressWarnings("serial")
public class MainWindow extends JFrame  {

    
    ProgressBarWindow pbw;
    
    private BackgroundPanel backgroundPanel;

    private ExcelChoicePanel excelChoicePanel;
    

    public static int progress = 0;

    public static final int WINDOW_HEIGHT = 370;

    public static final int WINDOW_WIDTH = 500;

   



    public MainWindow() {

        pbw = new ProgressBarWindow();

        

        setResizable(false);
        setIconImage(Toolkit.getDefaultToolkit()
                .getImage(getClass().getResource("/icon.png")));
        setBounds(200, 100, WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle("PPT Creator");

        backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(null);
        setContentPane(backgroundPanel);

         excelChoicePanel = new ExcelChoicePanel();
        
        getContentPane().add(excelChoicePanel);
        
        
        JButton validate = new JButton("Validate");
        validate.setFont(new Font("Arial", Font.BOLD, 15));
        validate.setBounds(180, 228, 130, 40);
        add(validate);
        validate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (excelChoicePanel.isEveryThingOk())
                    validation();
            }
        });

        
        addMenu();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        

    }

  

    private void addMenu() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);

        JMenuItem mntmExit = new JMenuItem("Exit");
        mntmExit.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent arg0) {
                
                System.exit(0);
            }
        });
        mnFile.add(mntmExit);

        

       
       

    }

    public static void openFileChooser(final FileType fileType,
            final JTextField field) {
        final JFrame frame = new JFrame(
                "JFileChooser Popup");
        Container contentPane = frame.getContentPane();

        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setControlButtonsAreShown(true);
        contentPane.add(fileChooser, BorderLayout.CENTER);

        fileChooser.setAcceptAllFileFilterUsed(false);

        FileFilter filter = new FileNameExtensionFilter(
                fileType.getDescription(), fileType.getAcceptedExtensions());
        fileChooser.setFileFilter(filter);

        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser theFileChooser = (JFileChooser) actionEvent
                        .getSource();
                String command = actionEvent.getActionCommand();
                if (command.equals(JFileChooser.APPROVE_SELECTION)) {
                    File selectedFile = theFileChooser.getSelectedFile();

                    field.setText(selectedFile.getAbsolutePath());

                    frame.dispose();
                } else if (command.equals(JFileChooser.CANCEL_SELECTION)) {
                    frame.dispose();
                }
            }
        };

        fileChooser.addActionListener(actionListener);

        frame.pack();
        frame.setVisible(true);
    }

    
    public void validation() {

        SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {

            @Override
            protected Boolean doInBackground() throws Exception {
                try {
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    doValidationInSwingWorker();
                } finally {
                    setCursor(Cursor.getDefaultCursor());
                }
                
                return true;

            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
        pbw.setVisible(true);
        pbw.setValue(0);
        pbw.setText("");
        try {
            worker.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void doValidationInSwingWorker() {
        try {
            convertExcelToPPT(excelChoicePanel.getTxtExcel().getText());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public  void  convertExcelToPPT(String excelFilePath) throws IOException {

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        PPTReader ppt = new PPTReader(classLoader.getClass()
                .getResource("/ppt/EmptyTemplate.pptx").getPath());

        
        ExcelReader er = new ExcelReader(excelFilePath);

        ImageCreator merguez = new ImageCreator(); // for charts

        // slide 2
        ppt.fillSlideTextValues(er.getSecondSlideData(), 1,
                new Color(0, 176, 240));

        // slide 3
        List<BufferedImage> l = new ArrayList<>();
        l.add(merguez.getDoubleValueChart(
                (er.getDataForChartTopLeftSlides3_4(3)), false)); // top left
                                                                  // chart
        l.add(merguez.getPcMobilTabletChart(
                er.getDataForStackedChartSlide3_4(3, true), false, true)); // top
                                                                           // right
                                                                           // chart
        l.add(merguez.getDoubleValueChart(
                (er.getDataForChartBottmLeftSlides3_4(3)), true)); // bottom
                                                                   // left chart
        l.add(merguez.getPcMobilTabletChart(
                er.getDataForStackedChartSlide3_4(3, false), true, true)); // bottom
                                                                           // right
                                                                           // chart

        ppt.placePNGImages(l, 2);

        // slide 4
        l.clear();
        l.add(merguez.getDoubleValueChart(
                (er.getDataForChartTopLeftSlides3_4(4)), false)); // top left
                                                                  // chart
        l.add(merguez.getPcMobilTabletChart(
                er.getDataForStackedChartSlide3_4(4, true), false, true)); // top
                                                                           // right
                                                                           // chart
        l.add(merguez.getDoubleValueChart(
                (er.getDataForChartBottmLeftSlides3_4(4)), true)); // bottom
                                                                   // left chart
        l.add(merguez.getPcMobilTabletChart(
                er.getDataForStackedChartSlide3_4(4, false), true, true)); // bottom
                                                                           // right
                                                                           // chart

        ppt.placePNGImages(l, 3);

        // slides 5-10
        l.clear();
        boolean percentage = false;
        for (int i = 4; i < 10; i++) {

            l.add(merguez.getAllAgesChart(
                    er.getDataForBarChartsSlide5_10(i + 1), percentage));
            l.add(merguez.getPcMobilTabletChart(
                    er.getDataForStackedChartSlide5_10(i + 1), percentage,
                    false));

            percentage = !percentage;
            ppt.placePNGImages(l, i);
            l.clear();

        }

        // slides 11 - 14 (on the ppt, 8-11 is for the excel)
        for (int i = 8; i < 12; i++) {
            List<Triplet> triplets = er.getDataForChartSlides8_11(i);
            triplets.sort(new Comparator<Triplet>() {

                @Override
                public int compare(Triplet arg0, Triplet arg1) {

                    return Double.compare(arg1.getValue(), arg0.getValue());
                }

            });

            Map<String, String> dataToPutInSlide = new HashMap<>();

            dataToPutInSlide.put("first",
                    triplets.get(0).getX() + ", " + triplets.get(0).getY());
            dataToPutInSlide.put("second",
                    triplets.get(1).getX() + ", " + triplets.get(1).getY());
            dataToPutInSlide.put("third",
                    triplets.get(2).getX() + ", " + triplets.get(2).getY());

            ppt.fillSlideTextValues(dataToPutInSlide, i + 2,
                    new Color(0, 112, 192));

            BufferedImage img = merguez.getTop3BarChart(triplets.subList(0, 3));

            List<BufferedImage> li = new ArrayList<>();
            li.add(img);

            ppt.placePNGImages(li, i + 2);

        }

        String name = new File(excelFilePath).getName();
        String fileNameToSave = Utils.getFileName(name.substring(0, name.lastIndexOf('.')), "pptx");
        ppt.save(fileNameToSave);
        ppt.close();
        er.close();
        pbw.setValue(100);      
        
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File(fileNameToSave);
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                // no application registered for PDFs
            }
        }
        

    }

}
