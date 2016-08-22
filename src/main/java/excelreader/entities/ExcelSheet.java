package main.java.excelreader.entities;

import java.util.ArrayList;
import java.util.List;

public class ExcelSheet {

    private String campaignName;

    private String startDate;

    private String endDate;

    private List<CampaignRow> campaignRows;

    private CampaignRow all;
    
    private List<String> columsLabels;

    public List<String> getColumsLabels() {
        return columsLabels;
    }

    public void setColumsLabels(List<String> columsLabels) {
        this.columsLabels = columsLabels;
    }

    public void setAll(CampaignRow all) {
        this.all = all;
    }

    public ExcelSheet() {
        campaignRows = new ArrayList<>();

    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<CampaignRow> getCampaignRows() {
        return campaignRows;
    }

    public void setCampaignRows(List<CampaignRow> campaignRows) {
        this.campaignRows = campaignRows;
    }

    public CampaignRow getAll() {
        return all;
    }

    @Override
    public String toString() {
        String res = "";

        res += "Campaign name : " + campaignName + "\n";
        res += "Start date : " + startDate + "\n";
        res += "End date : " + endDate + "\n";
        res += "Campaigns rows : " + campaignRows + "\n";
        res += "All ads sump up : " + all + "\n";

        return res;
    }

    
   
}
