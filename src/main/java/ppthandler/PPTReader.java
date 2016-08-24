package main.java.ppthandler;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

public class PPTReader {

    String fileName;
    XMLSlideShow ppt;

    public PPTReader(String fileName) {

        this.fileName = fileName;

        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try {
            ppt = new XMLSlideShow(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // for (XSLFSlide slide : ppt.getSlides()) {
        // System.out.println(slide.getTitle());
        // }

        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<XSLFSlide> getSlides() {
        return ppt.getSlides();
    }

    public void fillSlideTextValues(Map<String, String> datasToPutInSlide,
            int slideNumber, Color highlight) {
        XSLFSlide slide = ppt.getSlides().get(slideNumber);

        List<XSLFShape> shapes = slide.getShapes();

        for (XSLFShape shape : shapes) {

            if (shape instanceof XSLFTextShape) {
                XSLFTextShape textShape = (XSLFTextShape) shape;

                for (XSLFTextParagraph xslfTextParagraph : textShape
                        .getTextParagraphs()) {
                    String text = (xslfTextParagraph.getText());

                    if (!text.contains("<["))
                        continue;

                    for (XSLFTextRun xslfTextRun : xslfTextParagraph) {
                        xslfTextRun.setText("");
                    }

                    String[] vals = text.split("[<>]");

                    for (String s : vals) {
                        XSLFTextRun r = xslfTextParagraph.addNewTextRun();
                        r.setFontSize(14d);
                        r.setFontFamily("Arial");
                        if (s.matches("\\[@.*\\]")) {
                            s = s.replaceAll("[]@\\[]", "");
                            if (datasToPutInSlide.containsKey(s))
                                s = datasToPutInSlide.get(s);
                            r.setFontColor(highlight);
                            r.setBold(true);
                        } else {
                            r.setBold(false);
                        }
                        r.setText(s);

                    }

                }

            }
        }

    }

    public void save(String fileName) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            ppt.write(fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void close() {
        try {
            ppt.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public void placePNGImage(String imagePath, int slideNumber)
            throws FileNotFoundException, IOException {
        XSLFSlide slide = ppt.getSlides().get(slideNumber);

        File image = new File(imagePath);

        if (!image.exists())
            return;

        byte[] picture = null;
        try {
            FileInputStream fis = new FileInputStream(image);
            picture = IOUtils.toByteArray(fis);
            // fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //
        PictureData idx = ppt.addPicture(picture, PictureData.PictureType.PNG);

        XSLFPictureShape pic = slide.createPicture(idx);
        pic.setAnchor(new Rectangle(100, 100, 300, 300));

    }

    public void merguez() {
        XSLFSlide slide = ppt.getSlides().get(2);

        List<XSLFShape> shapes = slide.getShapes();

        for (XSLFShape shape : shapes) {

            if (shape instanceof XSLFTextShape) {
                XSLFTextShape textShape = (XSLFTextShape) shape;

                for (XSLFTextParagraph xslfTextParagraph : textShape
                        .getTextParagraphs()) {
                    String text = (xslfTextParagraph.getText());

                    
                    if (text.contains("@img")) {
                        
                        System.out.println(textShape.getAnchor());
                    
                    }
                }

            }
        }
    }

    // public static void main(String [] args){
    // String text = "aa-zz";
    // text = text.replaceAll("<\\[[a-z]*\\]>", "merg");
    // System.out.println(text);
    // }
}
