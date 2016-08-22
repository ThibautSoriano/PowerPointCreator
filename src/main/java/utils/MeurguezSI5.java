package main.java.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class MeurguezSI5 {

    public static void main(String[] args)
            throws DocumentException, IOException {
        Document document = new Document();
        FileOutputStream outputStream = new FileOutputStream("BorbalAnita.pdf");
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);

        document.open();

        document.add(new Paragraph("GuezMer"));

        String $_MERG = "MERG MERG MERG MERG MERG MERG MERG";
        
        
        $_MERG = Utils.splitLongTextToFitPage("k\u00E9sz\u00EDtette :");
//        Paragraph p = new Paragraph(Utils.splitLongTextToFitPage(
//                "MERG MERG MERG MERG MERG MERG MERG MERG MERG MERG MERG "
//                + "MERG MERG MERG MERG MERG MERG MERG MERG MERG MERG MERG "
//                + "MERG MERG MERG MERG MERG MERG MERG MERG MERG MERG MERG MERG"
//                + " MERG MERG MERG MERG MERG MERG MERG MERG MERG MERG MERG MERG "));

        
//        p.setAlignment(Element.ALIGN_CENTER);
        
        PdfContentByte cb = writer.getDirectContent();
        BaseFont bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.EMBEDDED);
        Chunk toMeasureSize = new Chunk($_MERG);
        Font fontTitle = new Font();
        fontTitle.setStyle(Font.BOLD);
        fontTitle.setSize(16);
            toMeasureSize.setFont(fontTitle);
            
            float xPosition = (PageSize.A4.getWidth() - toMeasureSize.getWidthPoint()) / 2;
            cb.saveState();
            cb.beginText();
            cb.moveText(xPosition, 490);
            cb.setFontAndSize(bfBold, 16);
            cb.showText($_MERG.toLowerCase());
            cb.endText();
            cb.restoreState();
        
        
//        document.add(p);
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
