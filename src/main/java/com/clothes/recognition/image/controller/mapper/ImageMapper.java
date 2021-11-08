package com.clothes.recognition.image.controller.mapper;

import com.clothes.recognition.image.controller.data.ImageApi;
import com.clothes.recognition.image.model.dto.ImageDto;
import org.springframework.stereotype.Component;

@Component
public class ImageMapper {
    public ImageDto map(ImageApi image) {
        return new ImageDto(image.getUserId(), image.getFile(), image.getReceiveDateTime());
    }
}
