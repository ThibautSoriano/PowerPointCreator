package main.java.exceltopdf.pdfsections;

import java.util.List;

import main.java.datasdownloading.entities.PeriodData;
import main.java.excelreader.entities.CampaignRowPeriod;

public class PeriodTotalPage extends Section {

	private PeriodData monthlyData;
	
	private PeriodData weeklyData;
	
	private CampaignRowPeriod all;
	
	private boolean impressions;
	
	private boolean reach;
	
	private boolean frequency;
	
	private boolean clicks;
	
	private boolean clickingUsers;
	
	private boolean clickThroughRate;
	
	private boolean uniqueCTR;

	public PeriodData getMonthlyData() {
		return monthlyData;
	}

	public void setMonthlyData(PeriodData monthlyData) {
		this.monthlyData = monthlyData;
	}

	public PeriodData getWeeklyData() {
		return weeklyData;
	}

	public void setWeeklyData(PeriodData weeklyData) {
		this.weeklyData = weeklyData;
	}

	public CampaignRowPeriod getAll() {
		return all;
	}

	public void setAll(CampaignRowPeriod all) {
		this.all = all;
	}

	public boolean isImpressions() {
		return impressions;
	}

	public void setImpressions(boolean impressions) {
		this.impressions = impressions;
	}

	public boolean isReach() {
		return reach;
	}

	public void setReach(boolean reach) {
		this.reach = reach;
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
}
