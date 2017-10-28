package com.ikip.newsdetect.model;

public enum UpdateStateEnum {
	
	DEACTIVATED(-1), WAIT(0) ,GET_NEWS(1),DETECT_ALERTS(2),DETECT_PLACES(3);

	private int value;

	UpdateStateEnum(int value) {
            this.value = value;
    }

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
}
