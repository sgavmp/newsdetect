package com.ikip.newsdetect.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "detected_news")
public class DetectedNews {

	@Id
	@GeneratedValue
	private Long id;
	@NotNull
	private Long feedId;
	@Lob
	@NotNull
	private String title;
	@Lob
	@NotNull
	private String link;
	@NotNull
	private LocalDateTime datePub;
	@NotNull
	private Long alertId;
	@NotNull
	@Enumerated(EnumType.STRING)
	private NewsDetectStatusEnum status;
	private Float score;
	private Float scoreLucene;

}
