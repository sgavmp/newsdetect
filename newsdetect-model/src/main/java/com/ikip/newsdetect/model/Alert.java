package com.ikip.newsdetect.model;

import com.sun.istack.internal.NotNull;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Alert {

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private Long id;
	protected String title;
	protected String titleEn;
	@NotNull
	@Lob
	private String words;
	@Lob
	private String wordsNegative;
	@Enumerated(EnumType.STRING)
	private AlertLevelEnum type;
	private LocalDateTime ultimaRecuperacion;
	public float Score;

}
