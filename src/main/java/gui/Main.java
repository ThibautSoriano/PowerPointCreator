package main.java.gui;

import main.java.ppthandler.PPTReader;

public class Main {
    
    public static void main (String[] args) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        PPTReader a = new PPTReader(classLoader.getClass().getResource("/ppt/EmptyTemplate.pptx").getPath());
        
    }

}
