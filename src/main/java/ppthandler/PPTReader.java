package main.java.ppthandler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

public class PPTReader {

    String fileName;
    XMLSlideShow ppt;

    public PPTReader(String fileName) {

        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        XMLSlideShow ppt;

        try {
            ppt = new XMLSlideShow(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        for (XSLFSlide slide : ppt.getSlides()) {
            System.out.println(slide.getTitle());
        }
    }

    
    
}
