package main.java.exceltopdf;

import java.io.IOException;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class HeaderFooter extends PdfPageEventHelper {
	
	public static final  int NO_PAGE_COUNT = 0;
	
	public static final int PAGE_COUNT_MIDDLE = 1;

	public static final int PAGE_COUNT_RIGHT = 2;

	private String lineInHeader= "";
	
	private String logoInHeader ="";
	
	private String lineInFooter ="";
	
	private boolean header;
	
	private boolean separatorInHeader;
	
	private boolean footer;
	
	private boolean separatorInFooter;
	
	private int pagesCount;
	
	public HeaderFooter() {
		
	}

    public HeaderFooter(boolean header, boolean separatorInHeader, boolean footer, boolean separatorInFooter,
			int pagesCount) {
		
		this.header = header;
		this.separatorInHeader = separatorInHeader;
		this.footer = footer;
		this.separatorInFooter = separatorInFooter;
		this.pagesCount = pagesCount;
	}


	public void onStartPage(PdfWriter writer, Document document) {
		if (header) {
			Phrase topLeftCorner = new Phrase();
			
			if (lineInHeader.equals("")) {
				topLeftCorner.add("www.gemius.hu");
			}
			else {
				topLeftCorner.add(lineInHeader);
			}
			
			// text in header
			BaseFont bf;
			try {
				bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
				
				PdfContentByte cb = writer.getDirectContent();
				
				cb.saveState();
				cb.beginText();
				cb.moveText(85, PageSize.A4.getHeight() - 60);
				cb.setFontAndSize(bf, 12);
				cb.showText(lineInHeader);
				cb.endText();
				cb.restoreState();
			
				} catch (DocumentException | IOException e1) {
					e1.printStackTrace();
				}
	        try {
	        	Image img = null;
	        	if (logoInHeader.equals("")) {
	        		img = Image.getInstance(getClass().getResource("/GemiusLogo.png"));
	        	}
	        	else {
	        		img = Image.getInstance(logoInHeader);
	        	}
	        	
	            img.scaleAbsoluteHeight(40);
	            img.scaleAbsoluteWidth(100);
	            
	            img.setAbsolutePosition(410, 780);
	            img.setAlignment(Element.ALIGN_CENTER);
	            writer.getDirectContent().addImage(img);
			} catch (DocumentException | IOException e) {
				e.printStackTrace();
			}
	        
	        if (separatorInHeader) {
	        	// draws a line below the header
	        	PdfContentByte cb = writer.getDirectContent();
				cb.setLineWidth(1.0f);
				float x = 85;
				float y = PageSize.A4.getHeight() - 72;
				cb.moveTo(x, y);
				cb.lineTo(PageSize.A4.getWidth() - 85, y);
				cb.stroke();
	        }
		}
    }

    public void onEndPage(PdfWriter writer, Document document) {
    	
    	if (footer) {
    		if (!lineInFooter.equals("")) {
    			BaseFont bf;
    			try {
    				bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
    				
    				PdfContentByte cb = writer.getDirectContent();
    				
    				cb.saveState();
    				cb.beginText();
    				cb.moveText(85, 60);
    				cb.setFontAndSize(bf, 12);
    				cb.showText(lineInFooter);
    				cb.endText();
    				cb.restoreState();
    			
    				} catch (DocumentException | IOException e1) {
    					e1.printStackTrace();
    				}
    		}
    		
    		if (pagesCount == PAGE_COUNT_MIDDLE) {
    			BaseFont bf;
    			try {
    				bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
    				String page = String.valueOf(document.getPageNumber() + ExcelToPdf.CURRENT_PAGE_NUMBER);
    				Chunk toMeasurePage = new Chunk(page);
    				
    				float xPositionPage = (PageSize.A4.getWidth() / 2) - (toMeasurePage.getWidthPoint() / 2);
    				PdfContentByte cb = writer.getDirectContent();
    				
    				cb.saveState();
    				cb.beginText();
    				cb.moveText(xPositionPage, 60);
    				cb.setFontAndSize(bf, 12);
    				cb.showText(page);
    				cb.endText();
    				cb.restoreState();
    			
    				} catch (DocumentException | IOException e1) {
    					e1.printStackTrace();
    				}
    		}
    		else if (pagesCount == PAGE_COUNT_RIGHT) {
    			BaseFont bf;
    			try {
    				bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
    				String page = String.valueOf(document.getPageNumber() + ExcelToPdf.CURRENT_PAGE_NUMBER);
    				Chunk toMeasurePage = new Chunk(page);
    				
    				float xPositionPage = PageSize.A4.getWidth() - 85 - toMeasurePage.getWidthPoint();
    				PdfContentByte cb = writer.getDirectContent();
    				cb.saveState();
    				cb.beginText();
    				cb.moveText(xPositionPage, 60);
    				cb.setFontAndSize(bf, 12);
    				cb.showText(page);
    				cb.endText();
    				cb.restoreState();
    			
    				} catch (DocumentException | IOException e1) {
    					e1.printStackTrace();
    				}
    		}
            
            if (separatorInFooter) {
        		PdfContentByte cb = writer.getDirectContent();

        		cb.setLineWidth(1.0f);
        		float x = 85;
        		float y = 72;
        		cb.moveTo(x, y);
        		cb.lineTo(PageSize.A4.getWidth() - 85, y);
        		cb.stroke();
        	}
    	}
    }

	public void setLineInHeader(String lineInHeader) {
		this.lineInHeader = lineInHeader;
	}

	public void setLogoInHeader(String logoInHeader) {
		this.logoInHeader = logoInHeader;
	}

	public void setLineInFooter(String lineInFooter) {
		this.lineInFooter = lineInFooter;
	}
    
    

}