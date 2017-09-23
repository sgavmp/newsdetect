package com.ikip.newsdetect.model;

public enum FeedTypeEnum {
	
	 massmedia(11), journal(21), specificmedia(31), institucional(41), alert(51);
	
	private Integer value;

    private FeedTypeEnum(Integer value) {
            this.value = value;
    }

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}


}
