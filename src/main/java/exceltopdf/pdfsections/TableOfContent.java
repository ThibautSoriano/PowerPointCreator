package main.java.exceltopdf.pdfsections;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.itextpdf.io.IOException;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfCopy.PageStamp;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

import main.java.datasdownloading.entities.TocElement;
import main.java.exceltopdf.ExcelToPdf;


public class TableOfContent {
	

//	public static final String SRC1 = "tmp_summary_page.pdf";
//    public static final String SRC2 = "1tmp_content_page.pdf";
//    public static final String SRC3 = "3tmp_content_page.pdf";
//    public static final String DEST = "MERGUEZ.pdf";
// 
//    public Map<String, PdfReader> filesToMerge = new TreeMap<String, PdfReader>();
 
//    public static void main(String[] args) throws IOException, DocumentException, java.io.IOException {
//        File file = new File(DEST);
////        file.getParentFile().mkdirs();
//        TableOfContent app = new TableOfContent();
//        app.createPdf(DEST);
//    }
 
//    public TableOfContent() throws IOException, java.io.IOException {
//    	filesToMerge = files;
////        filesToMerge.put("Summary", new PdfReader(SRC1));
////        filesToMerge.put("Content page 1", new PdfReader(SRC2));
//    }
	
	 public void createPdf(String filename, String SRC3, List<TocElement> filesToMerge, List<String> filesToDelete, List<String> filesNotInTOC, boolean tocStart) throws IOException, DocumentException, java.io.IOException {
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        Map<Integer, String> toc = new TreeMap<Integer, String>();
	        Document document = new Document();
	        PdfCopy copy = new PdfCopy(document, baos);
	        PageStamp stamp;
	        document.open();
	        
	        int n;
	        int pageNo = 0;
	        PdfImportedPage page;
	        Chunk chunk;
//	        FileOutputStream tempConcat = new FileOutputStream("concatStart.pdf");
	        
//	        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
//        	Document doc = new Document();
//        	PdfCopy copy2 = new PdfCopy(doc, baos2);
        	
	        for (int i = 0; i < filesNotInTOC.size(); i++) {
	        	
	        	PdfReader r = new PdfReader(filesNotInTOC.get(i));
//	        	page = copy.getImportedPage(r, 1);
//	        	copy2.addPage(page);
//	        	r.close();
	        	page = copy.getImportedPage(r, 1);
	        	copy.addPage(page);
	        	r.close();
	        }
	        
//	       PdfReader starter = new PdfReader(baos.toByteArray());
//	       doc.close();
	        
	        for (TocElement elem : filesToMerge) {
	        	PdfReader r = new PdfReader(elem.getDocToConcat());
	            n = r.getNumberOfPages();
	            toc.put(pageNo + 1, elem.getChapter());
	            for (int i = 0; i < n; ) {
	                pageNo++;
	                page = copy.getImportedPage(r, ++i);
	                stamp = copy.createPageStamp(page);
	                chunk = new Chunk(String.format("Goto %d", pageNo), new Font(FontFamily.HELVETICA, 1, Font.UNDERLINE, BaseColor.WHITE));
	                if (i == 1)
	                    chunk.setLocalDestination("p" + pageNo);
	                ColumnText.showTextAligned(stamp.getUnderContent(),
	                        Element.ALIGN_RIGHT, new Phrase(chunk),
	                        559, 810, 0);
	                stamp.alterContents();
	                copy.addPage(page);
	            }
	            r.close();
	        }
	        PdfReader reader = new PdfReader(SRC3);
	        page = copy.getImportedPage(reader, 1);
	        stamp = copy.createPageStamp(page);
	        Paragraph p;
	        PdfAction action;
	        PdfAnnotation link;
	        float y = 670;
	        ColumnText ct = new ColumnText(stamp.getOverContent());
	        ct.setSimpleColumn(85, 85, 511, y);
	        for (Map.Entry<Integer, String> entry : toc.entrySet()) {
	            p = new Paragraph(entry.getValue());
	            p.add(new Chunk(new DottedLineSeparator()));
	            p.add(String.valueOf(entry.getKey()));
	            ct.addElement(p);
	            ct.go();
	            action = PdfAction.gotoLocalPage("p" + entry.getKey(), false);
	            link = new PdfAnnotation(copy, 36, ct.getYLine(), 559, y, action);
	            stamp.addAnnotation(link);
	            y = ct.getYLine();
	        }
	        ct.go();
	        stamp.alterContents();
	        copy.addPage(page);
	        document.close();
//	        for (TocElement t : filesToMerge) {
//	            t.getDocToConcat().close();
//	        }
	        reader.close();
	 
	        reader = new PdfReader(baos.toByteArray());
	        n = reader.getNumberOfPages();
	        
	        
	        
	        
	        FileOutputStream finalPdf = new FileOutputStream(filename);
//	        PdfReader notInToc = new PdfReader("concatStart.pdf");
	        if (tocStart) {
	        	reader.selectPages(String.format("1-%d, %d, %d-%d",filesNotInTOC.size(), n, filesNotInTOC.size(), n-1));
	        	
	        	
//	        	PdfStamper stamp2 = new PdfStamper(reader, finalPdf);
	        	
//	        	stamp2.close();
	        	
//		        stamper.close();
	        }
//	        else {
//	        	reader.selectPages(String.format("1-%d, %d, %d-%d",filesNotInTOC.size(), n, filesNotInTOC.size(), n-1));
//	        	PdfStamper stamp2 = new PdfStamper(reader, finalPdf);
//	        	stamp2.close();
//	        	stamper.close();
//	        }
	        PdfStamper stamper = new PdfStamper(reader, finalPdf);
//	        PdfStamper stamper = new PdfStamper(reader, finalPdf);
	        stamper.close();
	        finalPdf.close();
//	        tempConcat.close();
	        
	        for (int i = 0; i < filesToDelete.size(); i++) {
	        	File f = new File(filesToDelete.get(i));
	        	System.out.println("trying to delete " + filesToDelete.get(i));
	        	f.delete();
	        }
//	        File start = new File("concatStart.pdf");
//	        start.delete();
	        ExcelToPdf.CURRENT_PAGE_NUMBER = 0;
	        
	        if (Desktop.isDesktopSupported()) {
			    try {
			        File myFile = new File(filename);
			        Desktop.getDesktop().open(myFile);
			    } catch (IOException ex) {
			        // no application registered for PDFs
			    }
			}
	    }
}