package com.clothes.recognition.image.repository;

import com.clothes.recognition.image.model.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

    @Query(value =
            "SELECT id " +
            "FROM public.image " +
            "where md5_sum = :md5Sum ;", nativeQuery = true)
    Long getIdByMd5Sum(@Param("md5Sum") String md5Sum);
}
