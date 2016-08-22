package main.java.exceltopdf.pdfsections;

import main.java.datasdownloading.entities.Campaign;
import main.java.excelreader.entities.ExcelSheet;

public class ContentPage extends Section {
	public static final int NO_CHART = 0;
	
	public static final int PIE_CHART = 1;
	
	public static final int BAR_CHART = 2;
	
	private int chartType;
	
	private ExcelSheet excelSheet;
	
	private Campaign campaign;
	
	private boolean impressions;
	
	private boolean reach;
	
	private boolean uniqueCookies;
	
	private boolean frequency;
	
	private boolean clicks;
	
	private boolean clickingUsers;
	
	private boolean clickThroughRate;
	
	private boolean uniqueCTR;
	
	private boolean weekly;
	
	private boolean monthly;
	
	private boolean general;

	public ContentPage(boolean impressions, boolean frequency, boolean clicks,
			boolean clickingUsers, boolean clickThroughRate, boolean uniqueCTR) {
		this.impressions = impressions;
		this.frequency = frequency;
		this.clicks = clicks;
		this.clickingUsers = clickingUsers;
		this.clickThroughRate = clickThroughRate;
		this.uniqueCTR = uniqueCTR;
	}

	public ExcelSheet getExcelSheet() {
		return excelSheet;
	}

	public void setExcelSheet(ExcelSheet excelSheet) {
		this.excelSheet = excelSheet;
	}

	public boolean isImpressions() {
		return impressions;
	}

	public void setImpressions(boolean impressions) {
		this.impressions = impressions;
	}

	public boolean isUniqueCookies() {
		return uniqueCookies;
	}

	public void setUniqueCookies(boolean uniqueCookies) {
		this.uniqueCookies = uniqueCookies;
	}

	public boolean isFrequency() {
		return frequency;
	}

	public void setFrequency(boolean frequency) {
		this.frequency = frequency;
	}

	public boolean isClicks() {
		return clicks;
	}

	public void setClicks(boolean clicks) {
		this.clicks = clicks;
	}

	public boolean isClickingUsers() {
		return clickingUsers;
	}

	public void setClickingUsers(boolean clickingUsers) {
		this.clickingUsers = clickingUsers;
	}

	public boolean isClickThroughRate() {
		return clickThroughRate;
	}

	public void setClickThroughRate(boolean clickThroughRate) {
		this.clickThroughRate = clickThroughRate;
	}

	public boolean isUniqueCTR() {
		return uniqueCTR;
	}

	public void setUniqueCTR(boolean uniqueCTR) {
		this.uniqueCTR = uniqueCTR;
	}

	public boolean isReach() {
		return reach;
	}

	public void setReach(boolean reach) {
		this.reach = reach;
	}

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

	public boolean isWeekly() {
		return weekly;
	}

	public void setWeekly(boolean weekly) {
		this.weekly = weekly;
	}

	public boolean isMonthly() {
		return monthly;
	}

	public void setMonthly(boolean monthly) {
		this.monthly = monthly;
	}

	public boolean isGeneral() {
		return general;
	}

	public void setGeneral(boolean general) {
		this.general = general;
	}

	public int getChartType() {
		return chartType;
	}

	public void setChartType(int chartType) {
		this.chartType = chartType;
	}

}
