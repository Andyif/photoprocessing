package com.amayd.uploadservice.repository;

import com.amayd.uploadservice.modes.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository <UploadedFile, Long> {
}
