
package main.java.gui;

import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import excelreader.ExcelReader;
import excelreader.Triplet;
import main.java.chartcreator.ImageCreator;
import main.java.ppthandler.PPTReader;
import main.java.utils.Utils;

public class Main {
    
    public static void main (String[] args) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        PPTReader ppt = new PPTReader(classLoader.getClass().getResource("/ppt/EmptyTemplate.pptx").getPath());

        ExcelReader er = new ExcelReader(classLoader.getClass().getResource("/gmr_templ_2016.xlsx").getPath());
 
        ppt.fillSlideTextValues(er.getSecondSlideData(),1,new Color(0, 176, 240));
 
        
        ImageCreator merguez = new ImageCreator();
        //replace text keys
        for (int i = 8; i< 12 ;i++) {
            List<Triplet> triplets =  er.getDataForChartSlides8_11(i);
            triplets.sort(new Comparator<Triplet>() {

                @Override
                public int compare(Triplet arg0, Triplet arg1) {
                    
                    return Double.compare(arg1.getValue(), arg0.getValue());
                }
                
            });
            
            Map<String,String> dataToPutInSlide = new HashMap<>();
            
            dataToPutInSlide.put("first",triplets.get(0).getX()+", "+triplets.get(0).getY());
            dataToPutInSlide.put("second",triplets.get(1).getX()+", "+triplets.get(1).getY());
            dataToPutInSlide.put("third",triplets.get(2).getX()+", "+triplets.get(2).getY());
            
            ppt.fillSlideTextValues(dataToPutInSlide, i+3, new Color(0, 112, 192));
        
        
            Image img = merguez.getTop3BarChart(triplets.subList(0, 2));
            
        }

      
        
        List<String> list = new ArrayList<>();
        list.add("merguez.png");
        list.add("merguez.png");
        
        ppt.placePNGImages(list,2);
        
        
        ppt.save(Utils.getFileName("dias","pptx"));
        ppt.close();
        er.close();
        
    }

}
