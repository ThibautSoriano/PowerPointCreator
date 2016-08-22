import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import slideshow.BarChart;
import slideshow.ChartType;
import slideshow.RawSlide;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException {
//		new ConverterWindow();
	 
	    
	RawSlide dias = new RawSlide("dias");
//	
	String [] a = { "dias", "dos"};
	    
	    BarChart merguez = new BarChart(dias,a,a,"merguez",ChartType.PurplePercentage,0,1);
	    merguez.createChart();
	    merguez.createChartImage();
	    
	    
//	     XMLSlideShow ppt;
//	        /**
//	         * The slides of the PPT.
//	         */
//	         XSLFSlide[] slides;
//	    
//	         
//	         ppt = new XMLSlideShow(new FileInputStream("dias.pptx"));
	         
	    
	}

}
