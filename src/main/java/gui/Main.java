package main.java.gui;

import java.util.HashMap;
import java.util.Map;

import main.java.ppthandler.PPTReader;
import utils.Utils;

public class Main {
    
    public static void main (String[] args) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        PPTReader a = new PPTReader(classLoader.getClass().getResource("/ppt/EmptyTemplate.pptx").getPath());
        
        Map<String,String> merg = new HashMap<String,String>();
        merg.put("date","dias");
        merg.put("mobil","dos");
        merg.put("tablet","santos");
        a.fillSecondSlide(merg);
        
        a.save(Utils.getFileName("dias","pptx"));
        a.close();
        
    }

}
