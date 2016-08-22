package main.java.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Internationalization {
    
    private static JSONObject map;
    private static Language currentLanguage = Language.EN;
    
    static {
        loadLanguage(Language.valueOf(SaveSettings.getKey("language")));        
    }
    

    public static void main(String []args)
    {
        System.out.println(Internationalization.map.get("Browse"));
    }
    
    
    public static Language getCurrentLanguage(){
        return currentLanguage;
    }
    
    
    public static void loadLanguage(Language lang){
        JSONParser parser = new JSONParser();
        
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream is = classLoader.getClass().getResourceAsStream("/languages/app/language"+lang.getExtension()+".json");
        
        
        
        InputStreamReader isr;
        
        

        try {
            isr = new InputStreamReader(is,lang.getEncoding());
            map = (JSONObject) parser.parse(isr);
            currentLanguage = lang;

        
        } catch (UnsupportedEncodingException e) {
            
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            
            e.printStackTrace(); 
        } catch (IOException e) {
            
            e.printStackTrace();
        } catch (ParseException e) {
           
            e.printStackTrace();
        }
    }
    
    public static String getKey(String key) {
        if ( map.get(key) != null) 
            return (String) map.get(key);
        return "???";
    }
    
}
