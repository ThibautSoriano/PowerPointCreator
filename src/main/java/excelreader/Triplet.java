package main.java.excelreader;

public class Triplet {
    
    private String x,y;
    
    private double value;

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    public double getValue() {
        return value;
    }

    public Triplet(String x, String y, double value) {
        super();
        this.x = x;
        this.y = y;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Triplet [x=" + x + ", y=" + y + ", value=" + value + "]";
    }

    

    
    
    

}
