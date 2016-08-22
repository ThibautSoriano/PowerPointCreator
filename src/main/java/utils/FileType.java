package main.java.utils;

public enum FileType {

    EXCEL("xls"),LOGO("png");
    
   
    private String [] acceptedExtensions = null;
    
    public String [] getAcceptedExtensions() {
        return acceptedExtensions;
    }

    private FileType(String... strings ) {
        acceptedExtensions =strings;        
    }

    public String getExtensions() {
        String res = "(";
        for (int i = 0; i< acceptedExtensions.length-1;i++) {
            res += "*."+acceptedExtensions[i] +"," ;
        }
        res += "*."+acceptedExtensions[acceptedExtensions.length-1] +")" ;
       
        return res;
    }

    public String getDescription() {
        
        String res = "";
        if (this == FileType.EXCEL)
            res = Internationalization.getKey("Excel files");
        else
            res =  Internationalization.getKey("Images");
        
        return res+=" "+getExtensions();
    }
    
}
