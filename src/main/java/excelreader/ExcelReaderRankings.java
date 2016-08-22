package main.java.excelreader;

import org.apache.poi.hssf.usermodel.HSSFRow;

import main.java.datasdownloading.entities.Campaign;

public class ExcelReaderRankings extends ExcelReader {
    
    @Override
    public void fillExcelSheet(String filePath) {
        readCampaignName();
        super.readStartDate();
        super.readEndDate();
        super.readCampaignRows();
        campaign.setColumsLabels(super.getColumsLabels());

    }

    public ExcelReaderRankings() {
       campaign = new Campaign();
       campaign.setTechnicalCampaign(false);
       documentStructure = new DocumentStructureRankings();
    }
    
    public void readCampaignName() {

        HSSFRow row = sheet.getRow( ((DocumentStructureRankings) documentStructure).getCampaignNameRow());
        
        campaign.getCampaignHeader().setCampaignName(
                row.getCell(((DocumentStructureRankings) documentStructure).getCampaignNameCol())
                        .getStringCellValue());
        
    }
    
    
    
    
}
