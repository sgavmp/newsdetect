package com.ikip.newsdetect.model;

public enum WebLevelEnum {
	
	YELLOW(1),ORANGE(2),RED(3),BLUE(4);
	
	private int value;

    private WebLevelEnum(int value) {
            this.value = value;
    }

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
