package com.ikip.newsdetect.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;

@Entity
@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class FeedLog {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
	private LocalDateTime diaregistro;
	private Long totalrecuperadas;
	private LocalDateTime tiempodeuso;
	
	
	public FeedLog() {

		diaregistro=LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
		totalrecuperadas=0l;
		tiempodeuso=LocalDateTime.MIN;
	}

}
