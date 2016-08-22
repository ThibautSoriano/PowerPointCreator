package main.java.excelreader.entities;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import main.java.utils.Percentage;

public  class CampaignRow {
    
    public static final int MAX_COLUMNS = 9;
    
    public static final int IMPRESSIONS_INDEX = 0;
    
    public static final int UNIQUE_COOKIES_INDEX = 1;
    
    public static final int FREQUENCY_INDEX = 2;
    
    public static final int CLICKS_INDEX = 3;
    
    public static final int CLICKING_USERS_INDEX = 4;
    
    public static final int CLICK_THROUGH_RATE_INDEX = 5;
    
    public static final int UNIQUE_CTR_INDEX = 6;

	public static final int REACH_INDEX = 7;
    
    protected String firstColumnData;

    protected int impressions;
    
    protected int reach;

	protected int uniqueCookies = -1;

    protected float frequency;

    protected int clicks;

    protected int clickingUsers;

    /**
     * percentage : 0,01 for example
     */
    protected Percentage clickThroughRate = new Percentage(0);

    /**
     * percentage
     */
    protected Percentage uniqueCTR = new Percentage(0);

    
   
    public String getFirstColumnData() {
        return firstColumnData;
    }

    public void setFirstColumnData(String firstColumnData) {
        this.firstColumnData = firstColumnData;
    }

    public CampaignRow(String firstColumnData, int impressions, float frequency, int clicks,
            int clickingUsers, Percentage clickThroughRate, Percentage uniqueCTR) {
	    this.firstColumnData = firstColumnData;
	    this.impressions = impressions;
	    this.frequency = frequency;
	    this.clicks = clicks;
	    this.clickingUsers = clickingUsers;
	    this.clickThroughRate = clickThroughRate;
	    this.uniqueCTR = uniqueCTR;
    }
    
    public CampaignRow() {
    }
    
    public int getImpressions() {
        return impressions;
    }

    public void setImpressions(int impressions) {
        this.impressions = impressions;
    }
    
    public int getReach() {
		return reach;
	}

	public void setReach(int reach) {
		this.reach = reach;
	}

    public int getUniqueCookies() {
        return uniqueCookies;
    }

    public void setUniqueCookies(int uniqueCookies) {
        this.uniqueCookies = uniqueCookies;
    }

    public float getFrequency() {
        return frequency;
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public int getClickingUsers() {
        return clickingUsers;
    }

    public void setClickingUsers(int clickingUsers) {
        this.clickingUsers = clickingUsers;
    }

    public Percentage getClickThroughRate() {
        return clickThroughRate;
    }

    public void setClickThroughRate(Percentage clickThroughRate) {
        this.clickThroughRate = clickThroughRate;
    }

    public Percentage getUniqueCTR() {
        return uniqueCTR;
    }

    public void setUniqueCTR(Percentage uniqueCTR) {
        this.uniqueCTR = uniqueCTR;
    }

    
    public String getLabelFirstColumn() {
        return firstColumnData;
    }

    public void setLabelFirstColumn(String labelFirstColumn) {
        this.firstColumnData = labelFirstColumn;
    }
    
    @Override
    public String toString() {
            String res = "";
            
            res += "\n";
            res += firstColumnData + "\t";
            res += impressions + "\t";
            res += uniqueCookies + "\t";
            res += reach + "\t";
            res += frequency + "\t";
            res += clicks + "\t";
            res += clickingUsers + "\t";
            res += clickThroughRate + "\t";
            res += uniqueCTR;
            
            return res;
    }
    
    public List<String> toList() {
        List<String> l = new ArrayList<String>();
        l.add(firstColumnData);
        l.add(getSpacesBetweenThousands(String.valueOf(impressions)));
    	l.add(getSpacesBetweenThousands(String.valueOf(uniqueCookies)));
        
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        
        
        l.add(String.valueOf(df.format(frequency)));
        l.add(getSpacesBetweenThousands(String.valueOf(clicks)));
        l.add(getSpacesBetweenThousands(String.valueOf(clickingUsers)));
        l.add(clickThroughRate.toString());
        l.add(uniqueCTR.toString());
        l.add(getSpacesBetweenThousands(String.valueOf(reach)));
        
        return l;
    }
    
    public boolean isEmpty(){
        return  impressions == 0 && (uniqueCookies == 0 || uniqueCookies == -1) && frequency == 0 && clicks == 0 && clickingUsers == 0
                && clickThroughRate.getValue() == 0 && uniqueCTR.getValue() == 0 && reach == 0;
    }
    
    public static String getSpacesBetweenThousands(String numberInString){
        
        
        StringBuilder sb = new StringBuilder(numberInString);
        for (int i = sb.length() - 3; i > 0; i -= 3)
            sb.insert(i, ' ');
       return sb.toString();
       
    }
    
    
    
    public List<Float> toListFloat() {
        List<Float> l = new ArrayList<>();
        l.add((float) impressions);
        l.add((float) uniqueCookies);
        l.add(frequency);
        l.add((float) clicks);
        l.add((float) clickingUsers);
        l.add(clickThroughRate.getValue());
        l.add(uniqueCTR.getValue());
        l.add((float) reach);
        
        return l;
    }
    
    public boolean isRelevant() {
        return  (!"Unrecognized".equals(firstColumnData)) && !isEmpty();
    }
    
    public static List<CampaignRow> sortBy(List<CampaignRow> listToSort, final int index) {
    	Collections.sort(listToSort, new Comparator<CampaignRow>() {
			@Override
			public int compare(CampaignRow cr1, CampaignRow cr2) {
				float field1 = cr1.toListFloat().get(index);
				float field2 = cr2.toListFloat().get(index);
				
				if (field1 - field2 > 0) {
					return -1;
				}
				else if (field1 == field2) {
					return 0;
				}
				else {
					return 1;
				}
				
			}
	    });
    	
    	return listToSort;
    }
    
}