package com.ikip.newsdetect.model;

public enum LocationLevelEnum {
	
	LOCAL(1),REGION(2),COUNTRY(3);
	
	private int value;

    private LocationLevelEnum(int value) {
            this.value = value;
    }

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
