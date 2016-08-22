package slideshow;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

/**
 * A JFreechart BarChart to represent a bar chart in the Powerpoint.
 * 
 * @author Chloé & Pierre
 */
public class BarChart extends Chart {

	private int start;
	private int increment;

	/**
	 * 
	 * @param slide
	 *            The slide which will contain the chart.
	 * @param cols
	 *            The columns names.
	 * @param rows
	 *            The rows names.
	 * @param imgName
	 *            The image name.
	 * @param chartType
	 *            The type of the chart.
	 * @param start
	 *            The index of the first cell
	 * @param increment
	 *            The number of columns in the RawSlide
	 */
	public BarChart(RawSlide slide, String[] cols, String[] rows, String imgName, ChartType chartType, int start,
			int increment) {
		super(slide, cols, rows, imgName, chartType);
		this.start = start;
		this.increment = increment;
	}

	@Override
	public void createChart() {

		// 2D table that contains the data
		double[][] tabData = new double[this.rows.length][this.columns.length];

		// Fill the table for slide 15 (special case)
		if (this.slide.getNumber() == 15) {
			for (int i = start, k = 0; k < this.columns.length; i += increment, k++) {
				// Retrieve the data from the cell
				tabData[0][k] = Double.parseDouble(slide.getCells().get(i).getText());
				tabData[1][k] = Double.parseDouble(slide.getCells().get(i + 1).getText());
			}
		} else {
			// Fill the table
			for (int i = start, k = 0; k < tabData.length; i += increment, k++) {
				for (int j = 0; j < tabData[k].length; j++) {
					try {
						// Retrieve the data from the cell
						tabData[k][j] = Double.parseDouble(slide.getCells().get(j + i).getText());
					} catch (NumberFormatException nfe) {
						// When retrieving the data, if there is a
						// NumberFormatException it's because the data is a
						// date, so
						// the date is converted to a double
						SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

						try {
							double dbldate = HSSFDateUtil
									.getExcelDate(formatter.parse(slide.getCells().get(j + i).getText()));
							tabData[k][j] = dbldate - (int) dbldate;
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}
			}

			// Invert the values if the slide is not the number 14
			double tmp = 0;
			if (this.slide.getNumber() != 14) {
				for (int i = 0; i < tabData.length / 2; i++) {
					tmp = tabData[i][0];
					tabData[i][0] = tabData[this.rows.length - 1 - i][0];
					tabData[this.rows.length - 1 - i][0] = tmp;
				}
			}
		}

		// Create the dataset used by JFreeChart
		data = DatasetUtilities.createCategoryDataset(this.rows, this.columns, tabData);

		// Create the chart
		chart = ChartFactory.createBarChart(null, null, null, data, PlotOrientation.HORIZONTAL, false, false, false);

		// Set the style of the chart
		setStyle(chart.getCategoryPlot());

	}

	@Override
	protected void setStyle(CategoryPlot plot) {
		// Remove gradient
		((BarRenderer) plot.getRenderer()).setBarPainter(new StandardBarPainter());

		// Remove the axis
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		CategoryAxis domainAxis = plot.getDomainAxis();

		if (this.chartType.equals(ChartType.GreenSimple) || this.chartType.equals(ChartType.OrangeSimple)
				|| this.chartType.equals(ChartType.PinkSimple) || this.chartType.equals(ChartType.PurpleSimple)
				|| this.slide.getNumber() == 15) {
			domainAxis.setTickLabelFont(new Font("Calibri", Font.BOLD, 22));
			domainAxis.setTickLabelPaint(Color.GRAY);
		} else {
			domainAxis.setVisible(false);
		}
		rangeAxis.setVisible(false);
		
		// Set the axis scale
		if (this.slide.getNumber() != 15) {
			double max = 0, min = Double.MAX_VALUE;
			for (int i = 0; i < plot.getDataset().getRowCount(); i++) {
				for (int j = 0; j < plot.getDataset().getColumnCount(); j++) {
					if (plot.getDataset().getValue(i, j).doubleValue() >= max)
						max = plot.getDataset().getValue(i, j).doubleValue();
					if (plot.getDataset().getValue(i, j).doubleValue() <= min)
						min = plot.getDataset().getValue(i, j).doubleValue();
				}
			}

			double scale = 0.0, upperbound = 0.0;
			if (max != 0 && (max - min) / max < 0.5) {
				scale = (max - ((max - min) * 3));
			}
			if (scale < 0 || scale == max)
				scale = 0;

			upperbound = (max - scale) * 0.5 + max;

			if (upperbound == 0)
				upperbound = 1.0;
			rangeAxis.setRange(scale, upperbound);
		}
		BarRenderer barRenderer = (BarRenderer) plot.getRenderer();

		// No margin between bars
		barRenderer.setItemMargin(0);

		// Set the labels
		if (chartType.equals(ChartType.OrangePercentage) || chartType.equals(ChartType.PurplePercentage)
				|| chartType.equals(ChartType.GreenPercentage) || chartType.equals(ChartType.PinkPercentage)
				|| chartType.equals(ChartType.Other)) {
			barRenderer.setBaseItemLabelGenerator(
					new StandardCategoryItemLabelGenerator("{2}", new DecimalFormat("#.00%")));
		} else if (chartType.equals(ChartType.OrangeDate) || chartType.equals(ChartType.PurpleDate)
				|| chartType.equals(ChartType.GreenDate) || chartType.equals(ChartType.PinkDate)) {
			barRenderer.setBaseItemLabelGenerator(
					new StandardCategoryItemLabelGenerator("{2}", new SimpleDateFormat("HH:mm:ss")));

			barRenderer.setBaseItemLabelGenerator(new CategoryItemLabelGenerator() {

				@Override
				public String generateRowLabel(CategoryDataset data, int row) {
					return null;
				}

				@Override
				public String generateLabel(CategoryDataset data, int row, int col) {
					return new SimpleDateFormat("HH:mm:ss")
							.format(HSSFDateUtil.getJavaDate((double) data.getValue(row, col)));
				}

				@Override
				public String generateColumnLabel(CategoryDataset data, int col) {
					return null;
				}
			});
		} else {
			barRenderer.setBaseItemLabelGenerator(
					new StandardCategoryItemLabelGenerator("{2}", NumberFormat.getInstance()));
		}
		barRenderer.setBaseItemLabelsVisible(true);
		barRenderer.setBaseItemLabelFont(new Font("Calibri", Font.BOLD, 20));

		// Set the color
		if (chartType.equals(ChartType.OrangeDate) || chartType.equals(ChartType.OrangePercentage)
				|| chartType.equals(ChartType.OrangeSimple)) {
			barRenderer.setSeriesPaint(0, new Color(255, 102, 9));
			barRenderer.setSeriesPaint(1, new Color(3, 149, 208));
		} else if (chartType.equals(ChartType.PurpleDate) || chartType.equals(ChartType.PurplePercentage)
				|| chartType.equals(ChartType.PurpleSimple)) {
			barRenderer.setSeriesPaint(0, new Color(64, 49, 82));
			barRenderer.setSeriesPaint(1, new Color(96, 74, 123));
			barRenderer.setSeriesPaint(2, new Color(128, 100, 162));
			barRenderer.setSeriesPaint(3, new Color(179, 162, 199));
			barRenderer.setSeriesPaint(4, new Color(204, 193, 218));
			barRenderer.setSeriesPaint(5, new Color(230, 224, 236));
		} else if (chartType.equals(ChartType.GreenDate) || chartType.equals(ChartType.GreenPercentage)
				|| chartType.equals(ChartType.GreenSimple)) {
			barRenderer.setSeriesPaint(0, new Color(81, 188, 76));
			barRenderer.setSeriesPaint(1, new Color(102, 224, 102));
			barRenderer.setSeriesPaint(2, new Color(153, 255, 102));
			barRenderer.setSeriesPaint(3, new Color(204, 255, 153));
			barRenderer.setSeriesPaint(4, new Color(204, 255, 204));
		} else if (chartType.equals(ChartType.PinkDate) || chartType.equals(ChartType.PinkPercentage)
				|| chartType.equals(ChartType.PinkSimple)) {
			barRenderer.setSeriesPaint(0, new Color(194, 20, 111));
			barRenderer.setSeriesPaint(1, new Color(255, 0, 102));
			barRenderer.setSeriesPaint(2, new Color(255, 51, 153));
			barRenderer.setSeriesPaint(3, new Color(255, 153, 204));
		} else if (this.slide.getNumber() == 15) {
			barRenderer.setSeriesPaint(0, new Color(133, 82, 161));
			barRenderer.setSeriesPaint(1, new Color(3, 153, 214));
		} else {
			barRenderer.setSeriesPaint(0, new Color(0, 62, 105));
			barRenderer.setSeriesPaint(1, new Color(0, 104, 177));
			barRenderer.setSeriesPaint(2, new Color(0, 153, 211));
		}

		// White background
		plot.setBackgroundPaint(Color.white);
		plot.setOutlineVisible(false);
	}

}
