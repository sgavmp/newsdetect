package com.ikip.newsdetect.model;

import lombok.*;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

@Entity
@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Topic {
	@Id
	private String title;
	@NotNull
	@Lob
	private String words;
	
}
