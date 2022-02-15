package com.spring.boot.accessory.controller;

import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.boot.accessory.annotations.LogExecutionTime;
import com.spring.boot.accessory.repository.MobileAccessoryRepository;

import msk.spring.boot.common.mobile.dto.MobileAccessoryDto;
import msk.spring.boot.common.mobile.entity.MobileAccessory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("mobile-accessories")
public class MobileAccessoryController {
	@Autowired
	MobileAccessoryRepository repository;

	@PostMapping
	@LogExecutionTime
	public Flux<MobileAccessoryDto> saveMobileAccessory(@RequestBody MobileAccessory mobileAccessory) {
		mobileAccessory.setActive(true);
		UUID id = UUID.randomUUID();
		mobileAccessory.setId(id);
		Mono<MobileAccessory> savedData = repository.save(mobileAccessory);
		// 1)Way
//		return savedData.flatMap(data -> getAllAccessory().collectList())
//				.flatMapMany(list -> Flux.fromIterable(list));
		// 2nd)Way
		return savedData.flatMapMany(data -> getAllAccessory());
	}

	@GetMapping
	@LogExecutionTime
	public Flux<MobileAccessoryDto> getAllAccessory() {
		return convertEntityToDto(repository.findAll());
	}

	@GetMapping("/{uuid}")
	@LogExecutionTime
	public Mono<MobileAccessoryDto> getAllAccessoryById(@PathVariable UUID uuid) {
		return convertEntityToDto(repository.findById(uuid));
	}

	@GetMapping("/{mobile-type}")
	@LogExecutionTime
	public Flux<MobileAccessoryDto> getAllAccessoryByType(@PathVariable String mobileType) {
		return convertEntityToDto(repository.findByMobileType(mobileType));
	}

	@PutMapping("/{uuid}")
	@LogExecutionTime
	public Mono<MobileAccessoryDto> updateMobileAccessory(@PathVariable UUID uuid,
			@RequestBody MobileAccessory mobileAccessory) {
		Mono<MobileAccessoryDto> mobileItem = getAllAccessoryById(uuid);

		return mobileItem.flatMap(acc -> {
			if (null == acc || null == mobileAccessory)
				throw new RuntimeException("request empty");
			mobileAccessory.setActive(true);
			mobileAccessory.setId(uuid);
			return repository.save(mobileAccessory).flatMap(data -> {

				return getAllAccessoryById(uuid);
			});

		});
	}

	@DeleteMapping("/{uuid}")
	@LogExecutionTime
	public Mono<String> deleMobile(@PathVariable UUID uuid) {
		Mono<MobileAccessoryDto> mobileItem = getAllAccessoryById(uuid);
		return mobileItem.flatMap(acc -> {
			MobileAccessory entity = new MobileAccessory();
			BeanUtils.copyProperties(acc, entity);
			entity.setActive(false);
			return repository.save(entity).flatMap(data -> {
				return Mono.just("success");
			});

		});
	}

	private Flux<MobileAccessoryDto> convertEntityToDto(Flux<MobileAccessory> allMobileDetails) {
		return allMobileDetails.flatMap(entity -> {
			MobileAccessoryDto dto = MobileAccessoryDto.builder().id(entity.getId()).desciption(entity.getDesciption())
					.isActive(entity.isActive()).name(entity.getName()).mobileType(entity.getMobileType()).build();
			return Mono.just(dto);
		});
	}

	private Mono<MobileAccessoryDto> convertEntityToDto(Mono<MobileAccessory> allMobileDetails) {
		return allMobileDetails.flatMap(entity -> {
			MobileAccessoryDto dto = MobileAccessoryDto.builder().id(entity.getId()).desciption(entity.getDesciption())
					.isActive(entity.isActive()).name(entity.getName()).mobileType(entity.getMobileType()).build();
			return Mono.just(dto);
		});
	}

}
