package slideshow;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.CategoryDataset;

/**
 * A JFreechart chart that allows to create a chart and convert it into an
 * image.
 * 
 * @author Chloé & Pierre
 */
public abstract class Chart {
	/**
	 * The data structure used by JFreeChart.
	 */
	protected CategoryDataset data;
	/**
	 * The JFreeChart object.
	 */
	protected JFreeChart chart;
	/**
	 * The columns names.
	 */
	protected String[] columns;
	/**
	 * The rows names.
	 */
	protected String[] rows;
	/**
	 * The slide containing the data.
	 */
	protected RawSlide slide;
	/**
	 * The chart type.
	 */
	protected ChartType chartType;
	/**
	 * The image name.
	 */
	private String imageName;

	public Chart(RawSlide slide, String[] cols, String[] rows, String imgName, ChartType chartType) {
		this.slide = slide;
		this.columns = cols;
		this.rows = rows;
		this.imageName = imgName;
		this.chartType = chartType;
	}

	/**
	 * Creates the chart.
	 */
	public abstract void createChart();

	/**
	 * Sets the style of the chart.
	 * 
	 * @param plot
	 *            The plot of the chart.
	 */
	protected abstract void setStyle(CategoryPlot plot);

	/**
	 * Convert the JFreechart chart to a png image.
	 */
	public void createChartImage() {

		BufferedImage objBufferedImage = getBufferedImage();
		ByteArrayOutputStream bas = new ByteArrayOutputStream();
		try {
			ImageIO.write(objBufferedImage, "png", bas);
		} catch (IOException e) {
			e.printStackTrace();
		}

		byte[] byteArray = bas.toByteArray();

		InputStream in = new ByteArrayInputStream(byteArray);
		BufferedImage image;
		try {
			image = ImageIO.read(in);
			File outputfile = new File("img\\" + imageName + ".png");
			ImageIO.write(image, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private BufferedImage getBufferedImage() {
		BufferedImage bufImg = null;

		// The size of the image is different if its a bar chart or a stacked
		// chart.
		if (this instanceof BarChart) {
			if(this.slide.getNumber() == 15)
				bufImg = chart.createBufferedImage(1500, 427);
			else
				bufImg = chart.createBufferedImage(540, 427);
		} else {
			bufImg = chart.createBufferedImage(520, 460);
		}

		return bufImg;
	}

	/**
	 * Returns the image name.
	 * 
	 * @return The image name.
	 */
	public String getImageName() {
		return imageName;
	}
}
