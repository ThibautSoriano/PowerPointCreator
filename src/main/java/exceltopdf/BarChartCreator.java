package main.java.exceltopdf;

import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import main.java.excelreader.entities.CampaignRow;
 
		public class BarChartCreator {
		 
		    public JFreeChart getChart(List<CampaignRow> campaignRows, int colIndex, String abscissa, String ordinate) {
		        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		        
		        if (campaignRows.size() > 5) {
		            for (int i = 0; i < 5; i++) {
                                dataset.addValue(campaignRows.get(i).toListFloat().get(colIndex), campaignRows.get(i).getFirstColumnData(), "");
                        }
                         
                        JFreeChart chart = ChartFactory.createBarChart3D("", ordinate, abscissa, dataset, PlotOrientation.HORIZONTAL,             
                                    true, true, false);
                        return chart;
		        }
		        
		        return null;
		    }
}