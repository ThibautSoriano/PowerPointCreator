package slideshow;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the pure data of a slide.
 * 
 * @author Chloé & Pierre
 */
public class RawSlide {
	/**
	 * The list of cells retrieved from the Excel file.
	 */
	private List<CellData> cells;
	/**
	 * Slide title.
	 */
	private String title;
	/**
	 * Slide number.
	 */
	private int number;

	public RawSlide(String title) {
		cells = new ArrayList<>();
		this.title = title;
		this.number = computeSlideNumber();
	}

	private int computeSlideNumber() {
		int n = -1;

//		try {
//		    System.out.println(title);
//			n = Integer.parseInt(title.split("(?i:slide )")[1]);
//		} catch (NumberFormatException nfe) {
//			n = -1;
//		}
//TODO
		return 1;
	}

	public void addCellData(CellData cell) {
		cells.add(cell);
	}

	public String getTitle() {
		return this.title;
	}

	public int getNumber() {
		return this.number;
	}

	public List<CellData> getCells() {
		return cells;
	}

	public void print() {
		System.out.println("TITLE : " + title);
		System.out.println("NUMBER : " + number);
		for (CellData cellData : cells) {
			System.out.println("|" + cellData.getText() + " : " + cellData.getHexaColor() + " (" + cellData.getX()
					+ ", " + cellData.getY() + ") |");
		}

		System.out.println();
	}

	/**
	 * Creates a stacked chart and returns its name.
	 * 
	 * @return The name of the chart created.
	 */
	private String getStackedChart() {
		String imgName = "chart_stack_" + this.number;
		ChartType chartType;
		// Number of rows of the stacked chart to create.
		int nrows = ((this.cells.size() - 3) / 6) - 1;
		String[] rows = new String[nrows];
		// Sets the name of each column.
		String[] cols = { this.getCells().get(7).getText(), this.getCells().get(8).getText() };

		// Sets the name of each row.
		for (int i = 9, j = 0; j < rows.length; i += 6, j++) {
			rows[j] = this.cells.get(i).getText();
		}

		// Sets the type of chart to create (color handling)
		if (nrows == 2)
			chartType = ChartType.OrangePercentage;
		else if (nrows == 6)
			chartType = ChartType.PurplePercentage;
		else if (nrows == 5)
			chartType = ChartType.GreenPercentage;
		else if (nrows == 4)
			chartType = ChartType.PinkPercentage;
		else
			chartType = ChartType.Other;

		// Chart creation.
		Chart stackedChart = new StackedChart(this, cols, rows, imgName, chartType);
		stackedChart.createChart();
		// Generates the image.
		stackedChart.createChartImage();

		return stackedChart.getImageName();
	}

	/**
	 * Creates one or two bar charts according to some parameters.
	 * 
	 * @param nCharts
	 *            The number of bar charts in the slide.
	 * @param ncols
	 *            The number of columns of both charts.
	 * @return The names of the charts created.
	 */
	private List<String> getBarChart(int nCharts, int ncols) {
		List<String> imgNames = new ArrayList<>();
		ChartType chartType;

		if (this.number == 14) {
			String[] rows = new String[3];
			String[] col = { "" };

			for (int i = 1, j = 0; j < rows.length; i += 2, j++) {
				rows[j] = this.cells.get(i).getText();
			}
			chartType = ChartType.Other;
			Chart barChart1 = new BarChart(this, col, rows, "chart_bar_1_" + this.number, chartType, 2, 2);
			barChart1.createChart();
			barChart1.createChartImage();
			imgNames.add(barChart1.getImageName());
		} else if (this.number == 15) {
			String[] rows = new String[2];
			String[] cols = new String[6];
			
			rows[0] = this.cells.get(2).getText();
			rows[1] = this.cells.get(3).getText();
			
			for(int i = 4, j = 0 ; j < 6 ; i += 3, j++){
				cols[j] = this.cells.get(i).getText();
			}
			
			chartType = ChartType.Other;
			Chart barChart1 = new BarChart(this, cols, rows, "chart_bar_1_" + this.number, chartType, 5, 3);
			barChart1.createChart();
			barChart1.createChartImage();
			imgNames.add(barChart1.getImageName());
		} else {
			// Number of rows of the bar chart to create.
			int nrows = ((this.cells.size() - 3) / ncols) - 1;

			String[] rows = new String[nrows];
			// Sets the name of each column.
			String[] col1 = null;
			if (nCharts == 1) {
				col1 = new String[2];
				col1[0] = this.getCells().get(4).getText();
				col1[1] = this.getCells().get(5).getText();
			} else {
				col1 = new String[1];
				col1[0] = this.getCells().get(4).getText();
			}
			// Sets the name of each row.
			for (int i = 3 + ncols, j = 0; j < rows.length; i += ncols, j++) {
				rows[j] = this.cells.get(i).getText();
			}

			// Sets the type of chart to create.
			if (nCharts == 1) {
				if (nrows == 2)
					chartType = ChartType.OrangeSimple;
				else if (nrows == 6)
					chartType = ChartType.PurpleSimple;
				else if (nrows == 5)
					chartType = ChartType.GreenSimple;
				else if (nrows == 4)
					chartType = ChartType.PinkSimple;
				else
					chartType = ChartType.Other;
			} else {
				if (nrows == 2)
					chartType = ChartType.OrangeDate;
				else if (nrows == 6)
					chartType = ChartType.PurpleDate;
				else if (nrows == 5)
					chartType = ChartType.GreenDate;
				else if (nrows == 4)
					chartType = ChartType.PinkDate;
				else
					chartType = ChartType.Other;
			}

			// Chart creation.
			Chart barChart1 = new BarChart(this, col1, rows, "chart_bar_1_" + this.number, chartType, 4 + ncols, ncols);
			barChart1.createChart();
			// Generates the image.
			barChart1.createChartImage();
			imgNames.add(barChart1.getImageName());

			// If there is a second chart
			if (nCharts == 2) {
				String[] col2 = { this.getCells().get(6).getText() };

				if (nrows == 2)
					chartType = ChartType.OrangePercentage;
				else if (nrows == 6)
					chartType = ChartType.PurplePercentage;
				else if (nrows == 5)
					chartType = ChartType.GreenPercentage;
				else if (nrows == 4)
					chartType = ChartType.PinkPercentage;
				else
					chartType = ChartType.Other;

				Chart barChart2 = new BarChart(this, col2, rows, "chart_bar_2_" + this.number, chartType, 6 + ncols,
						ncols);
				barChart2.createChart();
				barChart2.createChartImage();
				imgNames.add(barChart2.getImageName());
			}

		}

		return imgNames;
	}

	/**
	 * Returns the images names generated.
	 * 
	 * @return The images names of the charts.
	 */
	public List<String> getImages() {
		List<String> charts = new ArrayList<>();
		// Both charts
		if (this.number == 5 || this.number == 7 || this.number == 9 || this.number == 11) {
			charts.add(getBarChart(1, 6).get(0));
			charts.add(getStackedChart());
		} // Only bar charts
		else {
			if (this.number == 14) {
				charts = getBarChart(1, 2);
			} else if (this.number == 15) {
				charts = getBarChart(1, 3);
			} else {
				charts = getBarChart(2, 4);
			}
		}

		return charts;
	}

}