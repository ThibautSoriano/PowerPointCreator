package main.java.excelreader;

public class DocumentStructureTechnical extends DocumentStructure{

    @Override
    public int getFirstColumnLabelRow() {
        
        return 6;
    }

    @Override
    public int getFirstColumnLabelCol() {
        
        return 0;
    }
    
    
    @Override
    public int getDatesRow() {
        
        return 3;
    }

    @Override
    public int getDatesCol() {
        
        return 0;
    }

    @Override
    public int getCampaignRowStartRow() {
       
        return 6;
    }

    @Override
    public int getImpressionsCol() {
        
        return 1;
    }

    @Override
    public int getUniquesCookiesCol() {
       
        return 2;
    }

    @Override
    public int getFrequencyCol() {
        
        return 3;
    }

    @Override
    public int getClicksCol() {
       
        return 4;
    }

    @Override
    public int getClickingUsersCol() {
        
        return 5;
    }

    @Override
    public int getClickThroughRateCol() {
        
        return 6;
    }

    @Override
    public int getUniqueCtrCol() {
        
        return 7;
    }

    @Override
    public int getLabelsRow() {
        
        return 5;
    }

    @Override
    public int getLabelsCol() {
        
        return 0;
    }

   

}
