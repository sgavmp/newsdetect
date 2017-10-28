package com.ikip.newsdetect.main.dto;

import com.google.common.collect.Lists;
import com.ikip.newsdetect.model.*;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import java.util.ArrayList;
import java.util.List;

@Data
public class FeedForm {
	
	@NotBlank
	@NotEmpty
	private String name;
	private String dateFormat;
	private boolean isRSS = false;
	private boolean isAuto = false;
	private boolean actived = true;
	private Integer minRefresh = 120;
	private String selectorTitle;
	private String selectorContent;
	private String selectorPubDate;
	private boolean selectorTitleMeta;
	private boolean selectorContentMeta;
	private boolean selectorPubDateMeta;
	private List<String> feedPlace;
	private CharsetEnum charSet = CharsetEnum.UTF8;
	private ExtractionTypeEnum extractionType = ExtractionTypeEnum.ARTICLE_EXTRACTOR;
	private boolean NoEstandar = false;
	private List<String> pairAttributesTypes;
	private List<String> pairAttributesValues;
//	private List<Entry> pairAttributesInput;
	private String PluginPath;
	
	@URL
	@NotEmpty
	private String urlNews;//Url de la pagina de noticias o de rss
	private String newsLink;
	
	public FeedForm() {
		this.feedPlace=Lists.newArrayList();
		this.pairAttributesTypes=new ArrayList<String>();
	}
	
//	public FeedForm(Feed feed) {
//		this.name=feed.getName();
//		this.dateFormat=feed.getDateFormat();
//		this.languaje=feed.getLanguaje();
//		this.urlNews=feed.getUrlNews();
//		this.newsLink = feed.getNewsLink();
//		this.type = feed.getType();
//		this.isRSS = feed.isRSS();
//		this.minRefresh = feed.getMinRefresh();
//		this.selectorContent = feed.getSelectorContent();
//		this.selectorContentMeta = feed.getSelectorContentMeta();
//		this.selectorTitle = feed.getSelectorTitle();
//		this.selectorTitleMeta = feed.getSelectorTitleMeta();
//		this.selectorPubDate = feed.getSelectorPubDate();
//		this.selectorPubDateMeta = feed.getSelectorPubDateMeta();
//		this.actived = feed.isActived();
//		this.charSet = feed.getCharSet();
//		this.feedPlace = feed.getFeedPlace();
//		this.feedType = feed.getFeedType();
//		this.extractionType = feed.getExtractionType();
//		this.isAuto = feed.getIsAuto();
//		this.NoEstandar=feed.isNoEstandar();
//		this.pairAttributesTypes=new ArrayList<String>();
//		this.pairAttributesValues=new ArrayList<String>();
//		for (PairAtributte feedPlaceEnum : feed.getPairAttributes()) {
//			this.pairAttributesTypes.add(feedPlaceEnum.getType());
//			this.pairAttributesValues.add(feedPlaceEnum.getValue());
//		}
//		this.PluginPath=feed.getPluginPath();
//		this.pairAttributesInput=feed.getPairAttributes();
//	}
	
}
