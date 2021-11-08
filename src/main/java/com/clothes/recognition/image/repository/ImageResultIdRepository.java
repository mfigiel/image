package com.clothes.recognition.image.repository;

import com.clothes.recognition.image.model.entity.ImageResultIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageResultIdRepository extends JpaRepository<ImageResultIdEntity, Long> {

    List<ImageResultIdEntity> getImageResultIdEntitiesByImageEntityId(Long imageEntityId);
}
