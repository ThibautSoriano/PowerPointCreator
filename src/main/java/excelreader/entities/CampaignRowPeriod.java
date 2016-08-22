package main.java.excelreader.entities;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import main.java.utils.Internationalization;
import main.java.utils.Percentage;
import main.java.utils.Utils;

public class CampaignRowPeriod extends CampaignRow {
	
	public static final int MAX_COLUMNS_PERIOD = 10;
	
	private Date startPeriod;
	
	public CampaignRowPeriod() {
		super();
	}

	public CampaignRowPeriod(String firstColumnData, int impressions, float frequency, int clicks, int clickingUsers,
			Percentage clickThroughRate, Percentage uniqueCTR, Date periodStart) {
		super(firstColumnData, impressions, frequency, clicks, clickingUsers, clickThroughRate, uniqueCTR);
		this.startPeriod = periodStart;
	}


	public Date getStartPeriod() {
		return startPeriod;
	}

	public void setStartPeriod(Date startPeriod) {
		this.startPeriod = startPeriod;
	}
	
	
	public static List<CampaignRowPeriod> periodSortBy(List<CampaignRowPeriod> listToSort, final int index) {
    	Collections.sort(listToSort, new Comparator<CampaignRowPeriod>() {
			@Override
			public int compare(CampaignRowPeriod cr1, CampaignRowPeriod cr2) {
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
	
	
	public List<String> toList(boolean weekly, String dateFormat) {
		
        List<String> l = new ArrayList<String>();
        if (startPeriod != null){
                if (weekly) 
                    l.add(Utils.convertDateToTimePeriodWeekly(startPeriod,dateFormat));
                else
                    l.add(Utils.convertDateToTimePeriodMonthly(startPeriod,dateFormat));

        }
        else
        	l.add("NO DATE");
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
	
	public static CampaignRowPeriod getRowByDate(Date date, List<CampaignRowPeriod> content) {
		for (int i = 0; i < content.size(); i++) {
			if (content.get(i).getStartPeriod().equals(date)) {
				return content.get(i);
			}
		}
		
		return null;
	}
}
