package main.java.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.itextpdf.text.DocumentException;

import main.java.datasdownloading.HttpDownload;
import main.java.datasdownloading.HttpMessage;
import main.java.datasdownloading.entities.Campaign;
import main.java.datasdownloading.entities.PeriodData;
import main.java.datasdownloading.entities.SummaryData;
import main.java.excelreader.ExcelReader;
import main.java.excelreader.ExcelReaderRankings;
import main.java.excelreader.ExcelReaderTechnical;
import main.java.excelreader.entities.CampaignRow;
import main.java.exceltopdf.ExcelToPdf;
import main.java.exceltopdf.HeaderFooter;
import main.java.exceltopdf.pdfsections.ContentPage;
import main.java.exceltopdf.pdfsections.InsertPage;
import main.java.exceltopdf.pdfsections.PeriodTotalPage;
import main.java.exceltopdf.pdfsections.Section;
import main.java.exceltopdf.pdfsections.SummaryPage;
import main.java.exceltopdf.pdfsections.TitlePage;
import main.java.utils.FileType;
import main.java.utils.Internationalization;
import main.java.utils.InternationalizationPDF;
import main.java.utils.Language;
import main.java.utils.SaveSettings;
import main.java.utils.Utils;

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements IMainFrame {

    private List<SettingsChoicePanel> panels;
    private int currentPanel = 0;
    private NavigationPanel np;
    private BackgroundPanel backgroundPanel;
    private boolean download;

    public static int progress = 0;

    public static final int WINDOW_HEIGHT = 500;

    public static final int WINDOW_WIDTH = 600;

    private static HttpDownload session;

    private static boolean isRankings;

    private static boolean isTechnical;

    private static boolean isCreative;

    private static boolean isTimePeriodTotal;

    private static boolean isSummary;

    public static ProgressBarWindow pbw;

    public static boolean isSummary() {
        return isSummary;
    }

    public static void setSummary(boolean isSummary) {
        MainWindow.isSummary = isSummary;
    }

    public static boolean isTimePeriodTotal() {
        return isTimePeriodTotal;
    }

    public static void setTimePeriodTotal(boolean isTimePeriodTotal) {
        MainWindow.isTimePeriodTotal = isTimePeriodTotal;
    }

    public static boolean isRankings() {
        return isRankings;
    }

    public static boolean isTechnical() {
        return isTechnical;
    }

    public static void setRankings(boolean isRankings) {
        MainWindow.isRankings = isRankings;
    }

    public static void setTechnical(boolean isTechnical) {
        MainWindow.isTechnical = isTechnical;
    }

    public static HttpDownload getSession() {
        return session;
    }

    public static void setSession(HttpDownload session) {
        MainWindow.session = session;
    }

    public boolean isDownload() {
        return download;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }

    public static boolean isCreative() {
        return isCreative;
    }

    public static void setCreative(boolean isCreative) {
        MainWindow.isCreative = isCreative;
    }

    public MainWindow() {

        pbw = new ProgressBarWindow();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                if (session != null)
                    session.close();
            }
        });

        setResizable(false);
        setIconImage(Toolkit.getDefaultToolkit()
                .getImage(getClass().getResource("/icon.png")));
        setBounds(200, 100, WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle(Internationalization.getKey("Converter"));

        // getContentPane().setLayout(null);
        backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(null);
        setContentPane(backgroundPanel);

        showFirstPanel();

        addMenu();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    public static void main(String[] args) {

        try {
            // set for file chooser look
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        new MainWindow();

    }

    public void showNextPanel() {

        getContentPane().remove(panels.get(currentPanel));
        currentPanel++;
        getContentPane().add(panels.get(currentPanel));

        repaint();
        setVisible(true);
    }

    public void showPreviousPanel() {

        getContentPane().remove(panels.get(currentPanel));
        currentPanel--;
        getContentPane().add(panels.get(currentPanel));

        repaint();
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
                if (session != null)
                    session.close();
                System.exit(0);
            }
        });
        mnFile.add(mntmExit);

        JMenu mnSettings = new JMenu("Settings");
        menuBar.add(mnSettings);

        JMenu mnLanguage = new JMenu("Language");
        mnSettings.add(mnLanguage);

        ButtonGroup g = new ButtonGroup();

        for (final Language lang : Language.values()) {

            final JRadioButtonMenuItem rdbtnmntm = new JRadioButtonMenuItem(
                    lang.toString());
            if (lang == Internationalization.getCurrentLanguage())
                rdbtnmntm.setSelected(true);
            rdbtnmntm.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    Internationalization.loadLanguage(lang);
                    SaveSettings.changeAppLanguage(
                            lang.getLanguage().toUpperCase());

                    reloadPanels();

                    if (np != null) {
                        getContentPane().remove(np);
                        np = np.getNewInstance();

                        if (currentPanel == panels.size() - 1)
                            np.showValidateButton();

                        getContentPane().add(np);
                    }

                    rdbtnmntm.setSelected(true);
                    repaint();
                    setVisible(true);
                }
            });

            g.add(rdbtnmntm);
            mnLanguage.add(rdbtnmntm);
        }

    }

    public static void openFileChooser(final FileType fileType,
            final JTextField field) {
        final JFrame frame = new JFrame(
                Internationalization.getKey("JFileChooser Popup"));
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

    @Override
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

        if (download) {
            validationDownload();
        } else {
            
            pbw.setText("Parsing the excel file");
            List<Section> sections = new ArrayList<Section>();

            ExcelChoicePanel ecp = (ExcelChoicePanel) panels.get(0);
            GeneralSettingsPanel gsp = (GeneralSettingsPanel) panels.get(1);
            TitleSettingsPanel tsp = (TitleSettingsPanel) panels.get(2);
            InsertPageSettingsPanel isp = (InsertPageSettingsPanel) panels
                    .get(3);
            ColumnsSettingsPanel csp = (ColumnsSettingsPanel) panels.get(4);

            InternationalizationPDF ipdf = new InternationalizationPDF(
                    ecp.getSelectedLanguage());
            ExcelToPdf etpd = new ExcelToPdf(
                    ecp.getSelectedLanguage().getEncoding(), true, false,
                    ecp.getSelectedLanguage().getDateFormat(), true);

            int positionPageCount = gsp.getRdbtnBottomCenter().isSelected()
                    ? HeaderFooter.PAGE_COUNT_MIDDLE
                    : HeaderFooter.PAGE_COUNT_RIGHT;

            pbw.setValue(10);
            // Infos for the title page
            TitlePage tp = new TitlePage();

            HeaderFooter hfTitle = new HeaderFooter(
                    tsp.getChckbxHeader().isSelected(),
                    tsp.getChckbxSeparatorLineBelowHeader().isSelected(),
                    tsp.getChckbxFooter().isSelected(),
                    tsp.getChckbxSeparatorLineAboveFooter().isSelected(),
                    HeaderFooter.NO_PAGE_COUNT);

            hfTitle.setLineInFooter(gsp.getTxtBottomLeftText().getText());
            hfTitle.setLineInHeader(gsp.getTxtWebsite().getText());
            hfTitle.setLogoInHeader(gsp.getTxtLogo().getText());
            tp.setStructure(hfTitle);
            tp.setBelowTitle(tsp.getTxtrBelowTitle().getText());

            sections.add(tp);

            // Infos for the insert page
            InsertPage ip = new InsertPage();

            HeaderFooter hfInsert = new HeaderFooter(
                    isp.getChckbxHeader().isSelected(),
                    isp.getChckbxSeparatorLineBelow().isSelected(),
                    isp.getChckbxFooter().isSelected(),
                    isp.getChckbxSeparatorLineAbove().isSelected(),
                    HeaderFooter.NO_PAGE_COUNT);

            hfInsert.setLineInHeader(gsp.getTxtWebsite().getText());
            hfInsert.setLineInFooter(gsp.getTxtBottomLeftText().getText());
            hfInsert.setLogoInHeader(gsp.getTxtLogo().getText());

            ip.setCustomTextArea(Utils
                    .splitLongTextToFitPage(isp.getTxtrCreatedBy().getText()));
            ip.setStructure(hfInsert);

            sections.add(ip);

            // Columns choice settings
            pbw.setValue(30);
            List<JTextField> fields = ecp.getFields();
            ContentPage contentPage = null;
            int progress = 20;
            for (JTextField jTextField : fields) {
                String src = jTextField.getText();
                if (!src.isEmpty()) {

                    ExcelReader excelReader = null;
                    Campaign zk = null;

                    if (src.contains("Rankings")) {
                        excelReader = new ExcelReaderRankings();
                        contentPage = new ContentPage(
                                csp.getChckbxImpressionsRankings().isSelected(),
                                csp.getChckbxFrequencyRankings().isSelected(),
                                csp.getChckbxClicksRankings().isSelected(),
                                csp.getChckbxClickingUsersRankings()
                                        .isSelected(),
                                csp.getChckbxClickThroughRateRankings()
                                        .isSelected(),
                                csp.getChckbxUniqueCTRRankings().isSelected());
                        contentPage.setUniqueCookies(csp
                                .getChckbxUniqueCookiesRankings().isSelected());
                        contentPage.setChartType(ContentPage.BAR_CHART);
                        
                        zk = excelReader.readExcelSheet(src);
                        zk.setTitle("Rankings");
                        contentPage.setCampaign(zk);

                    } else if (src.contains("Technical")) {
                        excelReader = new ExcelReaderTechnical();
                        contentPage = new ContentPage(
                                csp.getChckbxImpressionsTechnical()
                                        .isSelected(),
                                csp.getChckbxFrequencyTechnical().isSelected(),
                                csp.getChckbxClicksTechnical().isSelected(),
                                csp.getChckbxClickingUsersTechnical()
                                        .isSelected(),
                                csp.getChckbxClickThroughRateTechnical()
                                        .isSelected(),
                                csp.getChckbxUniqueCTRTechnical().isSelected());
                        contentPage.setUniqueCookies(
                                csp.getChckbxUniqueCookiesTechnical()
                                        .isSelected());
                        contentPage.setChartType(ContentPage.PIE_CHART);
                        
                        zk = excelReader.readExcelSheet(src);
                        zk.setTitle("Technical");
                        contentPage.setCampaign(zk);

                    } else {
                        System.err.println("xls not recognized");
                        return;
                    }
                   

                    tp.setCampaignName(
                            zk.getCampaignHeader().getCampaignName());
                    tp.setDate(ipdf.getDateFromTo(
                            zk.getCampaignHeader().getStartDate(),
                            zk.getCampaignHeader().getEndDate()));

                    HeaderFooter hfContent = new HeaderFooter(
                            gsp.getChckbxHeader().isSelected(),
                            gsp.getChckbxHeaderLine().isSelected(),
                            gsp.getChckbxFooter().isSelected(),
                            gsp.getChckbxFooterLine().isSelected(),
                            positionPageCount);
                    hfContent.setLineInHeader(gsp.getTxtWebsite().getText());
                    hfContent.setLineInFooter(
                            gsp.getTxtBottomLeftText().getText());
                    hfContent.setLogoInHeader(gsp.getTxtLogo().getText());
                    contentPage.setStructure(hfContent);
                    sections.add(contentPage);
                    
                    
                }
                pbw.setValue(30+progress/fields.size());
            }
            pbw.setText("Creating the pdf");
            try {
                etpd.createPdf(
                        Utils.getPdfName(contentPage.getCampaign()
                                .getCampaignHeader().getCampaignName()),
                        sections, isp.getRdbtnOn().isSelected());
                pbw.setValue(100);
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                pbw.setValue(0);
                pbw.setVisible(false);
            }
            
            
        }

    }

    private void validationDownload() {
        
        pbw.setText("Checking internet connection");
        
        HttpMessage m = HttpDownload.canDownloadDataFromServer();
        if (!m.isOk()) {
            pbw.setVisible(false);
            JOptionPane.showMessageDialog(null,m.getErrorMessage(), "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            
            return ;
        }
        
        pbw.setText("Downloading data from the Gemius Server");
        

        List<SummaryData> summary = new ArrayList<>();

        List<Section> sections = new ArrayList<Section>();

        CampaignChoicePanel ccp = (CampaignChoicePanel) panels.get(1);
        ModulesSettingsPanel msp = (ModulesSettingsPanel) panels.get(2);
        GeneralSettingsPanel gsp = (GeneralSettingsPanel) panels.get(3);
        TitleSettingsPanel tsp = (TitleSettingsPanel) panels.get(4);
        InsertPageSettingsPanel isp = (InsertPageSettingsPanel) panels.get(5);
        ColumnsSettingsPanel csp = (ColumnsSettingsPanel) panels.get(6);

        InternationalizationPDF ipdf = new InternationalizationPDF(
                msp.getSelectedLanguage());
        ExcelToPdf etpd = new ExcelToPdf(
                msp.getSelectedLanguage().getEncoding(),
                msp.getChckbxWholeTotal().isSelected(),
                msp.getChckbxTimePeriodTotal().isSelected(),
                msp.getSelectedLanguage().getDateFormat(), gsp.getRdbtnTop().isSelected());

        String campaignID = ccp.getSelectedId();

        PeriodData weeklyData = null, monthlyData = null;

        // TODO optimiser ca
        if (msp.getChckbxTimePeriodTotal().isSelected()
                || msp.getChckbxMonthlyCreative().isSelected()
                || msp.getChckbxWeeklyCreative().isSelected()
                || msp.getChckbxMonthlyRankings().isSelected()
                || msp.getChckbxWeeklyRankings().isSelected()) {
            monthlyData = session.getPeriodData("Month", campaignID);
            weeklyData = session.getPeriodData("Week", campaignID);
            if (monthlyData == null || weeklyData == null) {
                return;
            }
            etpd.setMonthlyData(monthlyData);
            etpd.setWeeklyData(weeklyData);
        }

        //

        Campaign c1 = null, c2 = null, c3 = null;
        boolean error = false;

        pbw.setValue(10);
        if (msp.detectRankings() || msp.getChckbxSummary().isSelected()) {
            c1 = session.getCampaignRankingsById(campaignID,
                    (msp.getChckbxRankings().isSelected()
                            || msp.getChckbxSummary().isSelected()),
                    msp.getChckbxMonthlyRankings().isSelected(),
                    msp.getChckbxWeeklyRankings().isSelected());
            if (c1 == null)
                error = true;
        }
        pbw.setValue(30);
        if (msp.getChckbxTechnical().isSelected()) {
            c2 = session.getCampaignTechnicalById(campaignID);
            if (c2 == null)
                error = true;
        }

        pbw.setValue(50);
        if (msp.detectCreative()) {
            c3 = session.getCampaignCreativeById(campaignID,
                    msp.getChckbxCreative().isSelected(),
                    msp.getChckbxMonthlyCreative().isSelected(),
                    msp.getChckbxWeeklyCreative().isSelected());
            if (c3 == null)
                error = true;
        }
        pbw.setValue(70);
        if (error) {
            pbw.dispose();
            JOptionPane.showMessageDialog(null,
                    "The connection with the server failed", "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            
            return;
        }

        pbw.setText("Creating the pdf");
        
        Campaign commonInfos = null;

        if (c1 != null)
            commonInfos = c1;
        else if (c2 != null)
            commonInfos = c2;
        else if (c3 != null)
            commonInfos = c3;


        int positionPageCount = gsp.getRdbtnBottomCenter().isSelected()
                ? HeaderFooter.PAGE_COUNT_MIDDLE
                : HeaderFooter.PAGE_COUNT_RIGHT;

        // Infos for the title page
        TitlePage tp = new TitlePage();

        HeaderFooter hfTitle = new HeaderFooter(
                tsp.getChckbxHeader().isSelected(),
                tsp.getChckbxSeparatorLineBelowHeader().isSelected(),
                tsp.getChckbxFooter().isSelected(),
                tsp.getChckbxSeparatorLineAboveFooter().isSelected(),
                HeaderFooter.NO_PAGE_COUNT);

        hfTitle.setLineInFooter(gsp.getTxtBottomLeftText().getText());
        hfTitle.setLineInHeader(gsp.getTxtWebsite().getText());
        hfTitle.setLogoInHeader(gsp.getTxtLogo().getText());

        tp.setStructure(hfTitle);
        tp.setBelowTitle(Utils
                .splitLongTextToFitPage(tsp.getTxtrBelowTitle().getText()));
        tp.setCampaignName(commonInfos.getCampaignHeader().getCampaignName());

        tp.setDate(ipdf.getDateFromTo(
                commonInfos.getCampaignHeader().getStartDate(),
                commonInfos.getCampaignHeader().getEndDate()));

        sections.add(tp);

        // Infos for the insert page
        InsertPage ip = new InsertPage();

        HeaderFooter hfInsert = new HeaderFooter(
                isp.getChckbxHeader().isSelected(),
                isp.getChckbxSeparatorLineBelow().isSelected(),
                isp.getChckbxFooter().isSelected(),
                isp.getChckbxSeparatorLineAbove().isSelected(),
                HeaderFooter.NO_PAGE_COUNT);

        hfInsert.setLineInHeader(gsp.getTxtWebsite().getText());
        hfInsert.setLineInFooter(gsp.getTxtBottomLeftText().getText());
        hfInsert.setLogoInHeader(gsp.getTxtLogo().getText());

        ip.setCustomTextArea(isp.getTxtrCreatedBy().getText());
        ip.setStructure(hfInsert);

        sections.add(ip);
        pbw.setValue(75);

        // it is the same for the two possible content pages
        HeaderFooter hfContent = new HeaderFooter(
                gsp.getChckbxHeader().isSelected(),
                gsp.getChckbxHeaderLine().isSelected(),
                gsp.getChckbxFooter().isSelected(),
                gsp.getChckbxFooterLine().isSelected(), positionPageCount);
        hfContent.setLineInHeader(gsp.getTxtWebsite().getText());
        hfContent.setLineInFooter(gsp.getTxtBottomLeftText().getText());
        hfContent.setLogoInHeader(gsp.getTxtLogo().getText());

        // Columns choice settings

        List<String> labels = new ArrayList<>();
        labels.add("Placement path");
        labels.add("Impressions");
        labels.add("Unique cookies");
        labels.add("Frequency");
        labels.add("Clicks");
        labels.add("Clicking users");
        labels.add("Click Through Rate");
        labels.add("Unique CTR");
        labels.add("Reach");

        if (msp.getChckbxSummary().isSelected()) {
            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.CEILING);
            SimpleDateFormat hungarianF = new SimpleDateFormat(
                    msp.getSelectedLanguage().getDateFormat());

            summary.add(new SummaryData("campaign name:",
                    c1.getCampaignHeader().getCampaignName()));
            summary.add(new SummaryData("client name:",
                    c1.getCampaignHeader().getClientName()));
            summary.add(new SummaryData("campaign start date:",
                    hungarianF.format(c1.getCampaignHeader().getStartDate())));
            summary.add(new SummaryData("campaign end date:",
                    hungarianF.format(c1.getCampaignHeader().getEndDate())));
            if (csp.getChckbxImpressionsSummary().isSelected())
                summary.add(new SummaryData("impressions:",
                        CampaignRow.getSpacesBetweenThousands(
                                String.valueOf(c1.getAll().getImpressions()))));
            if (csp.getChckbxReachSummary().isSelected())
                summary.add(new SummaryData("reach:",
                        CampaignRow.getSpacesBetweenThousands(
                                String.valueOf(c1.getAll().getReach()))));
            if (csp.getChckbxFrequencySummary().isSelected())
                summary.add(new SummaryData("frequency:",
                        df.format(c1.getAll().getFrequency())));
            if (csp.getChckbxClicksSummary().isSelected())
                summary.add(new SummaryData("clicks:",
                        CampaignRow.getSpacesBetweenThousands(
                                String.valueOf(c1.getAll().getClicks()))));
            if (csp.getChckbxClickingUsersSummary().isSelected())
                summary.add(new SummaryData("clicking users:",
                        CampaignRow.getSpacesBetweenThousands(String
                                .valueOf(c1.getAll().getClickingUsers()))));
            if (csp.getChckbxClickThroughRateSummary().isSelected())
                summary.add(new SummaryData("click through rate:",
                        c1.getAll().getClickThroughRate().toString()));
            if (csp.getChckbxUniqueCTRSummary().isSelected())
                summary.add(new SummaryData("unique CTR:",
                        c1.getAll().getUniqueCTR().toString()));
        }

        SummaryPage summaryPage = new SummaryPage(summary,
                msp.getChckbxSummary().isSelected());

        summaryPage.setStructure(hfContent);
        sections.add(summaryPage);
        pbw.setValue(80);

        if (msp.getChckbxTimePeriodTotal().isSelected()) {
            PeriodTotalPage ptp = new PeriodTotalPage();
            ptp.setClickingUsers(
                    csp.getChckbxClickingUsersRankings().isSelected());
            ptp.setClicks(csp.getChckbxClicksRankings().isSelected());
            ptp.setClickThroughRate(
                    csp.getChckbxClickThroughRateRankings().isSelected());
            ptp.setFrequency(csp.getChckbxFrequencyRankings().isSelected());
            ptp.setImpressions(csp.getChckbxImpressionsRankings().isSelected());
            ptp.setReach(csp.getChckbxReach().isSelected());
            ptp.setUniqueCTR(csp.getChckbxUniqueCTRRankings().isSelected());

            ptp.setStructure(hfContent);
            if (msp.getChckbxWholeTotal().isSelected())
                ptp.setAll(session.getAll(campaignID));
            ptp.setMonthlyData(monthlyData);
            ptp.setWeeklyData(weeklyData);
            sections.add(ptp);
        }

        if (msp.detectRankings()) {
            ContentPage contentPage = new ContentPage(
                    csp.getChckbxImpressionsRankings().isSelected(),
                    csp.getChckbxFrequencyRankings().isSelected(),
                    csp.getChckbxClicksRankings().isSelected(),
                    csp.getChckbxClickingUsersRankings().isSelected(),
                    csp.getChckbxClickThroughRateRankings().isSelected(),
                    csp.getChckbxUniqueCTRRankings().isSelected());

            contentPage.setReach(csp.getChckbxReach().isSelected());
            contentPage.setChartType(ContentPage.BAR_CHART);
            contentPage.setStructure(hfContent);
            c1.setColumsLabels(labels);
            c1.setTitle("Rankings");
            contentPage.setCampaign(c1);
            contentPage.setMonthly(msp.getChckbxMonthlyRankings().isSelected());
            contentPage.setWeekly(msp.getChckbxWeeklyRankings().isSelected());
            contentPage.setGeneral(msp.getChckbxRankings().isSelected());
            sections.add(contentPage);
        }

        if (msp.detectCreative()) {
            ContentPage contentPage3 = new ContentPage(
                    csp.getChckbxImpressionsCreative().isSelected(),
                    csp.getChckbxFrequencyCreative().isSelected(),
                    csp.getChckbxClicksCreative().isSelected(),
                    csp.getChckbxClickingUsersCreative().isSelected(),
                    csp.getChckbxClickThroughRateCreative().isSelected(),
                    csp.getChckbxUniqueCTRCreative().isSelected());

            contentPage3.setReach(csp.getChckbxReachCreative().isSelected());
            contentPage3.setStructure(hfContent);
            c3.setColumsLabels(labels);
            c3.setTitle("Creative");
            contentPage3.setCampaign(c3);
            contentPage3.setGeneral(msp.getChckbxCreative().isSelected());
            contentPage3
                    .setMonthly(msp.getChckbxMonthlyCreative().isSelected());
            contentPage3.setChartType(ContentPage.BAR_CHART);
            contentPage3.setWeekly(msp.getChckbxWeeklyCreative().isSelected());
            sections.add(contentPage3);

        }

        if (msp.getChckbxTechnical().isSelected()) {
            ContentPage contentPage2 = new ContentPage(
                    csp.getChckbxImpressionsTechnical().isSelected(),
                    csp.getChckbxFrequencyTechnical().isSelected(),
                    csp.getChckbxClicksTechnical().isSelected(),
                    csp.getChckbxClickingUsersTechnical().isSelected(),
                    csp.getChckbxClickThroughRateTechnical().isSelected(),
                    csp.getChckbxUniqueCTRTechnical().isSelected());

            contentPage2.setReach(csp.getChckbxReachTechnical().isSelected());
            contentPage2.setChartType(ContentPage.PIE_CHART);
            contentPage2.setStructure(hfContent);
            contentPage2.setGeneral(msp.getChckbxTechnical().isSelected());
            c2.setColumsLabels(labels);
            c2.setTitle("Technical");
            contentPage2.setCampaign(c2);
            sections.add(contentPage2);

        }

        pbw.setValue(85);
        try {
            etpd.createPdfDownload(
                    Utils.getPdfName(
                            commonInfos.getCampaignHeader().getCampaignName()),
                    sections, isp.getRdbtnOn().isSelected(),
                    msp.getChckbxTimePeriodTotal().isSelected());
            pbw.setValue(100);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

    }

    private void reloadPanels() {

        if (panels.size() == 0) {
            getContentPane().remove(panels.get(0));
            showFirstPanel();
            return;
        }

        LinkedList<SettingsChoicePanel> panels2 = new LinkedList<SettingsChoicePanel>();
        for (SettingsChoicePanel settingsChoicePanel : panels) {
            panels2.add(settingsChoicePanel.getNewInstance());
        }

        getContentPane().remove(panels.get(currentPanel));
        panels = panels2;
        currentPanel = 0;
        getContentPane().add(panels.get(currentPanel));
    }

    public void createPanelsExcel() {

        getContentPane().remove(panels.get(currentPanel));

        panels = new LinkedList<SettingsChoicePanel>();
        panels.add(new ExcelChoicePanel());
        panels.add(new GeneralSettingsPanel(false));
        panels.add(new TitleSettingsPanel());
        panels.add(new InsertPageSettingsPanel());
        panels.add(new ColumnsSettingsPanel(false));

        currentPanel = 0;

        np = new NavigationPanel(this);

        getContentPane().add(np);

        getContentPane().add(panels.get(0));
        repaint();
        revalidate();

    }

    public void createPanelsInternet() {

        getContentPane().remove(panels.get(currentPanel));

        panels = new LinkedList<SettingsChoicePanel>();

        panels.add(new LoginPanel(this));
        panels.add(new CampaignChoicePanel());
        panels.add(new ModulesSettingsPanel());
        panels.add(new GeneralSettingsPanel(true));
        panels.add(new TitleSettingsPanel());
        panels.add(new InsertPageSettingsPanel());
        panels.add(new ColumnsSettingsPanel(true));

        currentPanel = 0;

        np = new NavigationPanel(this);

        getContentPane().add(np);

        getContentPane().add(panels.get(0));
        repaint();
        revalidate();

    }

    @Override
    public void nextPanel() {
        if (panels.get(currentPanel).isEveryThingOk()) {

            np.showPreviousButton();
            showNextPanel();
            if (currentPanel == panels.size() - 1) {
                np.showValidateButton();

            }

        }
        // else
        // System.out.println(m.getMessages());

    }

    @Override
    public void previousPanel() {
        np.showNextButton();

        if (currentPanel > 0) {
            showPreviousPanel();
            np.showPreviousButton();
        }

        else if (currentPanel == 0) {
            getContentPane().remove(panels.get(currentPanel));
            getContentPane().remove(np);
            showFirstPanel();
            repaint();
            revalidate();
        }

    }

    private void showFirstPanel() {
        panels = new LinkedList<SettingsChoicePanel>();
        MainMenuPanel mmp = new MainMenuPanel(this);
        panels.add(mmp);

        getContentPane().add(panels.get(0));

        currentPanel = 0;
    }

}
