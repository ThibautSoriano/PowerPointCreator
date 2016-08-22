package main.java.exceltopdf.pdfsections;

import java.util.ArrayList;
import java.util.List;

import main.java.datasdownloading.entities.SummaryData;

public class SummaryPage extends Section {
	
	private List<SummaryData> summary = new ArrayList<>();
	
	private boolean hasToBeDisplayed;
	
	public SummaryPage() {
		super();
	}

	public SummaryPage(List<SummaryData> summary, boolean hasToBeDisplayed) {
		super();
		this.summary = summary;
		this.hasToBeDisplayed = hasToBeDisplayed;
	}

	public List<SummaryData> getSummary() {
		return summary;
	}
	
	public void setSummary(List<SummaryData> summary) {
		this.summary = summary;
	}

	public boolean isHasToBeDisplayed() {
		return hasToBeDisplayed;
	}

	public void setHasToBeDisplayed(boolean hasToBeDisplayed) {
		this.hasToBeDisplayed = hasToBeDisplayed;
	}

}
