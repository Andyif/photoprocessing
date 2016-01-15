package com.amayd.uploadservice.rest.repository;

import com.amayd.uploadservice.rest.model.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageEntityRepository extends JpaRepository<ImageEntity, Long> {
}
