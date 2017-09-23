package com.ikip.newsdetect.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Location {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String query;
	
	private Timestamp ultimaRecuperacion;
	@Enumerated(EnumType.ORDINAL)
	public LocationLevelEnum type;
	//private CountryCode country; TODO

}
