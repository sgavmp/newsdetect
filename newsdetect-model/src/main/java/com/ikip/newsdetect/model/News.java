package com.ikip.newsdetect.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "news")
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
	@GeneratedValue
	private Long id;
	@Lob
	private String title;
	@Lob
	private String content;
	@NotNull
	private Long feedId;
	@Lob
	@NotNull
	private String url;
	@NotNull
	private LocalDateTime pubDate = LocalDateTime.now();

}
