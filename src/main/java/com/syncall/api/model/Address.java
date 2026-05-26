package com.syncall.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Address {

	@Column(nullable = false)
	private String city;
	
	@Column(nullable = false)
	private Long number;
	
	@Column(nullable = false)
	private String neighbourhood;
	
	@Column(nullable = false)
	private String street;
}
