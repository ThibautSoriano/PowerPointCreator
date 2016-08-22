package main.java.exceltopdf;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.JFreeChart;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import main.java.datasdownloading.entities.PeriodData;
import main.java.datasdownloading.entities.TocElement;
import main.java.excelreader.entities.CampaignRow;
import main.java.exceltopdf.pdfsections.ContentPage;
import main.java.exceltopdf.pdfsections.InsertPage;
import main.java.exceltopdf.pdfsections.PeriodTotalPage;
import main.java.exceltopdf.pdfsections.Section;
import main.java.exceltopdf.pdfsections.SummaryPage;
import main.java.exceltopdf.pdfsections.TableOfContent;
import main.java.exceltopdf.pdfsections.TitlePage;
import main.java.utils.Utils;

public class ExcelToPdf {

    public static final String TEMP_INSERT_PAGE = "tmp_insert_page.pdf";
    public static final String TEMP_TITLE_PAGE = "tmp_title_page.pdf";
    public static final String TEMP_SUMMARY_PAGE = "tmp_summary_page.pdf";
    public static final String TEMP_PERIOD_TOTAL_PAGE = "tmp_period_total_page.pdf";
    public static final String TEMP_CONTENT_PAGE = "tmp_content_page.pdf";
    public static final String TEMP_TOC = "tmp_toc.pdf";
    public static final String TEMP_TOC_DOC = "tmp_toc_doc.pdf";
    public List<String> FILES = new ArrayList<>();
    private static final int TITLE_PAGE = 0;
    private static final int INSERT_PAGE = 1;
    private static final int SUMMARY_PAGE = 2;
    private static final int PERIOD_TOTAL_PAGE = 3;
	
    public static int CURRENT_PAGE_NUMBER = 0;
    private int sectionNumber = 1;

    private String encoding = "";

    private boolean wholeTotal;
    private boolean timePeriodTotal;
    private boolean tocStart;
    private PeriodData monthlyData;
    private PeriodData weeklyData;
    private String dateFormat;
    
//    private Map<String, PdfReader> filesToMerge = new HashMap<String, PdfReader>();
    private List<TocElement> filesToMerge = new ArrayList<>();
    private List<String> filesToDelete = new ArrayList<>();
    private List<String> filesNotInTOC = new ArrayList<>();

    public PeriodData getMonthlyData() {
        return monthlyData;
    }

    public void setMonthlyData(PeriodData monthlyData) {
        this.monthlyData = monthlyData;
    }

    public PeriodData getWeeklyData() {
        return weeklyData;
    }

    public void setWeeklyData(PeriodData weeklyData) {
        this.weeklyData = weeklyData;
    }

    public ExcelToPdf(String encoding, boolean wholeTotal,
            boolean timePeriodTotal, String dateFormat, boolean tocStart) {
        this.encoding = encoding;
        this.wholeTotal = wholeTotal;
        this.timePeriodTotal = timePeriodTotal;
        this.dateFormat = dateFormat;
        this.tocStart = tocStart;
    }

    public void createPdf(String dest, List<Section> sections,
            boolean insertPageOn) throws IOException, DocumentException {

        TitlePage titlePage = (TitlePage) sections.get(TITLE_PAGE);

        createTitlePage(titlePage);

        if (insertPageOn) {
            InsertPage insertPage = (InsertPage) sections.get(INSERT_PAGE);

            createInsertPage(insertPage);
        }

        for (int i = 2; i < sections.size(); i++) {
            createContentPage((ContentPage) sections.get(i), false);
        }

        PdfConcat c = new PdfConcat();
        c.concat(FILES, dest);
    }

    public void createPdfDownload(String dest, List<Section> sections,
            boolean insertPageOn, boolean periodTotal) throws DocumentException, IOException {
    	
    	int startIndex = 3;
        TitlePage titlePage = (TitlePage) sections.get(TITLE_PAGE);

        createTitlePage(titlePage);

        if (insertPageOn) {
            InsertPage insertPage = (InsertPage) sections.get(INSERT_PAGE);

            createInsertPage(insertPage);
        }

        SummaryPage summaryPage = (SummaryPage) sections.get(SUMMARY_PAGE);

        if (summaryPage.isHasToBeDisplayed()) {
            createSummaryPage(summaryPage);
        }

        if (periodTotal) {
        	PeriodTotalPage ptPage = (PeriodTotalPage) sections
                .get(PERIOD_TOTAL_PAGE);
        	startIndex++;
        	createPeriodTotalPage(ptPage);
        }
        

        
		for (int i = startIndex; i < sections.size(); i++) {
            createContentPage((ContentPage) sections.get(i), true);
        }
		
		createTOC();
		
		TableOfContent toc = new TableOfContent();
		
		toc.createPdf(dest, TEMP_TOC, filesToMerge, filesToDelete, filesNotInTOC, tocStart);
//		FILES.add(TEMP_TOC_DOC);
//        PdfConcat c = new PdfConcat();
//        c.concat(FILES, dest);
    }

    private void createPeriodTotalPage(PeriodTotalPage ptPage)
            throws DocumentException, IOException {
        Document document = new Document();
        TabCreator tc = new TabCreator(wholeTotal);
        document.setMargins(85, 85, 85, 113);
        String fileName = TEMP_PERIOD_TOTAL_PAGE;
        FileOutputStream outputStream = new FileOutputStream(fileName);
//        FILES.add(fileName);
        
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        writer.setPageEvent(ptPage.getStructure());

        document.open();
        boolean[] colsToPrint = { true, false, ptPage.isImpressions(), false,
                ptPage.isFrequency(), ptPage.isClicks(),
                ptPage.isClickingUsers(), ptPage.isClickThroughRate(),
                ptPage.isUniqueCTR(), ptPage.isReach() };

        if (ptPage.getMonthlyData() != null) {
        	Paragraph title = new Paragraph(sectionNumber + ". Monthly Sums", new Font(FontFamily.HELVETICA, 16, Font.UNDERLINE));
			sectionNumber++;
			title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n\n"));

            document.add(tc.createTabPeriodTotal(
                    ptPage.getMonthlyData().getContent(),
                    ptPage.getMonthlyData().getColumsLabels(), ptPage.getAll(),
                    colsToPrint, true, false, dateFormat));
            document.add(new Paragraph("\n\n"));
        }

        if (ptPage.getWeeklyData() != null) {
        	Paragraph title = new Paragraph(sectionNumber + ". Weekly Sums", new Font(FontFamily.HELVETICA, 16, Font.UNDERLINE));
			sectionNumber++;
			title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n\n"));

            document.add(tc.createTabPeriodTotal(
                    ptPage.getWeeklyData().getContent(),
                    ptPage.getWeeklyData().getColumsLabels(), ptPage.getAll(),
                    colsToPrint, true, true, dateFormat));

        }
        document.close();
        CURRENT_PAGE_NUMBER += writer.getPageNumber();
        writer.flush();
        writer.close();
        outputStream.flush();
        outputStream.close();

        writer = null;
        outputStream = null;
        System.gc();
        filesToMerge.add(new TocElement("Period sums", TEMP_PERIOD_TOTAL_PAGE));
        filesToDelete.add(TEMP_PERIOD_TOTAL_PAGE);

    }

    private void createSummaryPage(SummaryPage summaryPage)
            throws DocumentException, IOException {
        Document document = new Document();
        TabCreator tc = new TabCreator(wholeTotal);
        document.setMargins(85, 85, 85, 113);
        String fileName = TEMP_SUMMARY_PAGE;
        FileOutputStream outputStream = new FileOutputStream(fileName);
//        FILES.add(fileName);
        
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        writer.setPageEvent(summaryPage.getStructure());

        document.open();
        
        Paragraph title = new Paragraph(sectionNumber + ". Summary", new Font(FontFamily.HELVETICA, 16, Font.UNDERLINE));
		sectionNumber++;
		title.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(title);
        document.add(new Paragraph("\n\n"));
        document.add(tc.getTabSummary(summaryPage.getSummary()));

        document.close();
        CURRENT_PAGE_NUMBER += writer.getPageNumber();
        writer.flush();
        writer.close();
        outputStream.flush();
        outputStream.close();

        writer = null;
        outputStream = null;
        System.gc();
        filesToMerge.add(new TocElement("Summary", TEMP_SUMMARY_PAGE));
        filesToDelete.add(TEMP_SUMMARY_PAGE);
    }

    private void createContentPage(ContentPage contentPage, boolean download)
            throws DocumentException, IOException {
        BarChartCreator barChartCreator = new BarChartCreator();
        PieChartCreator pieChartCreator = new PieChartCreator();
        Document document = new Document();
        document.setMargins(85, 85, 85, 113);
        String fileName = Utils.getNewTmpFileName() + TEMP_CONTENT_PAGE;
        FileOutputStream outputStream = new FileOutputStream(fileName);
        FILES.add(fileName);
        
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        writer.setPageEvent(contentPage.getStructure());
        List<CampaignRow> rows = null;
        document.open();
        Paragraph title = new Paragraph(sectionNumber + ". " + contentPage.getCampaign().getTitle() + " charts", new Font(FontFamily.HELVETICA, 16, Font.UNDERLINE));
		sectionNumber++;
		title.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(title);

        rows = contentPage.getCampaign().getCampaignContent();

        if (contentPage.getChartType() == ContentPage.BAR_CHART) {
            if (contentPage.isImpressions()) {
                JFreeChart impressionsChart = barChartCreator.getChart(
                        CampaignRow.sortBy(rows, CampaignRow.IMPRESSIONS_INDEX),
                        CampaignRow.IMPRESSIONS_INDEX, "Impressions", "Ads");
                if (impressionsChart != null)
                    document.add(getImageBar(impressionsChart, writer));
            }
            if (contentPage.isUniqueCookies()) {
                JFreeChart uniqueCookiesChart = barChartCreator.getChart(
                        CampaignRow.sortBy(rows,
                                CampaignRow.UNIQUE_COOKIES_INDEX),
                        CampaignRow.UNIQUE_COOKIES_INDEX, "Unique cookies",
                        "Ads");
                if (uniqueCookiesChart != null)
                    document.add(getImageBar(uniqueCookiesChart, writer));
            }
            if (contentPage.isReach()) {
                JFreeChart reachChart = barChartCreator.getChart(
                        CampaignRow.sortBy(rows, CampaignRow.REACH_INDEX),
                        CampaignRow.REACH_INDEX, "Reach", "Ads");
                if (reachChart != null)
                    document.add(getImageBar(reachChart, writer));
            }
            if (contentPage.isFrequency()) {
                JFreeChart frequencyChart = barChartCreator.getChart(
                        CampaignRow.sortBy(rows, CampaignRow.FREQUENCY_INDEX),
                        CampaignRow.FREQUENCY_INDEX, "Frequency", "Ads");
                if (frequencyChart != null)
                    document.add(getImageBar(frequencyChart, writer));
            }
            if (contentPage.isClicks()) {
                JFreeChart clicksChart = barChartCreator.getChart(
                        CampaignRow.sortBy(rows, CampaignRow.CLICKS_INDEX),
                        CampaignRow.CLICKS_INDEX, "Clicks", "Ads");
                if (clicksChart != null)
                    document.add(getImageBar(clicksChart, writer));
            }
            if (contentPage.isClickingUsers()) {
                JFreeChart clickingUsersChart = barChartCreator.getChart(
                        CampaignRow.sortBy(rows,
                                CampaignRow.CLICKING_USERS_INDEX),
                        CampaignRow.CLICKING_USERS_INDEX, "Clicking users",
                        "Ads");
                if (clickingUsersChart != null)
                    document.add(getImageBar(clickingUsersChart, writer));
            }
            if (contentPage.isClickThroughRate()) {
                JFreeChart clickThroughRateChart = barChartCreator.getChart(
                        CampaignRow.sortBy(rows,
                                CampaignRow.CLICK_THROUGH_RATE_INDEX),
                        CampaignRow.CLICK_THROUGH_RATE_INDEX,
                        "Click through rate", "Ads");
                if (clickThroughRateChart != null)
                    document.add(getImageBar(clickThroughRateChart, writer));
            }
            if (contentPage.isUniqueCTR()) {
                JFreeChart uniqueCTRChart = barChartCreator.getChart(
                        CampaignRow.sortBy(rows, CampaignRow.UNIQUE_CTR_INDEX),
                        CampaignRow.UNIQUE_CTR_INDEX, "Unique CTR", "Ads");
                if (uniqueCTRChart != null)
                    document.add(getImageBar(uniqueCTRChart, writer));
            }
        } else if (contentPage.getChartType() == ContentPage.PIE_CHART) {
            if (contentPage.isImpressions()) {
                JFreeChart impressionsChart = pieChartCreator.getChart(
                        CampaignRow.sortBy(rows, CampaignRow.IMPRESSIONS_INDEX),
                        CampaignRow.IMPRESSIONS_INDEX, "Impressions per county",
                        false, 0, 0);
                if (impressionsChart != null)
                    document.add(getImagePie(impressionsChart, writer));
                document.add(new Paragraph("\n"));
            }
            if (contentPage.isUniqueCookies()) {
                JFreeChart uniqueCookiesChart = pieChartCreator.getChart(
                        CampaignRow.sortBy(rows,
                                CampaignRow.UNIQUE_COOKIES_INDEX),
                        CampaignRow.UNIQUE_COOKIES_INDEX,
                        "Unique cookies per county", false, 0, 0);
                if (uniqueCookiesChart != null)
                    document.add(getImagePie(uniqueCookiesChart, writer));
                document.add(new Paragraph("\n"));
            }
            if (contentPage.isReach()) {
                JFreeChart reachChart = pieChartCreator.getChart(
                        CampaignRow.sortBy(rows, CampaignRow.REACH_INDEX),
                        CampaignRow.REACH_INDEX, "Reach per county", false, 0,
                        0);
                if (reachChart != null)
                    document.add(getImagePie(reachChart, writer));
                document.add(new Paragraph("\n"));
            }
            if (contentPage.isFrequency()) {
                int indexDenominator = CampaignRow.UNIQUE_COOKIES_INDEX;
                if (download) {
                    indexDenominator = CampaignRow.REACH_INDEX;
                }
                JFreeChart frequencyChart = pieChartCreator.getChart(
                        CampaignRow.sortBy(rows, CampaignRow.FREQUENCY_INDEX),
                        CampaignRow.FREQUENCY_INDEX, "Frequency per county",
                        true, CampaignRow.IMPRESSIONS_INDEX, indexDenominator);
                if (frequencyChart != null)
                    document.add(getImagePie(frequencyChart, writer));
                document.add(new Paragraph("\n"));
            }
            if (contentPage.isClicks()) {
                JFreeChart clicksChart = pieChartCreator.getChart(
                        CampaignRow.sortBy(rows, CampaignRow.CLICKS_INDEX),
                        CampaignRow.CLICKS_INDEX, "Clicks per county", false, 0,
                        0);
                if (clicksChart != null)
                    document.add(getImagePie(clicksChart, writer));
                document.add(new Paragraph("\n"));
            }
            if (contentPage.isClickingUsers()) {
                JFreeChart clickingUsersChart = pieChartCreator.getChart(
                        CampaignRow.sortBy(rows,
                                CampaignRow.CLICKING_USERS_INDEX),
                        CampaignRow.CLICKING_USERS_INDEX,
                        "Clicking users per county", false, 0, 0);
                if (clickingUsersChart != null)
                    document.add(getImagePie(clickingUsersChart, writer));
                document.add(new Paragraph("\n"));
            }
            if (contentPage.isClickThroughRate()) {
                JFreeChart clickThroughRateChart = pieChartCreator.getChart(
                        CampaignRow.sortBy(rows,
                                CampaignRow.CLICK_THROUGH_RATE_INDEX),
                        CampaignRow.CLICK_THROUGH_RATE_INDEX,
                        "Click through rate per county", true,
                        CampaignRow.CLICKS_INDEX,
                        CampaignRow.IMPRESSIONS_INDEX);
                if (clickThroughRateChart != null)
                    document.add(getImagePie(clickThroughRateChart, writer));
                document.add(new Paragraph("\n"));
            }
            if (contentPage.isUniqueCTR()) {
                int indexDenominator = CampaignRow.UNIQUE_COOKIES_INDEX;
                if (download) {
                    indexDenominator = CampaignRow.REACH_INDEX;
                }
                JFreeChart uniqueCTRChart = pieChartCreator.getChart(
                        CampaignRow.sortBy(rows, CampaignRow.UNIQUE_CTR_INDEX),
                        CampaignRow.UNIQUE_CTR_INDEX, "Unique CTR per county",
                        true, CampaignRow.CLICKING_USERS_INDEX,
                        indexDenominator);
                if (uniqueCTRChart != null)
                    document.add(getImagePie(uniqueCTRChart, writer));
                document.add(new Paragraph("\n"));
            }
        }

        document.close();
        CURRENT_PAGE_NUMBER += writer.getPageNumber();
        writer.flush();
        writer.close();
        outputStream.flush();
        outputStream.close();
        
        writer = null;
        outputStream = null;
        System.gc();
        
        filesToMerge.add(new TocElement(contentPage.getCampaign().getTitle() + " charts", fileName));
        filesToDelete.add(fileName);
		TabCreator tc = new TabCreator(wholeTotal);
		
		
		if (contentPage.isGeneral() || !download) {
			Document docGeneral = new Document();
			docGeneral.setMargins(85, 85, 85, 113);
	    	String fileNameGeneral = Utils.getNewTmpFileName() + TEMP_CONTENT_PAGE;
			FileOutputStream osGeneral = new FileOutputStream(fileNameGeneral);
			FILES.add(fileNameGeneral);
			
			PdfWriter writerG = PdfWriter.getInstance(docGeneral, osGeneral);
			writerG.setPageEvent(contentPage.getStructure());
			docGeneral.open();
			Paragraph p = new Paragraph(sectionNumber + ". " + contentPage.getCampaign().getTitle() + " general data", new Font(FontFamily.HELVETICA, 16, Font.UNDERLINE));
			sectionNumber++;
			p.setAlignment(Paragraph.ALIGN_CENTER);
			docGeneral.add(p);
			docGeneral.add(new Paragraph("\n"));
			List<String> labels;
			
			labels = contentPage.getCampaign().getColumsLabels();
			
	    	boolean [] colsToPrint = {
	    			true, contentPage.isImpressions(), contentPage.isUniqueCookies(), contentPage.isFrequency(),
	                contentPage.isClicks(), contentPage.isClickingUsers(), contentPage.isClickThroughRate(),
	                contentPage.isUniqueCTR(), contentPage.isReach()
	        };
			
	    	docGeneral.add(tc.createTabCampaign(rows, labels, contentPage.getCampaign().getAll(),colsToPrint,true));
			
	    	docGeneral.close();
	        CURRENT_PAGE_NUMBER += writerG.getPageNumber();
	        writerG.flush();
	        writerG.close();
	        osGeneral.flush();
	        osGeneral.close();
	        
	        writerG = null;
	        osGeneral = null;
	        System.gc();
	        filesToMerge.add(new TocElement(contentPage.getCampaign().getTitle() + " general data", fileNameGeneral));
	        filesToDelete.add(fileNameGeneral);
		}
        
        
        boolean [] colsPeriod = {
    			true, true, contentPage.isImpressions(), contentPage.isUniqueCookies(), contentPage.isFrequency(),
                contentPage.isClicks(), contentPage.isClickingUsers(), contentPage.isClickThroughRate(),
                contentPage.isUniqueCTR(), contentPage.isReach()
        };
        
        if (contentPage.isWeekly() && download) {
        	Document docWeekly = new Document();
        	docWeekly.setMargins(85, 85, 85, 113);
	    	String nameWeekly = Utils.getNewTmpFileName() + TEMP_CONTENT_PAGE;
			FileOutputStream osWeekly = new FileOutputStream(nameWeekly);
//			FILES.add(nameWeekly);
			
			PdfWriter writerW = PdfWriter.getInstance(docWeekly, osWeekly);
			writerW.setPageEvent(contentPage.getStructure());
			docWeekly.open();
			Paragraph p = new Paragraph(sectionNumber + ". " + contentPage.getCampaign().getTitle() + " weekly data", new Font(FontFamily.HELVETICA, 16, Font.UNDERLINE));
			sectionNumber++;
			p.setAlignment(Paragraph.ALIGN_CENTER);
			docWeekly.add(p);
			docWeekly.add(new Paragraph("\n"));
			docWeekly.add(tc.createTabPeriod(contentPage.getCampaign().getWeeklyData().getContent(), contentPage.getCampaign().getWeeklyData().getColumsLabels(), contentPage.getCampaign().getWeeklyData().getAll(), colsPeriod, true, timePeriodTotal, weeklyData.getContent(),true,dateFormat));
        	
        	docWeekly.close();
	        CURRENT_PAGE_NUMBER += writerW.getPageNumber();
	        writerW.flush();
	        writerW.close();
	        osWeekly.flush();
	        osWeekly.close();
	        
	        writerW = null;
	        osWeekly = null;
	        System.gc();
	        filesToMerge.add(new TocElement(contentPage.getCampaign().getTitle() + " weekly data", nameWeekly));
	        filesToDelete.add(nameWeekly);
        }
        
        if (contentPage.isMonthly() && download) {
        	Document docMonthly = new Document();
        	docMonthly.setMargins(85, 85, 85, 113);
	    	String nameMonthly = Utils.getNewTmpFileName() + TEMP_CONTENT_PAGE;
			FileOutputStream osMonthly = new FileOutputStream(nameMonthly);
//			FILES.add(nameMonthly);
			
			PdfWriter writerM = PdfWriter.getInstance(docMonthly, osMonthly);
			writerM.setPageEvent(contentPage.getStructure());
			docMonthly.open();
			Paragraph p = new Paragraph(sectionNumber + ". " + contentPage.getCampaign().getTitle() + " monthly data", new Font(FontFamily.HELVETICA, 16, Font.UNDERLINE));
			sectionNumber++;
			p.setAlignment(Paragraph.ALIGN_CENTER);
			docMonthly.add(p);
			docMonthly.add(new Paragraph("\n"));
			docMonthly.add(tc.createTabPeriod(contentPage.getCampaign().getMonthlyData().getContent(), contentPage.getCampaign().getMonthlyData().getColumsLabels(), contentPage.getCampaign().getMonthlyData().getAll(), colsPeriod, true, timePeriodTotal, monthlyData.getContent(),false,dateFormat));
        
			docMonthly.close();
	        CURRENT_PAGE_NUMBER += writerM.getPageNumber();
	        writerM.flush();
	        writerM.close();
	        osMonthly.flush();
	        osMonthly.close();
	        
	        writerM = null;
	        osMonthly = null;
	        System.gc();
	        filesToMerge.add(new TocElement(contentPage.getCampaign().getTitle() + " monthly data", nameMonthly));
	        filesToDelete.add(nameMonthly);
        }

    }

    public void createInsertPage(InsertPage insertPage)
            throws DocumentException, IOException {
        Document document = new Document();
        document.setMargins(85, 85, 85, 113);
        FileOutputStream outputStream = new FileOutputStream(TEMP_INSERT_PAGE);
        FILES.add(TEMP_INSERT_PAGE);
        filesNotInTOC.add(TEMP_INSERT_PAGE);
        filesToDelete.add(TEMP_INSERT_PAGE);
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);

        writer.setPageEvent(insertPage.getStructure());

        document.open();

        // custom text area
        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, encoding,
                BaseFont.EMBEDDED);
        PdfContentByte cb = writer.getDirectContent();
        String customArea = insertPage.getCustomTextArea();
        Paragraph custom = new Paragraph(customArea);
        custom.setAlignment(Element.ALIGN_CENTER);

        Chunk toMeasureCustom = new Chunk(customArea);

        float xPositionCustom = (PageSize.A4.getWidth()
                - toMeasureCustom.getWidthPoint()) / 2;
        cb.saveState();
        cb.beginText();
        cb.moveText(xPositionCustom, 320);
        cb.setFontAndSize(bf, 12);
        cb.showText(customArea);
        cb.endText();
        cb.restoreState();

        cb.closePathEoFillStroke();

        document.close();

        writer.flush();
        writer.close();
        outputStream.flush();
        outputStream.close();

        writer = null;
        outputStream = null;
        System.gc();
    }

    public void createTitlePage(TitlePage titlePage)
            throws DocumentException, IOException {
        Document document = new Document();
        document.setMargins(85, 85, 85, 113);
        
        FileOutputStream outputStream = new FileOutputStream(TEMP_TITLE_PAGE);
        FILES.add(TEMP_TITLE_PAGE);
        filesNotInTOC.add(TEMP_TITLE_PAGE);
        filesToDelete.add(TEMP_TITLE_PAGE);
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);

        writer.setPageEvent(titlePage.getStructure());
        document.open();

        // campaignName
        Paragraph campaignName = new Paragraph(titlePage.getCampaignName());
        campaignName.setAlignment(Element.ALIGN_CENTER);
        

        PdfContentByte cb = writer.getDirectContent();
        BaseFont bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, encoding,
                BaseFont.EMBEDDED);
        Chunk toMeasureSize = new Chunk(titlePage.getCampaignName());
        Font fontTitle = new Font();
        fontTitle.setStyle(Font.BOLD);
        fontTitle.setSize(16);
        toMeasureSize.setFont(fontTitle);

        float xPosition = (PageSize.A4.getWidth()
                - toMeasureSize.getWidthPoint()) / 2;
        cb.saveState();
        cb.beginText();
        cb.moveText(xPosition, 490);
        cb.setFontAndSize(bfBold, 16);
        cb.showText(titlePage.getCampaignName());
        cb.endText();
        cb.restoreState();

        // dates
        String datesString = titlePage.getDate();
        Paragraph dates = new Paragraph(datesString);
        dates.setAlignment(Element.ALIGN_CENTER);

        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, encoding,
                BaseFont.EMBEDDED);
        Chunk toMeasureDates = new Chunk(datesString);

        float xPositionDates = (PageSize.A4.getWidth()
                - toMeasureDates.getWidthPoint()) / 2;
        cb.saveState();
        cb.beginText();
        cb.moveText(xPositionDates, 460);
        cb.setFontAndSize(bf, 12);
        cb.showText(datesString);
        cb.endText();
        cb.restoreState();

        // custom text area
        String customArea = titlePage.getBelowTitle();
        Paragraph custom = new Paragraph(customArea);
        custom.setAlignment(Element.ALIGN_CENTER);

        Chunk toMeasureCustom = new Chunk(customArea);

        float xPositionCustom = (PageSize.A4.getWidth()
                - toMeasureCustom.getWidthPoint()) / 2;
        cb.saveState();
        cb.beginText();
        cb.moveText(xPositionCustom, 430);
        cb.setFontAndSize(bf, 12);
        cb.showText(customArea);
        cb.endText();
        cb.restoreState();

        cb.closePathEoFillStroke();

        document.close();

        writer.flush();
        writer.close();
        outputStream.flush();
        outputStream.close();

        writer = null;
        outputStream = null;
        System.gc();
    }

    public Image getImagePie(JFreeChart chart, PdfWriter writer)
            throws BadElementException, IOException {
        int width = 400;
        int height = 400;
        BufferedImage bufferedImage = chart.createBufferedImage(width, height);

        Image image = Image.getInstance(writer, bufferedImage, 1.0f);
        image.scalePercent(70);
        image.setAlignment(Image.MIDDLE);

        return image;
    }

    public Image getImageBar(JFreeChart chart, PdfWriter writer)
            throws BadElementException, IOException {
        int width = 570;
        int height = 430;
        BufferedImage bufferedImage = chart.createBufferedImage(width, height);

        Image image = Image.getInstance(writer, bufferedImage, 1.0f);
        image.scalePercent(70);
        image.setAlignment(Image.MIDDLE);

        return image;
    }

    
    public void createTOC() throws DocumentException, IOException {

    	Document document = new Document();
        document.setMargins(85, 85, 85, 113);
        FileOutputStream outputStream = new FileOutputStream(TEMP_TOC);
//        FILES.add(TEMP_TOC);
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        
        document.open();
        
        Paragraph p = new Paragraph("Table of content", new Font(FontFamily.HELVETICA, 16, Font.UNDERLINE));
		p.setAlignment(Paragraph.ALIGN_CENTER);
		
		document.add(p);

        document.close();

        writer.flush();
        writer.close();
        outputStream.flush();
        outputStream.close();

        writer = null;
        outputStream = null;
        System.gc();
    }
}
