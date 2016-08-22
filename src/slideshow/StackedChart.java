package slideshow;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.annotations.CategoryTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;

/**
 * A JFreechart StackedChart to represent a stacked chart with links in the
 * Powerpoint.
 * 
 * @author Chloé & Pierre
 */
public class StackedChart extends Chart {

	/**
	 * 
	 * @param slide
	 *            The slide which will contains the chart.
	 * @param cols
	 *            The columns names.
	 * @param rows
	 *            The rows names.
	 * @param imgName
	 *            The image name.
	 * @param chartType
	 *            The type of the chart.
	 */
	public StackedChart(RawSlide slide, String[] cols, String[] rows, String imgName, ChartType chartType) {
		super(slide, cols, rows, imgName, chartType);
	}

	@Override
	public void createChart() {

		// 2D table that contains the data
		double[][] tabData = new double[this.rows.length][this.columns.length];

		// Fill the table
		for (int i = 13, k = 0; k < tabData.length; i += 6, k++) {
			for (int j = 0; j < tabData[k].length; j++) {
				tabData[k][j] = Double.parseDouble(slide.getCells().get(j + i).getText());
			}
		}

		// Create the dataset used by JFreeChart
		data = DatasetUtilities.createCategoryDataset(this.rows, this.columns, tabData);

		// Create the chart
		chart = ChartFactory.createStackedBarChart(null, null, null, data, PlotOrientation.VERTICAL, false, false,
				false);

		// Create the links between the blocs
		createLinks(tabData, chart.getCategoryPlot());

		// Set the style of the chart
		setStyle(chart.getCategoryPlot());
	}

	@Override
	protected void setStyle(CategoryPlot plot) {
		// Remove gradient
		((BarRenderer) plot.getRenderer()).setBarPainter(new StandardBarPainter());
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		DecimalFormat pctFormat = new DecimalFormat("#%");
		// Set the number format to a percentage
		rangeAxis.setNumberFormatOverride(pctFormat);
		// Set the range (0% to 100%)
		rangeAxis.setRange(0, 1);
		// Set the axis color to black
		rangeAxis.setAxisLinePaint(Color.black);
		BarRenderer barRenderer = (BarRenderer) plot.getRenderer();

		// Set the chart color depending on the chart type
		if (this.chartType.equals(ChartType.OrangePercentage)) {
			barRenderer.setSeriesPaint(1, new Color(255, 102, 9));
			barRenderer.setSeriesPaint(0, new Color(3, 149, 208));
		} else if (this.chartType.equals(ChartType.PurplePercentage)) {
			barRenderer.setSeriesPaint(5, new Color(64, 49, 82));
			barRenderer.setSeriesPaint(4, new Color(96, 74, 123));
			barRenderer.setSeriesPaint(3, new Color(128, 100, 162));
			barRenderer.setSeriesPaint(2, new Color(179, 162, 199));
			barRenderer.setSeriesPaint(1, new Color(204, 193, 218));
			barRenderer.setSeriesPaint(0, new Color(230, 224, 236));
		} else if (this.chartType.equals(ChartType.GreenPercentage)) {
			barRenderer.setSeriesPaint(4, new Color(81, 188, 76));
			barRenderer.setSeriesPaint(3, new Color(102, 224, 102));
			barRenderer.setSeriesPaint(2, new Color(153, 255, 102));
			barRenderer.setSeriesPaint(1, new Color(204, 255, 153));
			barRenderer.setSeriesPaint(0, new Color(204, 255, 204));
		} else if (this.chartType.equals(ChartType.PinkPercentage)) {
			barRenderer.setSeriesPaint(3, new Color(194, 20, 111));
			barRenderer.setSeriesPaint(2, new Color(255, 0, 102));
			barRenderer.setSeriesPaint(1, new Color(255, 51, 153));
			barRenderer.setSeriesPaint(0, new Color(255, 153, 204));
		}

		// Set the labels format, the color and the font
		barRenderer
				.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", new DecimalFormat("#.00%")));
		barRenderer.setBaseItemLabelsVisible(true);
		barRenderer.setBaseItemLabelPaint(Color.white);
		barRenderer.setBaseItemLabelFont(new Font("Calibri", Font.BOLD, 20));

		// Set the X axis labels style
		plot.getDomainAxis().setTickLabelFont(new Font("Calibri", Font.BOLD, 15));
		// Set the Y axis labels style
		rangeAxis.setTickLabelFont(new Font("Calibri", Font.PLAIN, 15));

		// White background and no outline
		plot.setBackgroundPaint(Color.white);
		plot.setOutlineVisible(false);
	}

	/**
	 * Creates the links between the blocs of the bars. Each link is a new
	 * dataset added to the plot.
	 * 
	 * @param tabData
	 *            The data
	 * @param plot
	 *            The plot
	 */
	private void createLinks(double[][] tabData, CategoryPlot plot) {
		double sum1 = 0.0, sum2 = 0.0, diff;
		for (int i = 0; i < tabData.length; i++) {
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			sum1 += tabData[i][0];
			sum2 += tabData[i][1];

			// If the sum of one bar is more than 1 (== 100%, because of the
			// Double precision), sets the sum to 1
			if (sum1 >= 1 || sum2 >= 1) {
				sum1 = 1.0;
				sum2 = 1.0;
			}

			// sum1 >= sum2 : creates a link with the left point in the upper
			// part of the left bar and the right point in the lower part of the
			// right bar.
			if (sum1 >= sum2) {
				diff = sum1 - sum2;
				dataset.addValue(sum1 + diff, this.rows[i], this.columns[0]);
				dataset.addValue(sum2 - diff, this.rows[i], this.columns[1]);
			// Or else, same as before but inverted
			} else {
				diff = sum2 - sum1;
				dataset.addValue(sum1 - diff, this.rows[i], this.columns[0]);
				dataset.addValue(sum2 + diff, this.rows[i], this.columns[1]);
			}
			// Set the new line to the plot
			plot.setDataset(i + 1, dataset);
			final CategoryItemRenderer renderer = new LineAndShapeRenderer(true, false);
			renderer.setSeriesPaint(0, new Color(0, 0, 0));
			renderer.setSeriesPaint(1, new Color(0, 0, 0));
			plot.setRenderer(i + 1, renderer);

			// Annotations to replace the missing labels
			NumberFormat perc = NumberFormat.getPercentInstance();
			perc.setMinimumFractionDigits(2);
			// Handles the left bar
			if (tabData[i][0] < 0.0586) {
				CategoryTextAnnotation annot;
				if (i == 0) {
					annot = new CategoryTextAnnotation(perc.format(tabData[i][0]), this.columns[0], 0.017);
					annot.setPaint(Color.black);
				} else if (i == tabData.length - 1) {
					annot = new CategoryTextAnnotation(perc.format(tabData[i][0]), this.columns[0], sum1 - 0.017);
					annot.setPaint(Color.white);
				} else {
					annot = new CategoryTextAnnotation(perc.format(tabData[i][0]), this.columns[0], sum1 - 0.015);
					annot.setPaint(Color.white);
				}
				annot.setFont(new Font("Calibri", Font.BOLD, 20));
				plot.addAnnotation(annot);
			}
			// Handles the right bar
			if (tabData[i][1] < 0.0586) {
				CategoryTextAnnotation annot;
				if (i == 0) {
					annot = new CategoryTextAnnotation(perc.format(tabData[i][1]), this.columns[1], 0.017);
					annot.setPaint(Color.black);
				} else if (i == tabData.length - 1) {
					annot = new CategoryTextAnnotation(perc.format(tabData[i][1]), this.columns[1], sum2 - 0.017);
					annot.setPaint(Color.white);
				} else {
					annot = new CategoryTextAnnotation(perc.format(tabData[i][1]), this.columns[1], sum2 - 0.017);
					annot.setPaint(Color.white);
				}
				annot.setFont(new Font("Calibri", Font.BOLD, 20));
				plot.addAnnotation(annot);
			}
		}
	}

}
