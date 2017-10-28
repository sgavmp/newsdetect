package com.ikip.newsdetect.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "locations")
public class Location {

	@Id
	@GeneratedValue
	private Long id;
	@NotNull
	private String name;
	@NotNull
	@Lob
	private String query;

}
