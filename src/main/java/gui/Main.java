package main.java.gui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import excelreader.ExcelReader;
import main.java.ppthandler.PPTReader;
import utils.Utils;

public class Main {
    
    public static void main (String[] args) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        PPTReader a = new PPTReader(classLoader.getClass().getResource("/ppt/EmptyTemplate.pptx").getPath());
        
//        Map<String,String> merg = new HashMap<String,String>();
//        merg.put("date","dias");
//        merg.put("mobil","dos");
//        merg.put("tablet","santos");
        
        
        
        ExcelReader er = new ExcelReader(classLoader.getClass().getResource("/gmr_templ_2016.xlsx").getPath());
        
        
        
        a.fillSecondSlide(er.getSecondSlideData());
        
        a.save(Utils.getFileName("dias","pptx"));
        a.close();
        er.close();
        
    }

}
