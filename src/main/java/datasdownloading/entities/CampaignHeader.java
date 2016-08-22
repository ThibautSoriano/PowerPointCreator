package main.java.datasdownloading.entities;

import java.util.Date;

public class CampaignHeader {
	
	private String campaignID;
	
	private String campaignName;
	
	private String clientName;
	
	private CampaignStatus campaignStatus;
	
	private Date creationDate;
	
	private Date startDate;
	
	private Date endDate;
	
	public CampaignHeader() {
		
	}
	
	public CampaignHeader(String campaignID, String campaignName, String clientName, CampaignStatus campaignStatus, Date creationDate,
			Date startDate, Date endDate) {
		this.campaignID = campaignID;
		this.campaignName = campaignName;
		this.clientName = clientName;
		this.campaignStatus = campaignStatus;
		this.creationDate = creationDate;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public CampaignStatus getCampaignStatus() {
		return campaignStatus;
	}

	public void setCampaignStatus(CampaignStatus campaignStatus) {
		this.campaignStatus = campaignStatus;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getCampaignID() {
		return campaignID;
	}

	public void setCampaignID(String campaignID) {
		this.campaignID = campaignID;
	}
}
