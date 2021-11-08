package com.clothes.recognition.image.controller;

import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.clothes.recognition.image.controller.data.ImageApi;
import com.clothes.recognition.image.controller.mapper.ImageMapper;
import com.clothes.recognition.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.clothes.recognition.image.utilities.FileHelper.checkExtension;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    private final ImageMapper imageMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String detectImage(@Validated @ModelAttribute ImageApi image) {
        checkExtension(image.getFile().getOriginalFilename());
        return imageService.detectAndSaveImage(imageMapper.map(image));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<String> getAllLabelsJson() {
        return imageService.getAllLabelsJson();
    }

    @PostMapping("/v2")
    @ResponseStatus(HttpStatus.OK)
    public DetectLabelsResult detectImageV2(@Validated @ModelAttribute ImageApi image) {
        return imageService.detectCustomLabelsv2(imageMapper.map(image));
    }
}
