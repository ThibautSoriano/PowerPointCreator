package main.java.excelreader;

import java.io.File;

import main.java.datasdownloading.entities.Campaign;

public class ExcelReaderTechnical extends ExcelReader{
    
    public ExcelReaderTechnical() {
        campaign = new Campaign();
        campaign.setTechnicalCampaign(true);
        documentStructure = new DocumentStructureTechnical();
    }

    @Override
    public void fillExcelSheet(String filePath) {
        
        campaign.getCampaignHeader().setCampaignName(new File(filePath).getName().split("[%,]")[0]);
        readStartDate();
        readEndDate();
        super.readCampaignRows();
        campaign.setColumsLabels(super.getColumsLabels());
    }
    
}
