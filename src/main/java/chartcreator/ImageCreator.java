package main.java.chartcreator;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

import excelreader.Triplet;

public class ImageCreator {

	public Image getTop3BarChart(List<Float> top3) {
		
		BarRenderer renderer = new CustomRenderer(
	            new Paint[] {new Color(0, 176, 240), new Color(0, 176, 240), new Color(0, 176, 240),
	                Color.yellow, Color.orange, Color.cyan,
	                Color.magenta, Color.blue}
	        );
		
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
	    
	    
//	    BarRenderer renderer
//        = (BarRenderer) plot.getRenderer();
	    renderer.setMaximumBarWidth(.1);
	    renderer.setShadowVisible(false);
	    renderer.setBarPainter(new StandardBarPainter());
//		renderer.setPaint();
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
	
	public Image getDoubleValueChart(List<Triplet> triplets, boolean percentageValues) {
		
		BarRenderer renderer = new CustomRenderer(
	            new Paint[] {new Color(132, 189, 0), new Color(255, 102, 0), Color.green,
	                Color.yellow, Color.orange, Color.cyan,
	                Color.magenta, Color.blue}
	        );
		
		DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
		for (int i  = 0; i < triplets.size(); i++) {
			barDataset.setValue(triplets.get(i).getValue(), "", triplets.get(i).getX());
		}
		
	    JFreeChart chart = ChartFactory.createBarChart(
	        "", "", "", barDataset,
	        PlotOrientation.HORIZONTAL, false, true, false);
	    
	    CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setRangeGridlinesVisible(false);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setTickLabelFont(new Font("Calibri",Font.PLAIN,20));
//	    domainAxis.setVisible(false);
	    ValueAxis valueAxis = plot.getRangeAxis();
	    valueAxis.setVisible(false);
	    valueAxis.setUpperMargin(0.18);
	    
	    
//	    BarRenderer renderer
//        = (BarRenderer) plot.getRenderer();
	    renderer.setMaximumBarWidth(.1);
	    renderer.setShadowVisible(false);
	    renderer.setBarPainter(new StandardBarPainter());
        ItemLabelPosition p = new ItemLabelPosition(
            ItemLabelAnchor.OUTSIDE3, TextAnchor.CENTER_LEFT, TextAnchor.CENTER, 0.0
        );
        renderer.setSeriesPositiveItemLabelPosition(0, p);
        NumberFormat format;
		if (percentageValues) {
        	format = new DecimalFormat("#.00%");
        }
        else {
        	format = NumberFormat.getNumberInstance();
        }
		renderer.setSeriesItemLabelGenerator(0,
                new StandardCategoryItemLabelGenerator("  {2}", format));
		renderer.setBaseItemLabelFont(new Font("Calibri",Font.PLAIN,20));

		renderer.setSeriesItemLabelsVisible(0, true);
		plot.setRenderer(renderer);
	    int width = 480;
        int height = 240;
        BufferedImage bufferedImage = chart.createBufferedImage(width, height);

        return bufferedImage;
	}
	
	public static void main(String[] args) throws IOException {
		ImageCreator MERGUEZ = new ImageCreator();
		
//		File outputfile = new File("merguez.png");
//		List<Float> top = new ArrayList<>();
//		top.add(0.2233f);
//		top.add(0.18f);
//		top.add(0.15f);
//		ImageIO.write((RenderedImage) MERGUEZ.getTop3BarChart(top), "png", outputfile);
		
		File output = new File("zhengqin.png");
		List<Triplet> trip = new ArrayList<>();
		trip.add(new Triplet("Férfi", "zhengqin", 76));
		trip.add(new Triplet("Nő", "zhengqin", 56));
		ImageIO.write((RenderedImage) MERGUEZ.getDoubleValueChart(trip, false), "png", output);
		
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
