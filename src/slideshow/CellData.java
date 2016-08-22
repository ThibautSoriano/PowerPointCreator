package slideshow;

import org.apache.poi.ss.usermodel.Cell;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;

/**
 * The data of an Excel cell.
 * 
 * @author Chloé & Pierre
 */
public class CellData {
	/**
	 * The data of the cell.
	 */
	private String text;
	/**
	 * The style of the cell (not used)
	 */
	private XSSFCellStyle style;
	/**
	 * Coordinate x.
	 */
	private int x;
	/**
	 * Coordinate y.
	 */
	private int y;

	public CellData() {
		this.text = null;
		this.style = null;
	}

	/**
	 * Creates a new CellData with a Apache POI Excell Cell.
	 * 
	 * @param cell
	 *            The Excel Cell.
	 */
	public CellData(Cell cell) {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
			// If the contents of the cell is a numeric value (double), parse it
			// into a string.
			this.text = Double.toString(cell.getNumericCellValue());
			break;
		case Cell.CELL_TYPE_STRING:
			this.text = cell.getStringCellValue();
			break;
		default:
			this.text = "";
			break;
		}
		this.style = (XSSFCellStyle) cell.getCellStyle();
		this.x = cell.getRowIndex();
		this.y = cell.getColumnIndex();
	}

	/**
	 * Returns the hexadecimal value of the cell color.
	 * 
	 * @return The hexadecimal value of the cell color.
	 */
	public String getHexaColor() {
		String color = ExcelColors.XLWhite.toString();
		if (style.getFillForegroundXSSFColor() != null) {
			color = style.getFillForegroundXSSFColor().getARGBHex();
			color = color.substring(2);
		}

		return color;
	}

	public String getText() {
		return text;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setText(String txt) {
		text = txt;
	}
}
