package com.clothes.recognition.image.controller.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Data
public class ImageApi {
    @NotNull
    private Long UserId;
    @NotNull
    private MultipartFile file;
    @JsonIgnore
    private LocalDateTime receiveDateTime = now();

    private Boolean checkIsExistsInDB;
}
