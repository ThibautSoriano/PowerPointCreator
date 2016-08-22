package main.java.datasdownloading.entities;

public class TocElement {

	private String chapter;
	
	private String docToConcat;

	public TocElement(String chapter, String docToConcat) {
		this.chapter = chapter;
		this.docToConcat = docToConcat;
	}

	public String getChapter() {
		return chapter;
	}

	public void setChapter(String chapter) {
		this.chapter = chapter;
	}

	public String getDocToConcat() {
		return docToConcat;
	}

	public void setDocToConcat(String docToConcat) {
		this.docToConcat = docToConcat;
	}
}
