package main.java.chartcreator;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

public class ImageCreator {

	public Image getTop3BarChart(List<Float> top3) {
		DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
		for (int i  = 0; i < top3.size(); i++) {
			barDataset.setValue(top3.get(i), "", "Merg" + i);
		}
		
	    JFreeChart chart = ChartFactory.createBarChart(
	        "", "", "", barDataset,
	        PlotOrientation.HORIZONTAL, false, true, false);
	    
	    CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setRangeGridlinesVisible(false);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        
        CategoryAxis domainAxis = plot.getDomainAxis();
	    domainAxis.setVisible(false);
	    ValueAxis valueAxis = plot.getRangeAxis();
	    valueAxis.setVisible(false);
	    valueAxis.setUpperMargin(0.18);
	    
	    
	    BarRenderer renderer
        = (BarRenderer) plot.getRenderer();
	    renderer.setMaximumBarWidth(.1);
	    renderer.setShadowVisible(false);
	    renderer.setBarPainter(new StandardBarPainter());
		renderer.setPaint(new Color(0, 176, 240));
		DecimalFormat pctFormat = new DecimalFormat("#.00%");
		renderer.setSeriesItemLabelGenerator(0,
                new StandardCategoryItemLabelGenerator("  {2}",pctFormat));
		renderer.setBaseItemLabelFont(new Font("Calibri",Font.PLAIN,20));

		renderer.setSeriesItemLabelsVisible(0, true);
	    int width = 600;
        int height = 300;
        BufferedImage bufferedImage = chart.createBufferedImage(width, height);

        return bufferedImage;
	}
	
	public static void main(String[] args) throws IOException {
		ImageCreator MERGUEZ = new ImageCreator();
		
		File outputfile = new File("merguez.png");
		List<Float> top = new ArrayList<>();
		top.add(0.2233f);
		top.add(0.18f);
		top.add(0.15f);
		ImageIO.write((RenderedImage) MERGUEZ.getTop3BarChart(top), "png", outputfile);
		
		
//		FileOutputStream out;
//        try {
//        	out = new FileOutputStream("fornetti.pptx");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return;
//        }
//		XMLSlideShow ppt = new XMLSlideShow();
//        XSLFSlide slide = ppt.createSlide();
//        
//        File image=new File("merguez.png");
//
//    	byte[] picture = IOUtils.toByteArray(new FileInputStream(image));
//    	PictureData idx = ppt.addPicture(picture, PictureData.PictureType.PNG);
//    	slide.createPicture(idx);
//    	
//    	
//    	ppt.write(out);
	}
}
