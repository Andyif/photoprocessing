package com.amayd.uploadservice.rest.repository;

import com.amayd.uploadservice.rest.model.ProcessingResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessingResultRepository extends JpaRepository<ProcessingResult, Long> {
}
