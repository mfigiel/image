package com.clothes.recognition.image.model.dto;

import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;


@Value
public class ImageDto {
    private Long UserId;
    private MultipartFile file;
    private LocalDateTime receiveDateTime ;
}
