package com.ikip.newsdetect.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

	@Id @GeneratedValue
	private Long id;
	private String userName;
	private String email;
	private String password;
	private boolean isAdmin;

}
