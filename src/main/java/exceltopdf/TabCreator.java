package main.java.exceltopdf;

import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import main.java.datasdownloading.entities.SummaryData;
import main.java.excelreader.entities.CampaignRow;
import main.java.excelreader.entities.CampaignRowPeriod;
import main.java.utils.Utils;

public class TabCreator {

    private BaseColor headerColor;
    private BaseColor lastLineColor;
    private BaseColor bestRowsColor;
    private BaseColor worstRowsColor;
    private BaseColor interSumsColor;
    private boolean wholeTotal;
    // private boolean timePeriodTotal;

    public TabCreator(boolean wholeTotal) {// , boolean timePeriodTotal) {
        super();
        this.wholeTotal = wholeTotal;
        // this.timePeriodTotal = timePeriodTotal;
        headerColor = new BaseColor(7, 167, 227);
        lastLineColor = new BaseColor(7, 167, 227);
        bestRowsColor = new BaseColor(255, 255, 255);
        worstRowsColor = new BaseColor(190, 190, 190);
        interSumsColor = new BaseColor(116, 208, 241);

    }

    public PdfPTable createTabCampaign(List<CampaignRow> campaignRows,
            List<String> headers, CampaignRow all, boolean[] colsToPrint,
            boolean hideEmptyLines) {

        CampaignRow.sortBy(campaignRows, getIndexFromColsToPrint(colsToPrint));

        if (colsToPrint.length < CampaignRow.MAX_COLUMNS) {
            System.err.println("Wrong tab size in createTabCampaign. Must be "
                    + CampaignRow.MAX_COLUMNS + " at least.");
            return new PdfPTable(1);
        }

        // one column is added because the first one has a width of 2 columns
        int colsNumber = Utils.countTrueInTab(colsToPrint);
        PdfPTable table = new PdfPTable(colsNumber + 1);
        table.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        table.setWidthPercentage(100);

        // For the headers

        for (int j = 0; j < CampaignRow.MAX_COLUMNS; j++) {
            if (colsToPrint[j]) {

                Font font = new Font(FontFamily.HELVETICA, 8, Font.BOLD);
                Paragraph para = new Paragraph(headers.get(j), font);
                para.setAlignment(Element.ALIGN_CENTER);

                PdfPCell cell = new PdfPCell();
                cell.setPaddingBottom(15);

                cell.setBackgroundColor(headerColor);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.addElement(para);

                if (j == 0)
                    cell.setColspan(2);

                table.addCell(cell);

            }
        }

        // For all the rows
        for (int i = 0, countColor = 0; i < campaignRows.size(); i++) {

            if (!(hideEmptyLines && !campaignRows.get(i).isRelevant())) {

                List<String> l = campaignRows.get(i).toList();
                for (int j = 0; j < CampaignRow.MAX_COLUMNS; j++) {

                    if (colsToPrint[j]) {
                        Font font = new Font(FontFamily.HELVETICA, 8,
                                Font.UNDEFINED);

                        PdfPCell cell = new PdfPCell();
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                        cell.setPaddingBottom(10);
                        cell.setPaddingTop(0);

                        if (j == 0)
                            cell.setColspan(2);

                        Paragraph p = null;

                        if (j == 0)
                            p = new Paragraph(splitFirstColumnData(l.get(0),
                                    getMaxLength(colsNumber)), font);
                        else {
                            p = new Paragraph(l.get(j), font);
                            p.setAlignment(Element.ALIGN_CENTER);
                        }

                        if (countColor < 5)
                            cell.setBackgroundColor(bestRowsColor);
                        else
                            cell.setBackgroundColor(worstRowsColor);

                        cell.addElement(p);

                        table.addCell(cell);
                    }
                }
                countColor++;
            }
        }

        // For the last line (containing the sums usually)
        if (wholeTotal) {
            List<String> a = all.toList();
            for (int j = 0; j < CampaignRow.MAX_COLUMNS; j++) {
                if (colsToPrint[j]) {

                    Font font = new Font(FontFamily.HELVETICA, 8, Font.BOLD);
                    Paragraph p = new Paragraph(a.get(j), font);
                    if (j != 0)
                        p.setAlignment(Element.ALIGN_CENTER);

                    PdfPCell cell = new PdfPCell();
                    cell.addElement(p);
                    cell.setPaddingBottom(10);
                    cell.setPaddingTop(0);
                    cell.setBackgroundColor(lastLineColor);

                    if (j == 0)
                        cell.setColspan(2);

                    table.addCell(cell);
                }
            }

        }

        return table;
    }

    public PdfPTable createTabPeriod(List<CampaignRowPeriod> campaignRows,
            List<String> headers, CampaignRow all, boolean[] colsToPrint,
            boolean hideEmptyLines, boolean interSums,
            List<CampaignRowPeriod> interData, boolean weekly,
            String dateFormat) {

        if (colsToPrint.length < CampaignRowPeriod.MAX_COLUMNS_PERIOD) {
            System.err.println("Wrong tab size in createTabPeriod. Must be "
                    + CampaignRowPeriod.MAX_COLUMNS_PERIOD + " at least.");
            return new PdfPTable(1);
        }

        // 2 column are added because two has a width of 2 columns
        int colsNumber = Utils.countTrueInTab(colsToPrint);
        PdfPTable table = new PdfPTable(colsNumber + 2);
        table.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        table.setWidthPercentage(100);

        // For the headers
        for (int j = 0; j < CampaignRowPeriod.MAX_COLUMNS_PERIOD; j++) {

            if (colsToPrint[j]) {

                Font font = new Font(FontFamily.HELVETICA, 8, Font.BOLD);
                Paragraph para = new Paragraph(headers.get(j), font);
                para.setAlignment(Element.ALIGN_CENTER);

                PdfPCell cell = new PdfPCell();
                cell.setPaddingBottom(15);

                cell.setBackgroundColor(headerColor);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.addElement(para);

                if (j == 0 || j == 1)
                    cell.setColspan(2);

                table.addCell(cell);

            }
        }

        // For all the rows
        for (int i = 0; i < campaignRows.size(); i++) {

            if (!(hideEmptyLines && !campaignRows.get(i).isRelevant())) {

                List<String> l = campaignRows.get(i).toList(weekly, dateFormat);
                for (int j = 0; j < CampaignRowPeriod.MAX_COLUMNS_PERIOD; j++) {

                    if (colsToPrint[j]) {
                        Font font = new Font(FontFamily.HELVETICA, 8,
                                Font.UNDEFINED);

                        PdfPCell cell = new PdfPCell();
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                        cell.setPaddingBottom(10);
                        cell.setPaddingTop(0);

                        if (j == 0 || j == 1)
                            cell.setColspan(2);

                        Paragraph p = null;

                        if (j == 0)
                            p = new Paragraph(splitFirstColumnData(l.get(0),
                                    getMaxLength(colsNumber)), font);
                        else {
                            p = new Paragraph(l.get(j), font);
                            p.setAlignment(Element.ALIGN_CENTER);
                        }

                        cell.setBackgroundColor(bestRowsColor);

                        cell.addElement(p);

                        table.addCell(cell);
                    }
                }

            }

            if (interSums) {

                if ((i + 1) == campaignRows.size()
                        || !campaignRows.get(i).getStartPeriod().equals(
                                campaignRows.get(i + 1).getStartPeriod())) {
                    CampaignRowPeriod zk = CampaignRowPeriod.getRowByDate(
                            campaignRows.get(i).getStartPeriod(), interData);

                    if (zk != null) {
                        List<String> l = zk.toList(weekly, dateFormat);
                        for (int j = 0; j < CampaignRowPeriod.MAX_COLUMNS_PERIOD; j++) {

                            if (colsToPrint[j]) {
                                Font font = new Font(FontFamily.HELVETICA, 8,
                                        Font.BOLD);

                                PdfPCell cell = new PdfPCell();
                                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                                cell.setPaddingBottom(10);
                                cell.setPaddingTop(0);

                                Paragraph p = new Paragraph(
                                        splitFirstColumnData(l.get(j),
                                                getMaxLength(colsNumber)),
                                        font);

                                if (j == 0 || j == 1)
                                    cell.setColspan(2);

                                if (j != 0)
                                    p.setAlignment(Element.ALIGN_CENTER);

                                cell.setBackgroundColor(interSumsColor);

                                cell.addElement(p);

                                table.addCell(cell);
                            }
                        }

                    } // fin zk !=null

                }
            }

        }
        // For the last line (containing the sums usually)

        List<String> a = all.toList();
        a.add(0, "MERGUEZ");
        for (int j = 0; j < CampaignRowPeriod.MAX_COLUMNS_PERIOD; j++) {

            if (colsToPrint[j]) {

                Font font = new Font(FontFamily.HELVETICA, 8, Font.BOLD);
                Paragraph p = new Paragraph(a.get(j), font);
                if (j != 0)
                    p.setAlignment(Element.ALIGN_CENTER);

                PdfPCell cell = new PdfPCell();
                cell.addElement(p);
                cell.setPaddingBottom(10);
                cell.setPaddingTop(0);
                cell.setBackgroundColor(lastLineColor);

                if (j == 0 || j == 1)
                    cell.setColspan(2);

                table.addCell(cell);
            }
        }

        return table;
    }

    private int getIndexFromColsToPrint(boolean[] colsToPrint) {
        for (int i = 1; i < colsToPrint.length; i++) {
            if (colsToPrint[i])
                return i - 1;
        }
        return 0;
    }

    private String splitFirstColumnData(String firstColumnData, int maxLength) {
        StringBuilder res = new StringBuilder();
        int cpt = 0;
        for (int i = 0; i < firstColumnData.length(); i++) {
            if (cpt > maxLength && firstColumnData.charAt(i) == '/') {
                res.append("/\n");
                cpt = 0;

            } else
                res.append(firstColumnData.charAt(i));
            cpt++;

        }
        return res.toString();

    }

    public int getMaxLength(int colsNumber) {

        return (int) -6.67 * colsNumber + 50;
    }

    public PdfPTable getTabSummary(List<SummaryData> data) {

        PdfPTable table = new PdfPTable(2);
        table.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        table.setWidthPercentage(100);

        for (int i = 0; i < data.size(); i++) {

            Font font = new Font(FontFamily.HELVETICA, 12, Font.BOLD);
            Paragraph para = new Paragraph(data.get(i).getAttribution(), font);
            para.setAlignment(Element.ALIGN_RIGHT);

            PdfPCell cell = new PdfPCell();

            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.addElement(para);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);

            Font font2 = new Font(FontFamily.HELVETICA, 12, Font.NORMAL);
            Paragraph para2 = new Paragraph(data.get(i).getValue(), font2);
            para.setAlignment(Element.ALIGN_LEFT);

            PdfPCell cell2 = new PdfPCell();
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell2.addElement(para2);

            table.addCell(cell2);

        }
        return table;

    }

    public PdfPTable createTabPeriodTotal(List<CampaignRowPeriod> campaignRows,
            List<String> headers, CampaignRow all, boolean[] colsToPrint,
            boolean hideEmptyLines, boolean weekly, String dateFormat) {

        if (colsToPrint.length < CampaignRowPeriod.MAX_COLUMNS_PERIOD) {
            System.err.println("Wrong tab size in createTabPeriod. Must be "
                    + CampaignRowPeriod.MAX_COLUMNS_PERIOD + " at least.");
            return new PdfPTable(1);
        }

        // 2 column are added because two has a width of 2 columns
        int colsNumber = Utils.countTrueInTab(colsToPrint);
        PdfPTable table = new PdfPTable(colsNumber + 1);
        table.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        table.setWidthPercentage(100);

        // For the headers
        for (int j = 0; j < CampaignRowPeriod.MAX_COLUMNS_PERIOD; j++) {

            if (colsToPrint[j]) {

                Font font = new Font(FontFamily.HELVETICA, 8, Font.BOLD);
                Paragraph para = new Paragraph(headers.get(j), font);
                para.setAlignment(Element.ALIGN_CENTER);

                PdfPCell cell = new PdfPCell();
                cell.setPaddingBottom(15);

                cell.setBackgroundColor(headerColor);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.addElement(para);

                if (j == 0 || j == 1)
                    cell.setColspan(2);

                table.addCell(cell);

            }
        }

        // For all the rows
        for (int i = 0; i < campaignRows.size(); i++) {

            if (!(hideEmptyLines && !campaignRows.get(i).isRelevant())) {

                List<String> l = campaignRows.get(i).toList(weekly, dateFormat);
                for (int j = 0; j < CampaignRowPeriod.MAX_COLUMNS_PERIOD; j++) {

                    if (colsToPrint[j]) {
                        Font font = new Font(FontFamily.HELVETICA, 8,
                                Font.UNDEFINED);

                        PdfPCell cell = new PdfPCell();
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                        cell.setPaddingBottom(10);
                        cell.setPaddingTop(0);

                        if (j == 0 || j == 1)
                            cell.setColspan(2);

                        Paragraph p = null;

                        if (j == 0)
                            p = new Paragraph(splitFirstColumnData(l.get(0),
                                    getMaxLength(colsNumber)), font);
                        else {
                            p = new Paragraph(l.get(j), font);
                            p.setAlignment(Element.ALIGN_CENTER);
                        }

                        cell.setBackgroundColor(interSumsColor);

                        cell.addElement(p);
                        table.addCell(cell);
                    }
                }

            }

        }
        // For the last line (containing the sums usually)
        if (all != null) {

            List<String> a = all.toList();
            a.add(0, "Total");
            for (int j = 0; j < CampaignRowPeriod.MAX_COLUMNS_PERIOD; j++) {

                if (colsToPrint[j]) {

                    Font font = new Font(FontFamily.HELVETICA, 8, Font.BOLD);
                    Paragraph p = new Paragraph(a.get(j), font);
                    if (j != 0)
                        p.setAlignment(Element.ALIGN_CENTER);

                    PdfPCell cell = new PdfPCell();
                    cell.addElement(p);
                    cell.setPaddingBottom(10);
                    cell.setPaddingTop(0);
                    cell.setBackgroundColor(lastLineColor);

                    if (j == 0 || j == 1)
                        cell.setColspan(2);

                    table.addCell(cell);
                }
            }
        }

        return table;
    }

}
