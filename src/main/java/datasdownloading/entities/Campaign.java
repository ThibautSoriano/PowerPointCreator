package main.java.datasdownloading.entities;

import java.util.ArrayList;
import java.util.List;

import main.java.excelreader.entities.CampaignRow;

public class Campaign {

	private CampaignHeader campaignHeader = new CampaignHeader();
	
	private List<CampaignRow> campaignContent = new ArrayList<>();
	
	private CampaignRow all;
	
	private String title = "";
	
	private PeriodData monthlyData;
	
	private PeriodData weeklyData;
	
	private List<String> columsLabels;
	
	private boolean isTechnicalCampaign;

	public Campaign(CampaignHeader campaignHeader, List<CampaignRow> campaignContent, CampaignRow all) {
		this.campaignHeader = campaignHeader;
		this.campaignContent = campaignContent;
		this.all = all;
	}
	
	public Campaign() {
	}

	public CampaignHeader getCampaignHeader() {
		return campaignHeader;
	}

	public void setCampaignHeader(CampaignHeader campaignHeader) {
		this.campaignHeader = campaignHeader;
	}

	public List<CampaignRow> getCampaignContent() {
		return campaignContent;
	}

	public void setCampaignContent(List<CampaignRow> campaignContent) {
		this.campaignContent = campaignContent;
	}

    public List<String> getColumsLabels() {
        return columsLabels;
    }

    public void setColumsLabels(List<String> columsLabels) {
        this.columsLabels = columsLabels;
    }

	public CampaignRow getAll() {
		return all;
	}

	public void setAll(CampaignRow all) {
		this.all = all;
	}

	public boolean isTechnicalCampaign() {
		return isTechnicalCampaign;
	}

	public void setTechnicalCampaign(boolean isTechnicalCampaign) {
		this.isTechnicalCampaign = isTechnicalCampaign;
	}

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
