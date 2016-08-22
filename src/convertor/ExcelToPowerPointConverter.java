package convertor;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.SwingWorker;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFAutoShape;
import org.apache.poi.xslf.usermodel.XSLFGroupShape;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTable;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import slideshow.CellData;
import slideshow.RawSlide;
import slideshow.Slideshow;

/**
 * The conversion class that handle the Excel extraction and the Powerpoint
 * creation. Extends SwingWorker to be run in a different thread (necessary for
 * the progressbar).
 * 
 * @author Chloé & Pierre
 */
public class ExcelToPowerPointConverter extends SwingWorker<Void, Void> {
	/**
	 * The Apache POI object used to manipulate the PPT.
	 */
	private XMLSlideShow ppt;
	/**
	 * The slides of the PPT.
	 */
	private XSLFSlide[] slides;
	/**
	 * List used to store the raw data extracted from the Excel file.
	 */
	private ArrayList<RawSlide> datas;
	/**
	 * The name of the PPT created.
	 */
	private String newFileName;

	/**
	 * 
	 * @param pptPath
	 *            The path of the PPT template.
	 * @param excelPath
	 *            The path of the Excel file.
	 * @param newPptname
	 *            The new name for the PPT generated.
	 */
	public ExcelToPowerPointConverter(String pptPath, String excelPath, String newPptname) {
		try {
			ppt = new XMLSlideShow(new FileInputStream(pptPath));
			slides = ppt.getSlides();
			datas = getDatas(excelPath);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		newFileName = newPptname;
	}

	/**
	 * Add an image in a specific slide in the PPT file.
	 * 
	 * @param path
	 *            The image path.
	 * @param xPosition
	 *            The position on the x axis in the slide.
	 * @param yPosition
	 *            The position on the y axis in the slide.
	 * @param width
	 *            The width of the image in the slide.
	 * @param heigh
	 *            The height of the image in the slide.
	 * @param slideNumber
	 *            The slide number.
	 */
	public void addImage(String path, double xPosition, double yPosition, double width, double heigh, int slideNumber) {
		try {
			File image = new File(path);
			byte[] picture = IOUtils.toByteArray(new FileInputStream(image));
			int idx = ppt.addPicture(picture, XSLFPictureData.PICTURE_TYPE_PNG);
			XSLFPictureShape pic = slides[slideNumber].createPicture(idx);
			Rectangle2D rec = pic.getAnchor();
			rec.setFrame(xPosition, yPosition, width, heigh);
			pic.setAnchor(rec);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Extracts all the data from the Excel file and put them in a list of
	 * RawSlides.
	 * 
	 * @param dataPath:
	 *            the Excel file where the informations are
	 * @return a list of all the data slide by slide
	 */
	public ArrayList<RawSlide> getDatas(String dataPath) {
		try {
			// Getting the first sheet of the Excel file.
			FileInputStream file = new FileInputStream(new File(dataPath));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet spreadsheet = workbook.getSheetAt(0);

			ArrayList<RawSlide> slides = new ArrayList<>();
			RawSlide slide = null;
			CellData cellData = null;
			XSSFRow row;
			String name = "[Name]";
			Boolean nextHasName = false;

			// Reading the Excel file (from left to right starting from the
			// top).
			Iterator<Row> rowIterator = spreadsheet.iterator();
			while (rowIterator.hasNext()) {
				row = (XSSFRow) rowIterator.next();

				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					// Retrieving the data of the current cell.
					Cell cell = cellIterator.next();
					cellData = new CellData(cell);

					// Handles the "name" macro.
					if (cellData.getText().toLowerCase().equals("ru"))
						nextHasName = false;

					if (nextHasName) {
						nextHasName = false;
						name = cellData.getText();
					}

					// If the current cell is a new slide, adds the previous
					// slide to the list and creates a new one.
					if (slide != null && cellData.getText().toLowerCase().contains("slide")
							&& !cellData.getText().equals(slide.getTitle()) && !cellData.getText().equals("")) {
						slides.add(slide);
						slide = new RawSlide(cellData.getText());
					}

					if (slide == null)
						slide = new RawSlide(cellData.getText());
					// Add a cell to the slide if the cell is not empty.
					if (cellData.getText() != "")
						slide.addCellData(cellData);

					if (cellData.getText().toLowerCase().equals("[name]"))
						cellData.setText(name);
					if (cellData.getText().toLowerCase().equals("name")) {
						nextHasName = true;
					}
				}
			}
			slides.add(slide);
			workbook.close();
			file.close();
			return slides;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		}
	}

	/**
	 * Creates the Powerpoint file with the data extracted from the Excel file.
	 */
	public void convert() {
		// Slide n°1, replace the macro [name] by its value.
		String newTitle = slides[0].getTitle().replace("[NAME]", datas.get(0).getCells().get(2).getText());
		XSLFTextShape titleArea = slides[0].getPlaceholder(0);
		titleArea.setText(newTitle);
		// Used for the progress bar (adds 5% to the progress)
		setProgress(getProgress() + 2);

		
		// Slide n°3, replace all the macros by their values. Parsing used to
		// show the data with a proper format.

		XSLFTextShape shape1= ((XSLFAutoShape) ((XSLFGroupShape) slides[2].getShapes()[1]).getShapes()[1]);
		String newShape1= shape1.getText().replace("[Reach%1]", datas.get(1).getCells().get(13).getText());
		shape1.setText(newShape1);
		shape1.getTextParagraphs().get(0).getTextRuns().get(0).setFontSize(14);
		setProgress(getProgress() + 1);
		XSLFTextShape shape2 = ((XSLFAutoShape) ((XSLFGroupShape) slides[2].getShapes()[2]).getShapes()[1]);
		String newShape2= shape2.getText().replace("[minutes1]", Integer.toString((int) Double.parseDouble(datas.get(1).getCells().get(15).getText()))).replace("[seconds1]", Integer.toString((int) Double.parseDouble(datas.get(1).getCells().get(16).getText())));
		shape2.setText(newShape2);
		shape2.getTextParagraphs().get(0).getTextRuns().get(0).setFontSize(14);
		setProgress(getProgress() + 1);
		XSLFTextShape shape3 = ((XSLFAutoShape) ((XSLFGroupShape) slides[2].getShapes()[3]).getShapes()[1]);
		String newShape3= shape3.getText().replace("[Date1]", datas.get(1).getCells().get(7).getText()).replace("[Name1]", datas.get(1).getCells().get(8).getText()).replace("[RU number1]", Integer.toString((int) Double.parseDouble(datas.get(1).getCells().get(9).getText())));
		shape3.setText(newShape3);
		shape3.getTextParagraphs().get(0).getTextRuns().get(0).setFontSize(14);
		setProgress(getProgress() + 1);
		
		XSLFTextShape shape4= ((XSLFAutoShape) ((XSLFGroupShape) slides[2].getShapes()[5]).getShapes()[1]);
		String newShape4= shape4.getText().replace("[Reach%2]", datas.get(1).getCells().get(14).getText());
		shape4.setText(newShape4);
		shape4.getTextParagraphs().get(0).getTextRuns().get(0).setFontSize(14);
		setProgress(getProgress() + 1);
		XSLFTextShape shape5 = ((XSLFAutoShape) ((XSLFGroupShape) slides[2].getShapes()[6]).getShapes()[1]);
		String newShape5= shape5.getText().replace("[minutes2]", Integer.toString((int) Double.parseDouble(datas.get(1).getCells().get(17).getText()))).replace("[seconds2]", Integer.toString((int) Double.parseDouble(datas.get(1).getCells().get(18).getText())));
		shape5.setText(newShape5);
		shape5.getTextParagraphs().get(0).getTextRuns().get(0).setFontSize(14);
		setProgress(getProgress() + 1);
		XSLFTextShape shape6 = ((XSLFAutoShape) ((XSLFGroupShape) slides[2].getShapes()[4]).getShapes()[1]);
		String newShape6= shape6.getText().replace("[Date2]", datas.get(1).getCells().get(10).getText()).replace("[Name2]", datas.get(1).getCells().get(11).getText()).replace("[RU number2]", Integer.toString((int) Double.parseDouble(datas.get(1).getCells().get(12).getText())));
		shape6.setText(newShape6);
		shape6.getTextParagraphs().get(0).getTextRuns().get(0).setFontSize(14);
		setProgress(getProgress() + 1);
		setProgress(getProgress() + 2); // 10% total
		
		
//		XSLFTextShape firstArea = (XSLFTextShape) (slides[2].getShapes()[3]);
//		String newFirstArea = firstArea.getText().replace("[Date]", datas.get(1).getCells().get(4).getText())
//				.replace("[Name]", datas.get(1).getCells().get(5).getText()).replace("[RU number]",
//						Integer.toString((int) Double.parseDouble(datas.get(1).getCells().get(6).getText())));
//		firstArea.setText(newFirstArea);
//		
//		XSLFTextShape secondArea = (XSLFTextShape) (slides[2].getShapes()[5]);
//		String newSecondArea;
//		if (datas.get(1).getCells().get(7).getText().equals("0.0"))
//			newSecondArea= secondArea.getText().replace("[Reach%]","0%");
//		else {
//			DecimalFormat dfaa = new DecimalFormat("#0.00%");
//			newSecondArea = secondArea.getText().replace("[Reach%]", dfaa.format(Double.parseDouble(datas.get(1).getCells().get(7).getText())));
//		}
//		secondArea.setText(newSecondArea);
//		XSLFTextShape thirdArea = (XSLFTextShape) (slides[2].getShapes()[4]);
//		String newThirdArea = thirdArea.getText()
//				.replace("[minutes]",
//						Integer.toString((int) Double.parseDouble(datas.get(1).getCells().get(8).getText())))
//				.replace("[seconds]",
//						Integer.toString((int) Double.parseDouble(datas.get(1).getCells().get(9).getText())));
//		thirdArea.setText(newThirdArea);

		
	      
		// Adding the charts generated to the slides.
		// Slide n°5
		Slideshow s = new Slideshow(datas);
		List<String> images = s.getSlideByNumber(5).getImages();
		addImage("img\\" + images.get(0) + ".png", 70, 110, 340, 226.7, 4);
		setProgress(getProgress() + 4);
		addImage("img\\" + images.get(1) + ".png", 398, 110, 320, 260, 4);
		setProgress(getProgress() + 4);

		// Slide n°6
		images = s.getSlideByNumber(6).getImages();
		addImage("img\\" + images.get(0) + ".png", 70, 110, 340, 226.7, 5);
		setProgress(getProgress() + 4);
		addImage("img\\" + images.get(1) + ".png", 398, 110, 340, 226.7, 5);
		setProgress(getProgress() + 4);

		// Slide n°7
		images = s.getSlideByNumber(7).getImages();
		addImage("img\\" + images.get(0) + ".png", 70, 110, 300, 226.7, 6);
		setProgress(getProgress() + 4);
		addImage("img\\" + images.get(1) + ".png", 380, 100, 340, 260, 6);
		setProgress(getProgress() + 4);

		// Slide n°8
		images = s.getSlideByNumber(8).getImages();
		addImage("img\\" + images.get(0) + ".png", 70, 110, 340, 226.7, 7);
		setProgress(getProgress() + 4);
		addImage("img\\" + images.get(1) + ".png", 398, 110, 340, 226.7, 7);
		setProgress(getProgress() + 4);

		// Slide n°9
		images = s.getSlideByNumber(9).getImages();
		addImage("img\\" + images.get(0) + ".png", 70, 110, 340, 226.7, 8);
		setProgress(getProgress() + 4);
		addImage("img\\" + images.get(1) + ".png", 398, 110, 320, 260, 8);
		setProgress(getProgress() + 4);

		// Slide n°10
		images = s.getSlideByNumber(10).getImages();
		addImage("img\\" + images.get(0) + ".png", 70, 110, 340, 226.7, 9);
		setProgress(getProgress() + 4);
		addImage("img\\" + images.get(1) + ".png", 398, 110, 340, 226.7, 9);
		setProgress(getProgress() + 4);

		// Slide n°11
		images = s.getSlideByNumber(11).getImages();
		addImage("img\\" + images.get(0) + ".png", 70, 110, 340, 226.7, 10);
		setProgress(getProgress() + 4);
		addImage("img\\" + images.get(1) + ".png", 398, 100, 320, 260, 10);
		setProgress(getProgress() + 4);

		// Slide n°12
		images = s.getSlideByNumber(12).getImages();
		addImage("img\\" + images.get(0) + ".png", 70, 110, 340, 226.7, 11);
		setProgress(getProgress() + 4);
		addImage("img\\" + images.get(1) + ".png", 398, 110, 340, 226.7, 11);
		setProgress(getProgress() + 4);

		// Slide n°13, get the table and fill it with the new data.
		DecimalFormat df = new DecimalFormat("#0.00%");
		XSLFTable tab1 = (XSLFTable) (slides[12].getShapes()[1]);

		for (int i = 7, j = 1; i < 12; i++, j++) {
			Double res = Double.parseDouble(datas.get(10).getCells().get(i).getText());
			tab1.getRows().get(1).getCells().get(j).setText(df.format(res));
			tab1.getRows().get(1).getCells().get(j).setLeftInset(30);
		}
		for (int i = 13, j = 1; i < 18; i++, j++) {
			Double res = Double.parseDouble(datas.get(10).getCells().get(i).getText());
			tab1.getRows().get(2).getCells().get(j).setText(df.format(res));
			tab1.getRows().get(2).getCells().get(j).setLeftInset(30);
		}
		setProgress(getProgress() + 4);
		
		XSLFTable tab2 = (XSLFTable) (slides[12].getShapes()[0]);
		for (int i = 25, j = 1; i < 30; i++, j++) {
			Double res = Double.parseDouble(datas.get(10).getCells().get(i).getText());
			tab2.getRows().get(1).getCells().get(j).setText(df.format(res));
			tab2.getRows().get(1).getCells().get(j).setLeftInset(30);
		}
		for (int i = 31, j = 1; i < 36; i++, j++) {
			Double res = Double.parseDouble(datas.get(10).getCells().get(i).getText());
			tab2.getRows().get(2).getCells().get(j).setText(df.format(res));
			tab2.getRows().get(2).getCells().get(j).setLeftInset(30);
		}
		setProgress(getProgress() + 4);

		// Slide n°14, adds the chart to the slide
		images = s.getSlideByNumber(14).getImages();
		addImage("img\\" + images.get(0) + ".png", 350, 110, 360, 230, 13);
		setProgress(getProgress() + 4);
		
		// Slide n°15, adds the chart to the slide
		images = s.getSlideByNumber(15).getImages();
		addImage("img\\" + images.get(0) + ".png", 70, 110, 600, 226.7, 14);
		setProgress(getProgress() + 4);

		// Saving the work in a new Powerpoint file
		try {
			File file = new File(newFileName);
			FileOutputStream out = new FileOutputStream(file);
			ppt.write(out);
			out.close();
			setProgress(getProgress() + 10);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Done!");
	}

	// SwingWorker methods that runs the conversion.
	@Override
	protected Void doInBackground() throws Exception {

		convert();

		return null;
	}
}
