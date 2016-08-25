
package main.java.gui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import main.java.chartcreator.ImageCreator;
import main.java.excelreader.ExcelReader;
import main.java.excelreader.Triplet;
import main.java.ppthandler.PPTReader;
import main.java.utils.Utils;

public class Main {
    
    public static void main (String[] args) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        PPTReader ppt = new PPTReader(classLoader.getClass().getResource("/ppt/EmptyTemplate.pptx").getPath());

        ExcelReader er = new ExcelReader(classLoader.getClass().getResource("/gmr_templ_2016.xlsx").getPath());
 
        ImageCreator merguez = new ImageCreator(); //for charts
        
        
        
        //slide 2
        ppt.fillSlideTextValues(er.getSecondSlideData(),1,new Color(0, 176, 240));
 
        
        //slide 3 
        List<BufferedImage> l = new ArrayList<>();
        l.add(merguez.getDoubleValueChart((er.getDataForChartTopLeftSlides3_4(3)),false)); //top left chart
        l.add(merguez.getPcMobilTabletChart(er.getDataForStackedChartSlide3_4(3, true),false,true)); // top right chart
        l.add(merguez.getDoubleValueChart((er.getDataForChartBottmLeftSlides3_4(3)),true)); //bottom left chart
        l.add(merguez.getPcMobilTabletChart(er.getDataForStackedChartSlide3_4(3, false),true,true)); // bottom right chart
        
        ppt.placePNGImages(l,2);
        
        
      //slide 4 
        l.clear();
        l.add(merguez.getDoubleValueChart((er.getDataForChartTopLeftSlides3_4(4)),false)); //top left chart
        l.add(merguez.getPcMobilTabletChart(er.getDataForStackedChartSlide3_4(4, true),false,true)); // top right chart
        l.add(merguez.getDoubleValueChart((er.getDataForChartBottmLeftSlides3_4(4)),true)); //bottom left chart
        l.add(merguez.getPcMobilTabletChart(er.getDataForStackedChartSlide3_4(4, false),true,true)); // bottom right chart
        
        ppt.placePNGImages(l,3);
        
        
//        slides 5-10
        l.clear();
        boolean percentage = false;
        for (int i = 4; i< 10 ;i++) {
            
            l.add(merguez.getAllAgesChart(er.getDataForBarChartsSlide5_10(i+1),percentage));
            l.add(merguez.getPcMobilTabletChart(er.getDataForStackedChartSlide5_10(i+1),percentage,false));
        
            percentage = !percentage;
            ppt.placePNGImages(l,i);
            l.clear();
            
        }
        
        
        
        //slides 11 - 14 (on the ppt, 8-11 is for the excel)
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
            
            ppt.fillSlideTextValues(dataToPutInSlide, i+2, new Color(0, 112, 192));
        
            System.out.println(triplets.subList(0, 3));
            BufferedImage img = merguez.getTop3BarChart(triplets.subList(0, 3));
           
            List<BufferedImage> li = new ArrayList<>();
            li.add(img);
            
            ppt.placePNGImages(li,i+2);
            
            
        }

      
        
        
        
        ppt.save(Utils.getFileName("dias","pptx"));
        ppt.close();
        er.close();
        
    }

}
