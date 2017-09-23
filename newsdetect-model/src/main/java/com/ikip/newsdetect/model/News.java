package com.ikip.newsdetect.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;

@Entity
@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class News {
	
	public static String fieldTitle = "title";
	public static String fieldTitleNoCase = "titleN";
	public static String fieldBody = "body";
	public static String fieldBodyNoCase = "bodyN";
	public static String fieldDatePub = "datePub";
	public static String fieldDateCreate = "dateCreate";
	public static String fieldUrl = "id";
	public static String fieldSite = "feed";
	public static String fieldSiteLoc = "siteLoc";
	public static String fieldSiteType = "siteType";
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private Long id;
	@Lob
	private String title;
	@Lob
	private String content;
	private Long site;
	@Lob
	private String url;
	private LocalDateTime pubDate = LocalDateTime.now();

}
