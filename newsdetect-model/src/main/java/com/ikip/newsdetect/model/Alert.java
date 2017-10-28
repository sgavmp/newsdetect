package com.ikip.newsdetect.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "alerts")
public class Alert {

	@Id
	@GeneratedValue
	private Long id;
	protected String title;
	@NotNull
	@Lob
	private String query;
	@Enumerated(EnumType.STRING)
	private AlertLevelEnum type;

}
