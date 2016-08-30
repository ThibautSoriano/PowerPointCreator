package main.java.excelreader;

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

    public Map<String, String> getSecondSlideData() {
        Map<String, String> map = new HashMap<>();
        XSSFSheet s = workbook.getSheetAt(0);
        map.put("date", s.getRow(12).getCell(0).getStringCellValue());
        map.put("ru", s.getRow(12).getCell(2).getRawValue());
        map.put("mobil", s.getRow(12).getCell(3).getRawValue());
        map.put("tablet", s.getRow(12).getCell(4).getRawValue());
        map.put("pc", s.getRow(12).getCell(5).getRawValue());
        map.put("percentage",
                s.getRow(13).getCell(0).getNumericCellValue() * 100 + "%");
        map.put("grp", s.getRow(14).getCell(0).getRawValue());

        return map;
    }

    public List<Triplet> getDataForChartSlides8_11(int slideNumber) {
        List<Triplet> l = new ArrayList<>();
        if (slideNumber < 8 || slideNumber > 11)
            return l;

        XSSFSheet s = workbook.getSheetAt(0);

        int iInit = 101 + (slideNumber - 8) * 10; // +1 because rows start at 0
                                                  // and +1 to be in cell 102 if
                                                  // slide == 8
        int jInit = 1;

        for (int i = iInit; i < iInit + 2; i++) {
            int j = jInit;
            Cell c = s.getRow(i).getCell(j);
            while (c != null && c.getCellType() != Cell.CELL_TYPE_BLANK) {
                l.add(new Triplet(s.getRow(i).getCell(0).getStringCellValue(),
                        s.getRow(iInit - 1).getCell(j).getStringCellValue(),
                        c.getNumericCellValue()));
                c = s.getRow(i).getCell(++j);

            }
        }

        iInit += 4;
        for (int i = iInit; i < iInit + 2; i++) {
            int j = jInit;
            Cell c = s.getRow(i).getCell(j);
            while (c != null && c.getCellType() != Cell.CELL_TYPE_BLANK) {
                l.add(new Triplet(s.getRow(i).getCell(0).getStringCellValue(),
                        s.getRow(iInit - 1).getCell(j).getStringCellValue(),
                        c.getNumericCellValue()));
                c = s.getRow(i).getCell(++j);

            }
        }

        return l;

    }

    public List<Triplet> getDataForChartTopLeftSlides3_4(int slideNumber) {
        List<Triplet> l = new ArrayList<>();
        if (slideNumber < 3 || slideNumber > 4)
            return l;

        XSSFSheet s = workbook.getSheetAt(0);

        int iInit = 22 + 11 * (slideNumber - 3);

        l.add(new Triplet(s.getRow(iInit).getCell(0).getStringCellValue(),
                s.getRow(iInit - 2).getCell(1).getStringCellValue(),
                s.getRow(iInit).getCell(1).getNumericCellValue()));
        l.add(new Triplet(s.getRow(iInit+1).getCell(0).getStringCellValue(),
                s.getRow(iInit - 2).getCell(1).getStringCellValue(),
                s.getRow(iInit+1).getCell(1).getNumericCellValue()));
        
        

        return l;
    }

    public List<Triplet> getDataForChartBottmLeftSlides3_4(int slideNumber) {
        List<Triplet> l = new ArrayList<>();
        if (slideNumber < 3 || slideNumber > 4)
            return l;

        XSSFSheet s = workbook.getSheetAt(0);

        int iInit = 26 + 11 * (slideNumber - 3);

        
        l.add(new Triplet(s.getRow(iInit).getCell(0).getStringCellValue(),
                s.getRow(iInit - 2).getCell(1).getStringCellValue(),
                s.getRow(iInit).getCell(1).getNumericCellValue()));
        
        l.add(new Triplet(s.getRow(iInit+1).getCell(0).getStringCellValue(),
                s.getRow(iInit - 2).getCell(1).getStringCellValue(),
                s.getRow(iInit+1).getCell(1).getNumericCellValue()));

        return l;
    }

    public List<Triplet> getDataForStackedChartSlide3_4(int slideNumber,
            boolean top) {
        if (top) {
            if (slideNumber == 3) {
                return getDataForStackedChartSlide3_10(22, 2, 2, 2);
            } else if (slideNumber == 4) {
                return getDataForStackedChartSlide3_10(33, 2, 2, 2);
            }
        }

        if (slideNumber == 3) {
            return getDataForStackedChartSlide3_10(26, 2, 2, 2);
        } else if (slideNumber == 4) {
            return getDataForStackedChartSlide3_10(37, 2, 2, 2);
        }

        return new ArrayList<>();

    }

    public List<Triplet> getDataForStackedChartSlide5_10(int slideNumber) {
        int iInit = 0, jInit = 0, lineNumber = 0, topLegendOffsetFromiInit = 0;

        if (slideNumber == 5) {
            iInit = 45;
            jInit = 2;
            lineNumber = 6;
            topLegendOffsetFromiInit = 1;
        } else if (slideNumber == 6) {
            iInit = 54;
            jInit = 2;
            lineNumber = 6;
            topLegendOffsetFromiInit = 1;
        } else if (slideNumber == 7) {
            iInit = 65;
            jInit = 2;
            lineNumber = 5;
            topLegendOffsetFromiInit = 2;
        } else if (slideNumber == 8) {
            iInit = 73;
            jInit = 2;
            lineNumber = 5;
            topLegendOffsetFromiInit = 2;
        } else if (slideNumber == 9) {
            iInit = 85;
            jInit = 2;
            lineNumber = 4;
            topLegendOffsetFromiInit = 2;
        } else if (slideNumber == 10) {
            iInit = 92;
            jInit = 2;
            lineNumber = 4;
            topLegendOffsetFromiInit = 2;
        }

        return getDataForStackedChartSlide3_10(iInit, jInit, lineNumber,
                topLegendOffsetFromiInit);
    }

    public List<Triplet> getDataForBarChartsSlide5_10(int slideNumber) {

        int iInit = 0, lineNumber = 0, topLegendOffsetFromiInit = 0;

        if (slideNumber == 5) {
            iInit = 45;

            lineNumber = 6;
            topLegendOffsetFromiInit = 1;
        } else if (slideNumber == 6) {
            iInit = 54;
            lineNumber = 6;
            topLegendOffsetFromiInit = 1;
        } else if (slideNumber == 7) {
            iInit = 65;
            lineNumber = 5;
            topLegendOffsetFromiInit = 2;
        } else if (slideNumber == 8) {
            iInit = 73;
            lineNumber = 5;
            topLegendOffsetFromiInit = 2;
        } else if (slideNumber == 9) {
            iInit = 85;
            lineNumber = 4;
            topLegendOffsetFromiInit = 2;
        } else if (slideNumber == 10) {
            iInit = 92;
            lineNumber = 4;
            topLegendOffsetFromiInit = 2;
        }

        XSSFSheet s = workbook.getSheetAt(0);

        List<Triplet> l = new ArrayList<>();

        for (int i = iInit; i < iInit + lineNumber; i++) {

            Cell c = s.getRow(i).getCell(1);

            l.add(new Triplet(s.getRow(i).getCell(0).getStringCellValue(),
                    s.getRow(iInit - topLegendOffsetFromiInit).getCell(1)
                            .getStringCellValue(),
                    c.getNumericCellValue()));

            c = s.getRow(i).getCell(1);

        }

        return l;
    }

    private List<Triplet> getDataForStackedChartSlide3_10(int iInit, int jInit,
            int lineNumber, int topLegendOffsetFromiInit) {
        XSSFSheet s = workbook.getSheetAt(0);
        List<Triplet> l = new ArrayList<>();

        for (int i = iInit; i < iInit + lineNumber; i++) {
            int j = jInit;
            Cell c = s.getRow(i).getCell(j);
            while (c != null && c.getCellType() != Cell.CELL_TYPE_BLANK) {
                l.add(new Triplet(s.getRow(i).getCell(0).getStringCellValue(),
                        s.getRow(iInit - topLegendOffsetFromiInit).getCell(j)
                                .getStringCellValue(),
                        c.getNumericCellValue()));
                c = s.getRow(i).getCell(++j);

            }
        }
        return l;
    }

}
