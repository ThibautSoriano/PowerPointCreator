package main.java.utils;

import java.io.File;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class Utils {

    public static String getFileName(String campaignName,String extension) {

        String res = deAccent(campaignName);
        res = res.replaceAll(" ", "_");
        res = res.replaceAll("[^a-zA-Z0-9_\\-\\.]+", "");

        return getNewFileBaseName(res, extension, 0);

    }

    public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str,
                Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    public static String getNewFileBaseName(String fileName, String extension,
            int copy) {
        String name = fileName;
        if (copy == 0)
            name += "." + extension;
        else
            name += "_" + copy + "." + extension;

        if (new File(name).exists())
            return getNewFileBaseName(fileName, extension, copy + 1);

        return name;

    }
    
}
