package main.java.datasdownloading.entities;

public enum CampaignStatus {
	CURRENT("current"), FINISHED("finished"), WAITING("waiting");
	
	private String value;
	
	private CampaignStatus(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		
		return value;
	}
}
