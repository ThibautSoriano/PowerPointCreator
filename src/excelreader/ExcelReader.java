package excelreader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class ExcelReader {

    XSSFWorkbook workbook;

    public ExcelReader(String filePath) throws IOException {

        FileInputStream fis = null;

        fis = new FileInputStream(filePath);
        workbook = new XSSFWorkbook(fis);
        fis.close();

    }

    public void close() {
        try {
            workbook.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
    
    public Map<String,String> getSecondSlideData() {
        Map<String,String> map = new HashMap<>();
        XSSFSheet s = workbook.getSheetAt(0);
        map.put("date", s.getRow(12).getCell(0).getStringCellValue());
        map.put("ru", s.getRow(12).getCell(2).getRawValue());
        map.put("mobil", s.getRow(12).getCell(3).getRawValue());
        map.put("tablet", s.getRow(12).getCell(4).getRawValue());
        map.put("pc", s.getRow(12).getCell(5).getRawValue());
        map.put("percentage", s.getRow(13).getCell(0).getNumericCellValue()*100+"%");
        map.put("grp", s.getRow(14).getCell(0).getRawValue());
        
        return map;
    }
    

}
