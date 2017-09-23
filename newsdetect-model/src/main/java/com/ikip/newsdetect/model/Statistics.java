package com.ikip.newsdetect.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "estadisticas")
@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Statistics {

	@Id
	@Column(name = "FECHA")
	private Date fecha;
	@Column(name = "TOTAL")
	private Integer numNews = 0;
	@Column(name = "ALERTA")
	private Integer alertas = 0;
	@Column(name = "RIESGO")
	private Integer riesgos = 0;
	@Column(name = "NOTICIAS")
	private Integer noticias = 0;
	
	@Transient
	private Long totalrecuperadas;
	@Transient
	private String tiempodeuso;
	
}
