package com.clothes.recognition.image.repository;

import com.clothes.recognition.image.model.entity.ImageResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageResultRepository extends JpaRepository<ImageResultEntity, Long> {

    List<ImageResultEntity> getImageResultEntitiesByUse(Boolean use);
}
