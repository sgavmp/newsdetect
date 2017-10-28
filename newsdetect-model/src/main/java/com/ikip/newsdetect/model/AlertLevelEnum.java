package com.ikip.newsdetect.model;

public enum AlertLevelEnum {
	
	YELLOW(1),ORANGE(2),RED(3);
	
	private int value;

    AlertLevelEnum(int value) {
            this.value = value;
    }

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
