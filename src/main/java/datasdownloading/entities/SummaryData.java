package main.java.datasdownloading.entities;

public class SummaryData {
	
	private String attribution;
	
	private String value;
	
	public SummaryData() {
		super();
	}

	public SummaryData(String attribution, String value) {
		super();
		this.attribution = attribution;
		this.value = value;
	}

	public String getAttribution() {
		return attribution;
	}

	public void setAttribution(String attribution) {
		this.attribution = attribution;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
