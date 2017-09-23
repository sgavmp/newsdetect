package com.ikip.newsdetect.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Feed {

	@Id @GeneratedValue(strategy=GenerationType.TABLE)
	private Long id;
	private String name;
	private String dateFormat;
	@Enumerated(EnumType.STRING)
	private LanguageEnum languaje = LanguageEnum.SPANISH;
	@Lob
	private String lastNewsLink = "";
	@Lob
	private String urlNews;
	private boolean isRSS = false;
	private boolean isAuto = false;
	@Enumerated(EnumType.STRING)
	private WebLevelEnum type = WebLevelEnum.red;
	@Enumerated(EnumType.STRING)
	private FeedTypeEnum feedType = FeedTypeEnum.massmedia;
	private Integer numNewNews;
	private LocalDateTime dateFirstNews;
	private LocalDateTime ultimaRecuperacion;
	private boolean actived = true;
	private String comment;
	private Integer minRefresh = 120;
	private String selectorTitle;
	private String selectorContent;
	private String selectorPubDate;
	private boolean selectorTitleMeta;
	private boolean selectorContentMeta;
	private boolean selectorPubDateMeta;
	@Enumerated(EnumType.STRING)
	private CharsetEnum charSet = CharsetEnum.UTF8;
	@Enumerated(EnumType.STRING)
	private UpdateStateEnum state = UpdateStateEnum.WAIT;
	private String newsLink;
	@Enumerated(EnumType.STRING)
	private ExtractionTypeEnum extractionType = ExtractionTypeEnum.ARTICLE_EXTRACTOR;
	private boolean NoEstandar=false;
	private String pluginPath="";
	@Transient
	private boolean updateIndex=false;
	@Transient
	private String nextExecution="";
	
}
