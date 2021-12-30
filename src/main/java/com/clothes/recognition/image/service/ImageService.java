package com.clothes.recognition.image.service;

import com.amazonaws.services.rekognition.model.BoundingBox;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Instance;
import com.amazonaws.services.rekognition.model.Label;
import com.clothes.recognition.image.model.converter.HashMapConverter;
import com.clothes.recognition.image.model.dto.ImageDto;
import com.clothes.recognition.image.model.entity.ImageEntity;
import com.clothes.recognition.image.repository.ImageRepository;
import com.clothes.recognition.image.utilities.FileHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import static com.clothes.recognition.image.utilities.FileHelper.multipartFileToImage;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final AmazonService amazonService;
    private final ImageRepository imageRepository;
    private final HashMapConverter hashMapConverter;
    private final ImageResultService imageResultService;

    public void putImageToS3(ImageDto imageDto) {
        String imageName = FileHelper.getFileName(imageDto);
        amazonService.putImageToS3(imageDto, imageName);
    }

    public void detectImage(ImageDto imageDto) {
        amazonService.detectImage(imageDto);
    }

    public String detectAndSaveImage(ImageDto imageDto) {
        Long idByMd5Sum = null;
        String md5Sum = null;
        if (imageDto.getCheckIsExistsInDB()) {
            md5Sum = FileHelper.getMD5Sum(imageDto.getFile());
            idByMd5Sum = imageRepository.getIdByMd5Sum(md5Sum);
        }
        if (idByMd5Sum == null) {
            return startRecognizeProcessForNewImage(imageDto, md5Sum);
        } else {
            return imageResultService.startRecognizeProcessForExistingImage(idByMd5Sum);
        }
    }

    public String startRecognizeProcessForNewImage(ImageDto imageDto, String md5Sum) {
        List<Label> labels = amazonService.detectImage(imageDto);
        getImagePosition(getLabelToUsed(labels), multipartFileToImage(imageDto.getFile()));
        saveImageData(imageDto, FileHelper.getFileName(imageDto), labels, md5Sum, getLabelToUsed(labels));
        return imageResultService.getLabelInUseForImage(labels.stream().map(label -> label.getName()).collect(Collectors.toSet()));
    }

    private void getImagePosition(Label labelToUsed, BufferedImage bufferedImage) {
        if (labelToUsed == null) {
            return;
        }
        Instance instance = labelToUsed.getInstances().stream().filter(el -> el.getBoundingBox() != null).findFirst().orElse(null);
        if (instance == null) {
            return;
        }
        BoundingBox boundingBox = instance.getBoundingBox();
        Float left = bufferedImage.getWidth() * boundingBox.getLeft();
        Float top = bufferedImage.getHeight() * boundingBox.getTop();
        Float clotheWidth = bufferedImage.getWidth() * boundingBox.getWidth();
        Float clotheHeight = bufferedImage.getHeight() * boundingBox.getHeight();

        BufferedImage croppedImage = bufferedImage.getSubimage(
                left.intValue(),
                top.intValue(),
                clotheWidth.intValue(),
                clotheHeight.intValue()
        );

        Color pixel = new Color(croppedImage.getRGB(clotheWidth.intValue() / 2, (int) (clotheHeight.intValue() * 0.5)));
    }

    private Label getLabelToUsed(List<Label> labels) {
        return labels.stream().filter(el -> !el.getInstances().isEmpty() && el.getInstances().get(0).getBoundingBox() != null).findFirst().orElse(null);
    }

    private ImageEntity saveImageData(ImageDto imageDto, String imageName, List<Label> labels, String md5Sum, Label labelToUsed) {
        try {
            ImageEntity imageEntity = imageRepository.save(new ImageEntity(null, imageDto.getUserId(), imageName, imageDto.getFile().getBytes(),
                    imageDto.getReceiveDateTime(), hashMapConverter.convertToJson(labels), md5Sum));
            saveResults(labels, labelToUsed, imageEntity);

            return imageEntity;
        } catch (IOException e) {
            log.error("failed to save image, ".concat(e.getMessage()));
        }
        return null;
    }

    private void saveResults(List<Label> labels, Label labelToUsed, ImageEntity imageEntity) {
        if (labelToUsed != null) {
            imageResultService.saveImageResultWithLabelToUsed(imageEntity.getId(), labels, labelToUsed.getName());
        } else {
            imageResultService.saveImageResult(imageEntity.getId(), labels);
        }
    }

    public List<String> getAllLabelsJson() {
        return imageRepository.findAll().stream().map(el -> el.getLabelsJson()).collect(Collectors.toList());
    }

    public DetectLabelsResult detectCustomLabelsv2(ImageDto imageDto) {
        return amazonService.detectImageV2(imageDto);
    }
}
