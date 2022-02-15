package com.spring.boot.accessory.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import msk.spring.boot.common.mobile.entity.MobileAccessory;
import reactor.core.publisher.Flux;

public interface MobileAccessoryRepository extends ReactiveMongoRepository<MobileAccessory, UUID> {
	Flux<MobileAccessory> findByMobileType(String mobileType);
}
