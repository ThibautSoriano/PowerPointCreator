package main.java.exceltopdf.pdfsections;

public class TitlePage extends Section {
	
	private String campaignName ="";
	
//	private String startDate = "";
//	
//	private String endDate= "";
	
	private String date ="";
	
	private String belowTitle = "online kamp�ny elemz�se";
	
	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	

	public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBelowTitle() {
		return belowTitle;
	}

	public void setBelowTitle(String belowTitle) {
		this.belowTitle = belowTitle;
	}
}
