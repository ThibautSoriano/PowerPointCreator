package main.java.chartcreator;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
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

import org.apache.poi.util.IOUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;

import excelreader.Triplet;

public class ImageCreator {

//	public BufferedImage getTop3BarChart(List<Triplet> top3) {
//		
////		BarRenderer renderer = new CustomRenderer(
////	            new Paint[] {new Color(0, 176, 240), new Color(0, 176, 240), new Color(0, 176, 240),
////	                Color.yellow, Color.orange, Color.cyan,
////	                Color.magenta, Color.blue}
////	        );
//		
//		DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
//		for (int i  = 0; i < top3.size(); i++) {
//			barDataset.setValue(top3.get(i).getValue(), "Merguez " + i, "Merg" + i);
//		}
//		
//	    JFreeChart chart = ChartFactory.createBarChart(
//	        "", "", "", barDataset,
//	        PlotOrientation.HORIZONTAL, false, true, false);
//	    
//	    CategoryPlot plot = (CategoryPlot) chart.getPlot();
//        plot.setRangeGridlinesVisible(false);
//        plot.setBackgroundPaint(Color.WHITE);
//        plot.setOutlineVisible(false);
//        
//        CategoryAxis domainAxis = plot.getDomainAxis();
//	    domainAxis.setVisible(false);
//	    ValueAxis valueAxis = plot.getRangeAxis();
//	    valueAxis.setVisible(false);
//	    valueAxis.setUpperMargin(0.22);
//	    
//	    
//	    BarRenderer renderer
//        = (BarRenderer) plot.getRenderer();
//	    renderer.setMaximumBarWidth(.35);
//	    renderer.setShadowVisible(false);
//	    renderer.setBarPainter(new StandardBarPainter());
//	    renderer.setSeriesPaint(0, new Color(0, 176, 240));
//	    renderer.setSeriesPaint(1, new Color(0, 176, 240));
//	    renderer.setSeriesPaint(2, new Color(0, 176, 240));
////		renderer.setPaint();
////	    ItemLabelPosition p = new ItemLabelPosition(
////	            ItemLabelAnchor.OUTSIDE3, TextAnchor.CENTER_LEFT, TextAnchor.CENTER, 0.0
////	        );
////        renderer.setSeriesPositiveItemLabelPosition(0, p);
//		DecimalFormat pctFormat = new DecimalFormat("#.00%");
//		renderer.setSeriesItemLabelGenerator(0,
//                new StandardCategoryItemLabelGenerator("  {2}",pctFormat));
//		renderer.setBaseItemLabelFont(new Font("Arial",Font.PLAIN,20));
//		renderer.setSeriesItemLabelGenerator(1,
//                new StandardCategoryItemLabelGenerator("  {2}", pctFormat));
//		renderer.setSeriesItemLabelGenerator(2,
//                new StandardCategoryItemLabelGenerator("  {2}", pctFormat));
//		renderer.setBaseItemLabelFont(new Font("Arial",Font.PLAIN,20));
//		
//		renderer.setSeriesItemLabelsVisible(0, true);
//		renderer.setSeriesItemLabelsVisible(1, true);
//		renderer.setSeriesItemLabelsVisible(2, true);
//		
////		plot.setRenderer(renderer);
//		
//	    int width = 550;
//        int height = 300;
//        BufferedImage bufferedImage = chart.createBufferedImage(width, height);
//
//        return bufferedImage;
//	}
//	
	public BufferedImage getDoubleValueChart(List<Triplet> triplets, boolean percentageValues) {
		
		BarRenderer renderer = new CustomRenderer(
	            new Paint[] {new Color(132, 189, 0), new Color(255, 102, 0)}
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
        domainAxis.setTickLabelFont(new Font("Arial",Font.PLAIN,14));
//	    domainAxis.setVisible(false);
	    ValueAxis valueAxis = plot.getRangeAxis();
	    valueAxis.setVisible(false);
	    valueAxis.setUpperMargin(0.18);
	    
	    
//	    BarRenderer renderer
//        = (BarRenderer) plot.getRenderer();
	    renderer.setMaximumBarWidth(.2);
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
		renderer.setBaseItemLabelFont(new Font("Arial",Font.PLAIN,14));

		renderer.setSeriesItemLabelsVisible(0, true);
		plot.setRenderer(renderer);
		
	    int width = 400;
        int height = 150;
        BufferedImage bufferedImage = chart.createBufferedImage(width, height);

        return bufferedImage;
	}
	
	public BufferedImage getTop3BarChart(List<Triplet> top3) {
		BarRenderer renderer = new CustomRenderer(
	            new Paint[] {new Color(0, 176, 240), new Color(0, 176, 240), new Color(0, 176, 240)}
	        );
		
		DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
		for (int i  = 0; i < top3.size(); i++) {
			barDataset.setValue(top3.get(i).getValue(), "", top3.get(i).getX());
		}
		
	    JFreeChart chart = ChartFactory.createBarChart(
	        "", "", "", barDataset,
	        PlotOrientation.HORIZONTAL, false, true, false);
	    
	    CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setRangeGridlinesVisible(false);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        
        CategoryAxis domainAxis = plot.getDomainAxis();
//        domainAxis.setVisible(false);
        domainAxis.setTickLabelFont(new Font("Arial",Font.PLAIN,0));
        
//	    domainAxis.setVisible(false);
	    ValueAxis valueAxis = plot.getRangeAxis();
	    valueAxis.setVisible(false);
	    valueAxis.setUpperMargin(0.18);
	    
	    
//	    BarRenderer renderer
//        = (BarRenderer) plot.getRenderer();
	    renderer.setMaximumBarWidth(.15);
	    renderer.setShadowVisible(false);
	    renderer.setBarPainter(new StandardBarPainter());
        ItemLabelPosition p = new ItemLabelPosition(
            ItemLabelAnchor.OUTSIDE3, TextAnchor.CENTER_LEFT, TextAnchor.CENTER, 0.0
        );
        renderer.setSeriesPositiveItemLabelPosition(0, p);
        NumberFormat format;
        	format = new DecimalFormat("#.00%");
        
		renderer.setSeriesItemLabelGenerator(0,
                new StandardCategoryItemLabelGenerator("  {2}", format));
		renderer.setBaseItemLabelFont(new Font("Arial",Font.PLAIN,16));

		renderer.setSeriesItemLabelsVisible(0, true);
		plot.setRenderer(renderer);
		
		int width = 550;
        int height = 300;
        BufferedImage bufferedImage = chart.createBufferedImage(width, height);

        return bufferedImage;
	}
	
	public BufferedImage getAllAgesChart(List<Triplet> triplets, boolean percentageValues) {
		
		BarRenderer renderer = new CustomRenderer(
	            new Paint[] {new Color(0, 176, 240), new Color(255, 0, 102), new Color(153, 0, 153),
	                new Color(16, 24, 32), new Color(132, 189, 0), new Color(255, 102, 0)}
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
        domainAxis.setTickLabelFont(new Font("Arial",Font.PLAIN,16));
//	    domainAxis.setVisible(false);
	    ValueAxis valueAxis = plot.getRangeAxis();
	    valueAxis.setVisible(false);
	    valueAxis.setUpperMargin(0.3);
	    
	    
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
		renderer.setBaseItemLabelFont(new Font("Arial",Font.PLAIN,16));

		renderer.setSeriesItemLabelsVisible(0, true);
		plot.setRenderer(renderer);
	    int width = 400;
        int height = 350;
        BufferedImage bufferedImage = chart.createBufferedImage(width, height);

        return bufferedImage;
	}
	
	public BufferedImage getPcMobilTabletChart(List<Triplet> triplets, boolean percentageValues, boolean halfPageSize) {
		
//		BarRenderer renderer = new CustomRenderer(
//	            new Paint[] {new Color(0, 176, 240), new Color(255, 0, 102), new Color(153, 0, 153),
//	                new Color(16, 24, 32), new Color(132, 189, 0), new Color(255, 102, 0)}
//	        );

		int fontSize = 16;
		if (halfPageSize) {
			fontSize = 14;
		}
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i  = 0; i < triplets.size(); i++) {
			dataset.setValue(triplets.get(i).getValue(), triplets.get(i).getY(), triplets.get(i).getX());
		}
        
        JFreeChart chart = ChartFactory.createBarChart("", "", "", dataset,
				PlotOrientation.HORIZONTAL,
				true, // include legend
				false,
				false
		);
        
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setRangeGridlinesVisible(false);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setTickLabelFont(new Font("Arial",Font.PLAIN,fontSize));
        
//	    domainAxis.setVisible(false);
	    ValueAxis valueAxis = plot.getRangeAxis();
	    valueAxis.setVisible(false);
	    valueAxis.setUpperMargin(0.3);
	    
	    
	    BarRenderer renderer
        = (BarRenderer) plot.getRenderer();
	    renderer.setItemMargin(0);
	    renderer.setSeriesPaint(0, new Color(0, 176, 240));
	    renderer.setSeriesPaint(1, new Color(255, 0, 102));
	    renderer.setSeriesPaint(2, new Color(153, 0, 153));
	    if (halfPageSize) {
	    	renderer.setMaximumBarWidth(.1);
	    } else {
	    	renderer.setMaximumBarWidth(.03);
	    }
	    
	    renderer.setShadowVisible(false);
	    renderer.setBarPainter(new StandardBarPainter());

        NumberFormat format;
		if (percentageValues) {
        	format = new DecimalFormat("#.00%");
        }
        else {
        	format = NumberFormat.getNumberInstance();
        }
		renderer.setSeriesItemLabelGenerator(0,
                new StandardCategoryItemLabelGenerator("  {2}", format));
		renderer.setSeriesItemLabelGenerator(1,
                new StandardCategoryItemLabelGenerator("  {2}", format));
		renderer.setSeriesItemLabelGenerator(2,
                new StandardCategoryItemLabelGenerator("  {2}", format));
		renderer.setBaseItemLabelFont(new Font("Arial",Font.PLAIN,fontSize));
		
		renderer.setSeriesItemLabelsVisible(0, true);
		renderer.setSeriesItemLabelsVisible(1, true);
		renderer.setSeriesItemLabelsVisible(2, true);
//		plot.setRenderer(renderer);
		
		LegendTitle legend = chart.getLegend();
		legend.setFrame(BlockBorder.NONE);
		legend.setPosition(RectangleEdge.TOP);
		legend.setItemFont(new Font("Arial",Font.PLAIN,fontSize));
		
		int width = 450;
        int height = 350;
        
        if (halfPageSize) {
        	width = 450;
        	height = 150;
        }
        
        BufferedImage bufferedImage = chart.createBufferedImage(width, height);
 
        return bufferedImage;
	}
	
	public static void main(String[] args) throws IOException {
		ImageCreator MERGUEZ = new ImageCreator();
		
		File outputfile = new File("merguez.png");
		List<Triplet> trip = new ArrayList<>();
		trip.add(new Triplet("merguez", "zhengqin", 1f));
		trip.add(new Triplet("mer", "zhengqin", 0.2233f));
		trip.add(new Triplet("guez", "zhengqin", 0.15f));

		ImageIO.write((RenderedImage) MERGUEZ.getTop3BarChart(trip), "png", outputfile);
		
//		File output = new File("zhengqin.png");
//		List<Triplet> trip = new ArrayList<>();
//		trip.add(new Triplet("Férfi", "zhengqin", 76));
//		trip.add(new Triplet("Nő", "zhengqin", 56));
//		ImageIO.write((RenderedImage) MERGUEZ.getDoubleValueChart(trip, false), "png", output);
		
//		File output = new File("yukaiwen.png");
//		List<Triplet> trip = new ArrayList<>();
//		trip.add(new Triplet("60+", "zhengqin", 0.88f));
//		trip.add(new Triplet("50 - 59", "zhengqin", 0.3f));
//		trip.add(new Triplet("40 - 49", "zhengqin", 0.344f));
//		trip.add(new Triplet("30 - 39", "zhengqin", 0.766f));
//		trip.add(new Triplet("18 - 29", "zhengqin", 0.2222f));
//		trip.add(new Triplet("15 - 17", "zhengqin", 1f));
//		ImageIO.write((RenderedImage) MERGUEZ.getAllAgesChart(trip, true), "png", output);
		
//		File output = new File("diasdos.png");
//		List<Triplet> trip = new ArrayList<>();
//		trip.add(new Triplet("60+", "PC", 45));
//		trip.add(new Triplet("50 - 59", "PC", 32));
//		trip.add(new Triplet("40 - 49", "PC", 43));
//		trip.add(new Triplet("30 - 39", "PC", 24));
//		trip.add(new Triplet("18 - 29", "PC", 65));
//		trip.add(new Triplet("15 - 17", "PC", 32));
//		
//		trip.add(new Triplet("60+", "MOBIL", 45));
//		trip.add(new Triplet("50 - 59", "MOBIL", 56));
//		trip.add(new Triplet("40 - 49", "MOBIL", 76));
//		trip.add(new Triplet("30 - 39", "MOBIL", 12));
//		trip.add(new Triplet("18 - 29", "MOBIL", 87));
//		trip.add(new Triplet("15 - 17", "MOBIL", 54));
//		
//		trip.add(new Triplet("60+", "TABLET", 15));
//		trip.add(new Triplet("50 - 59", "TABLET", 65));
//		trip.add(new Triplet("40 - 49", "TABLET", 34));
//		trip.add(new Triplet("30 - 39", "TABLET", 23));
//		trip.add(new Triplet("18 - 29", "TABLET", 65));
//		trip.add(new Triplet("15 - 17", "TABLET", 12));
//		ImageIO.write((RenderedImage) MERGUEZ.getPcMobilTabletChart(trip, false, false), "png", output);
		
		
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
//    	XSLFPictureShape merg = slide.createPicture(idx);
//    	merg.setAnchor(new Rectangle(100, 100, 100, 200));
//    	
//    	ppt.write(out);
	}
}
