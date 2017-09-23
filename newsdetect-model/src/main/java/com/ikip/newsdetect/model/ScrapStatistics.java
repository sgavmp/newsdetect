package com.ikip.newsdetect.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ScrapStatistics {

	@Id
	@Column(name = "FECHA")
	private LocalDateTime date;
	@Column(name = "TOTAL")
	private Integer total = 0;
	
}