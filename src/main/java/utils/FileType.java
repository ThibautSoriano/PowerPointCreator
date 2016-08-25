package main.java.utils;

public enum FileType {

    EXCEL("xls","xlsx");

    private String[] acceptedExtensions = null;

    public String[] getAcceptedExtensions() {
        return acceptedExtensions;
    }

    private FileType(String... strings) {
        acceptedExtensions = strings;
    }

    public String getExtensions() {
        String res = "(";
        for (int i = 0; i < acceptedExtensions.length - 1; i++) {
            res += "*." + acceptedExtensions[i] + ",";
        }
        res += "*." + acceptedExtensions[acceptedExtensions.length - 1] + ")";

        return res;
    }

    public String getDescription() {

      
        return "Excel files " + getExtensions();
    }

}
