package main.java.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SaveSettings {

    private static final String settingsFile = "/preferences/settings.json";

    private static JSONObject map;

    static {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream is = classLoader.getClass()
                .getResourceAsStream(settingsFile);

        JSONParser parser = new JSONParser();

        InputStreamReader isr;

        isr = new InputStreamReader(is);

        try {
            map = (JSONObject) parser.parse(isr);
        } catch (IOException e) {

            // e.printStackTrace();
        } catch (ParseException e) {
            // e.printStackTrace();
        }
    }

    private static void saveFile() {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        try {
            FileWriter file = new FileWriter(
                    classLoader.getClass().getResource(settingsFile).getPath());
            file.write(map.toJSONString());
            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void saveLoginName(String loginName) {

        map.put("login", loginName);
        saveFile();
    }
    
    public static void changeAppLanguage(String language) {
    	map.put("language", language);
        saveFile();
    }
    
    public static String getKey(String key) {
        return (String) map.get(key);
    }

    public static boolean loginHasToBeRemembered() {
        return (Boolean) map.get("remember_login");
    }

    public static String getLogin() {
        return (String) map.get("login");
    }

    public static void changeLoginHasToBeRemembered(boolean hasToBeRemembered){
        map.put("remember_login", hasToBeRemembered);
        saveFile();
    }

    public static void main(String[] args) {
        saveLoginName("merguez");
        System.out.println(loginHasToBeRemembered());
        System.out.println(getLogin());
        changeLoginHasToBeRemembered(false);
        System.out.println(loginHasToBeRemembered());
        
    }
}
