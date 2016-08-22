package main.java.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Percentage {
    
    private float value;
    
    public Percentage(){
        value = 0f;
    }
    
    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public Percentage(float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        
        return String.valueOf(df.format(value*100))+"%";
    }
    
    
    

    
    
}
