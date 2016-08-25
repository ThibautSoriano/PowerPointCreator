
package main.java.gui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.UIManager;

import main.java.chartcreator.ImageCreator;
import main.java.excelreader.ExcelReader;
import main.java.excelreader.Triplet;
import main.java.ppthandler.PPTReader;
import main.java.utils.Utils;

public class Main {

    public static void main(String[] args) throws IOException {
        
        try {
            // set for file chooser look
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        new MainWindow();
    }

    

}
