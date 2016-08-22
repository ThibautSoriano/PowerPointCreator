package main.java.excelreader;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import main.java.datasdownloading.entities.Campaign;
import main.java.excelreader.entities.CampaignRow;
import main.java.utils.Percentage;


public abstract class ExcelReader {

	protected Campaign campaign;
    
	protected String type = "";
	
	

    protected HSSFSheet sheet;
    
    protected DocumentStructure documentStructure;
    

    /**
     * Fill the excelSheet attribute with data from the excel file
     */
    public abstract void fillExcelSheet(String filePath);
    
    /**
     * @return the ExcelSheet with fields filled in
     */
    public Campaign readExcelSheet(String filePath) {
        
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            HSSFWorkbook workbook = new HSSFWorkbook(fis);

            // reading the first sheet (there is only one)
            sheet = workbook.getSheetAt(0);

            fillExcelSheet(filePath); 

            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return campaign;
    }
    
    
    public void readStartDate() {
        HSSFRow row = sheet.getRow(documentStructure.getDatesRow());
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");

        String dates = row.getCell(documentStructure.getDatesCol())
                .getStringCellValue();

        String[] s = dates.split(" ");
        
        try {
			campaign.getCampaignHeader().setStartDate(f.parse(s[0].replaceAll("-", "/")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
    }

    public void readEndDate() {
        HSSFRow row = sheet.getRow(documentStructure.getDatesRow());
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");

        String dates = row.getCell(documentStructure.getDatesCol())
                .getStringCellValue();

        String[] s = dates.split(" ");

        try {
			campaign.getCampaignHeader().setEndDate(f.parse(s[2].replaceAll("-", "/")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
    }
    
    
    public List<String> getColumsLabels() {
        List<String> labels = new ArrayList<String>();
        
        HSSFRow row = sheet.getRow(documentStructure.getLabelsRow());
        
        int index = documentStructure.getLabelsCol();
        while (true) {
            Cell next = row.getCell(index);
            if (next ==  null)
                break;
            labels.add(next.getStringCellValue());
            index++;
            
        }
        return labels;
    }
    
    public void readCampaignRows() {
        int index = documentStructure.getCampaignRowStartRow();

        // we iterate and if we see a placement name = "All " then we break
        // and yes, there is a blank space after "All"
        while (true) {
            System.out.println("Line " + (index + 1) + " ...");
            HSSFRow row = sheet.getRow(index);
            String firstColumnData = row.getCell(documentStructure.getFirstColumnLabelCol())
                    .getStringCellValue();
            int impressions = 0;
            if (row.getCell(documentStructure.getImpressionsCol())
                    .getCellType() == Cell.CELL_TYPE_NUMERIC) {
                impressions = (int) row.getCell(documentStructure.getImpressionsCol())
                        .getNumericCellValue();
            }
            int uniqueCookies = 0;
            if (row.getCell(documentStructure.getUniquesCookiesCol())
                    .getCellType() == Cell.CELL_TYPE_NUMERIC) {
                uniqueCookies = (int) row
                        .getCell(documentStructure.getUniquesCookiesCol())
                        .getNumericCellValue();
            }
            float frequency = 0;
            if (row.getCell(documentStructure.getFrequencyCol())
                    .getCellType() == Cell.CELL_TYPE_NUMERIC) {
                frequency = (float) row.getCell(documentStructure.getFrequencyCol())
                        .getNumericCellValue();
            }
            int clicks = 0;
            if (row.getCell(documentStructure.getClicksCol())
                    .getCellType() == Cell.CELL_TYPE_NUMERIC) {
                clicks = (int) row.getCell(documentStructure.getClicksCol())
                        .getNumericCellValue();
            }
            int clickingUsers = 0;
            if (row.getCell(documentStructure.getClickingUsersCol())
                    .getCellType() == Cell.CELL_TYPE_NUMERIC) {
                clickingUsers = (int) row
                        .getCell(documentStructure.getClickingUsersCol())
                        .getNumericCellValue();
            }
            
            Percentage clickThroughRatePercentage = new Percentage();
            if (row.getCell(documentStructure.getClickThroughRateCol())
                    .getCellType() == Cell.CELL_TYPE_NUMERIC) {
                float clickThroughRate = (float) row
                        .getCell(documentStructure.getClickThroughRateCol())
                        .getNumericCellValue();
                clickThroughRatePercentage.setValue(clickThroughRate);
            }
            
            Percentage uniqueCTRPercentage = new Percentage();
            if (row.getCell(documentStructure.getUniqueCtrCol())
                    .getCellType() == Cell.CELL_TYPE_NUMERIC) {
                float uniqueCTR = (float) row.getCell(documentStructure.getUniqueCtrCol())
                        .getNumericCellValue();
                uniqueCTRPercentage.setValue(uniqueCTR);
            }

            CampaignRow cr = new CampaignRow(firstColumnData, impressions,
                    frequency, clicks, clickingUsers,
                    clickThroughRatePercentage, uniqueCTRPercentage);
            cr.setUniqueCookies(uniqueCookies);

            
            HSSFRow nextRow = sheet.getRow(index+1);
            
            if (nextRow ==  null)
            {
                campaign.setAll(cr);
                System.out.println("Done !");
                break;
            }
                    
            else {
                campaign.getCampaignContent().add(cr);
                index++;
            }
        }
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
    
}
