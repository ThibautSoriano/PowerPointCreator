package main.java.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class InternationalizationPDF {
    
    private Language lang;
    
    private  JSONObject map;
    
    public InternationalizationPDF(Language lang) {
        this.lang = lang;
        
        JSONParser parser = new JSONParser();
        
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream is = classLoader.getClass().getResourceAsStream("/languages/pdf/language_pdf"+lang.getExtension()+".json");
        
        
        InputStreamReader isr;
        
            try {
                isr = new InputStreamReader(is,lang.getEncoding());
                map = (JSONObject) parser.parse(isr);
            } catch (IOException | ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }
    
    
    
    public String getDateFromTo(Date from, Date to) {
        
        String res= (String)  map.get("date");
        SimpleDateFormat f = new SimpleDateFormat(lang.getDateFormat());
        res = res.replace("<from>",f.format(from) );
        res = res.replace("<to>", f.format(to));
        
        return res;
        
    }

    
    
    
    
    public static void main(String [] args){
        InternationalizationPDF pute = new InternationalizationPDF(Language.EN);
        System.out.println(pute.getDateFromTo(new Date(System.currentTimeMillis()),new Date(System.currentTimeMillis()) ));
    }
}
