package com.ikip.newsdetect.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "feeds")
public class Feed {

	@Id @GeneratedValue
	private Long id;
	@NotNull
	private String name;
	private String dateFormat;
	@Lob
	@NotNull
	private String urlScrap;
	private String urlFeed;
	@Enumerated(EnumType.STRING)
	@NotNull
	private ScrapTypeEnum scrapType;
	@NotNull
	private String feedType;
	private String description;
	@NotNull
	private Integer minRefresh;
	private String selectorTitle;
	private String selectorContent;
	private String selectorPubDate;
	private Boolean selectorTitleMeta;
	private Boolean selectorContentMeta;
	private Boolean selectorPubDateMeta;
	@Enumerated(EnumType.STRING)
	private CharsetEnum charSet;
	@Enumerated(EnumType.STRING)
	private UpdateStateEnum state;
	@Enumerated(EnumType.STRING)
	private ExtractionTypeEnum extractionType;
	
}
