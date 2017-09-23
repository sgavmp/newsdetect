package com.ikip.newsdetect.model;

public enum LanguageEnum {
	
	SPANISH(0) ,ENGLISH(1);

	private int value;

    private LanguageEnum(int value) {
            this.value = value;
    }

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
