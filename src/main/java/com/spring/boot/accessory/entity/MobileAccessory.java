package com.spring.boot.accessory.entity;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class MobileAccessory {
	@Id
	private UUID id;
	private String name;
	private String desciption;
	private String mobileType;
	private boolean isActive;
}
