package main.java.ppthandler;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

public class PPTReader {

    XMLSlideShow ppt;

    public PPTReader(InputStream inputStream) {


        
        

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
            int slideNumber, Color highlight,double fontSize) {
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
                        r.setFontSize(fontSize);
                        r.setFontFamily("Arial");
                        if (s.matches("\\[@.*\\]")) {
                            s = s.replaceAll("[]@\\[]", "");
                            if (datasToPutInSlide.containsKey(s))
                                s = datasToPutInSlide.get(s);
                            else
                                s = "<[@"+s+"]>";
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

    public void placePNGImages(List<BufferedImage> images, int slideNumber)
            throws IOException {
        XSLFSlide slide = ppt.getSlides().get(slideNumber);

        List<XSLFTextShape> shapes = getImagesTextShapesInOrder(slideNumber);

        int i = 0;
        for (XSLFTextShape textShape : shapes) {

            for (XSLFTextParagraph xslfTextParagraph : textShape
                    .getTextParagraphs()) {

                // removing text from the shapes
                for (XSLFTextRun xslfTextRun : xslfTextParagraph) {
                    xslfTextRun.setText("");
                }

                BufferedImage image = null;
                try {
                    
                    image = images.get(i++);
                } catch (Exception e) {
                    return;
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "png", baos);
               
                
                baos.flush();
                byte[] picture = baos.toByteArray();
                baos.close();

                PictureData idx = ppt.addPicture(picture,
                        PictureData.PictureType.PNG);

                XSLFPictureShape pic = slide.createPicture(idx);

                pic.setAnchor(new Rectangle(
                        new Double(textShape.getAnchor().getX()).intValue(),
                        new Double(textShape.getAnchor().getY()).intValue(),
                        image.getWidth(), image.getHeight()));

            }
        }

    }

    private List<XSLFTextShape> getImagesTextShapesInOrder(int slideNumber) {
        XSLFSlide slide = ppt.getSlides().get(slideNumber);

        List<XSLFShape> shapes = slide.getShapes();

        List<XSLFTextShape> l = new ArrayList<>(shapes.size());

        for (XSLFShape shape : shapes) {

            if (shape instanceof XSLFTextShape) {
                XSLFTextShape textShape = (XSLFTextShape) shape;

                for (XSLFTextParagraph xslfTextParagraph : textShape
                        .getTextParagraphs()) {
                    String text = (xslfTextParagraph.getText());

                    if (text.contains("@img")) {
                        String position = text.replaceAll("(@img| )", "");
                        l.add(textShape);

                    }
                }
            }
        }
        
        
        l.sort(new Comparator<XSLFTextShape>() {

            @Override
            public int compare(XSLFTextShape o1, XSLFTextShape o2) {
                return o1.getTextParagraphs().get(0).getText().compareTo(o2.getTextParagraphs().get(0).getText());
            }
        });

        return l;
    }

    // public static void main(String [] args){
    // System.out.println(Integer.parseInt(" 1 "));
    // }
}
