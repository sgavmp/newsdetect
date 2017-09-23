package com.ikip.newsdetect.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class NewsDetect {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	private Long site;
	@Lob
	private String title;
	@Lob
	private String link;
	private LocalDateTime datePub;
	private Long alertDetect;
	private boolean history = false;
	private boolean falPositive = false;
	private boolean mark = false;
	private float score;
	private float scoreLucene;

}
