package main.java.exceltopdf;

import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;

import main.java.excelreader.entities.CampaignRow;

public class PieChartCreator {
 
    public JFreeChart getChart(List<CampaignRow> campaignRows, int colIndex, String title, boolean isRate, int numeratorIndex, int denominatorIndex) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        int nbElements = 0;
        int currentIndex = 0;
    
    	for (int i = 0; nbElements < 4; i++) {
    	    
        	if (campaignRows.get(i).isRelevant()) {
        		dataset.setValue(campaignRows.get(i).getFirstColumnData(), campaignRows.get(i).toListFloat().get(colIndex));
        		nbElements++;
        	}
        	
        	currentIndex = i;
        }
    	
    	float others = 0;
        
        if (isRate) {
                others = getPercentage(campaignRows, currentIndex, numeratorIndex, denominatorIndex);
        } else {
                for (int j = currentIndex; j < campaignRows.size(); j++) {
                        others += campaignRows.get(j).toListFloat().get(colIndex);
                }
        }
        dataset.setValue("Others", others);
    	
        
        JFreeChart chart = ChartFactory.createPieChart3D(title, dataset, true, true, false);

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setLabelGenerator(null);
        
        return chart;
    }

	private float getPercentage(List<CampaignRow> campaignRows, int startIndex, int numeratorIndex, int denominatorIndex) {
		float numerator = 0;
		float denominator = 0;
		float result = 0;
		
		for (int i = startIndex; i < campaignRows.size(); i++) {
			numerator += campaignRows.get(i).toListFloat().get(numeratorIndex);
			denominator += campaignRows.get(i).toListFloat().get(denominatorIndex);
		}
		
		if (denominator > 0) {
			result = numerator / denominator;
		}
		
		return result;
	}
}