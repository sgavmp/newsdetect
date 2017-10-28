package com.ikip.newsdetect.model;

import lombok.*;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "topics")
public class Topic {
	@Id
	private String title;
	@NotNull
	@Lob
	private String query;
	
}
