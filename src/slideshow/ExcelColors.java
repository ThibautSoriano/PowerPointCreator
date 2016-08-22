package slideshow;

/**
 * Represents the colors found in the Excel file.
 * 
 * @author Chloé & Pierre
 */
public enum ExcelColors {

	XLWhite("FFFFFF"), XLBlue("4BACC6"), XLOrange("F79646");

	private String color;

	private ExcelColors(String color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return color;
	}
}
