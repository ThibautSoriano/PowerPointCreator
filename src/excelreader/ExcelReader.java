package excelreader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
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

    public List<Triplet> getDataForChartSlides8_11(int slideNumber) {
        List<Triplet> l = new ArrayList<>();
        if (slideNumber<8 || slideNumber>11)
            return l;
        
        XSSFSheet s = workbook.getSheetAt(0);
        
        int iInit = 101+(slideNumber-8)*10 ; //+1 because rows start at 0 and +1 to be in cell 102 if slide == 8
        int jInit = 1;
        
        
        for(int i = iInit;i<iInit+2;i++){
            int j = jInit;
            Cell c = s.getRow(i).getCell(j);
            while( c != null && c.getCellType() != Cell.CELL_TYPE_BLANK) {
                l.add(new Triplet(s.getRow(i).getCell(0).getStringCellValue(),s.getRow(iInit-1).getCell(j).getStringCellValue(),c.getNumericCellValue()));
                c = s.getRow(i).getCell(++j);
                
            }
        }
        
        iInit+=4;
        for(int i = iInit;i<iInit+2;i++){
            int j = jInit;
            Cell c = s.getRow(i).getCell(j);
            while( c != null && c.getCellType() != Cell.CELL_TYPE_BLANK) {
                l.add(new Triplet(s.getRow(i).getCell(0).getStringCellValue(),s.getRow(iInit-1).getCell(j).getStringCellValue(),c.getNumericCellValue()));
                c = s.getRow(i).getCell(++j);
                
            }
        }
        
        
        
        
        
        return l;
    
    }
    

}
