package com.spring.boot.accessory.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.boot.accessory.entity.MobileAccessory;
import com.spring.boot.accessory.repository.MobileAccessoryRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class MobileAccessoryController {
	@Autowired
	MobileAccessoryRepository repository;

	@PostMapping
	public Flux<MobileAccessory> saveMobileAccessory(@RequestBody MobileAccessory mobileAccessory) {
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
	public Flux<MobileAccessory> getAllAccessory() {
		return repository.findAll();
	}

	@GetMapping("/{uuid}")
	public Mono<MobileAccessory> getAllAccessoryById(@PathVariable UUID uuid) {
		return repository.findById(uuid);
	}

	@GetMapping("/{mobile-type}")
	public Flux<MobileAccessory> getAllAccessoryByType(@PathVariable String mobileType) {
		return repository.findByMobileType(mobileType);
	}

	@PutMapping("/{uuid}")
	public Mono<MobileAccessory> updateMobileAccessory(@PathVariable UUID uuid,
			@RequestBody MobileAccessory mobileAccessory) {
		Mono<MobileAccessory> mobileItem = getAllAccessoryById(uuid);

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
	public Mono<String> deleMobile(@PathVariable UUID uuid) {
		Mono<MobileAccessory> mobileItem = getAllAccessoryById(uuid);
		return mobileItem.flatMap(acc -> {
			acc.setActive(false);
			return repository.save(acc).flatMap(data -> {
				return Mono.just("success");
			});

		});
	}

}
