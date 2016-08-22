package main.java.exceltopdf.pdfsections;

import main.java.exceltopdf.HeaderFooter;

public abstract class Section {
	private HeaderFooter structure;

	public HeaderFooter getStructure() {
		return structure;
	}

	public void setStructure(HeaderFooter structure) {
		this.structure = structure;
	}

}
