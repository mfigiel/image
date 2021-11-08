package com.clothes.recognition.image.service;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.clothes.recognition.image.model.dto.ImageDto;
import com.clothes.recognition.image.utilities.FileHelper;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AmazonService {
    public static final String BUCKET = "awsimagerecognition";

    private final AmazonS3 amazonS3;
    private final AmazonRekognition amazonRekognition;

    public void putImageToS3(ImageDto imageDto, String imageName) {
        amazonS3.putObject(createPutObjectRequest(imageDto, imageName));
    }

    public List<Label> detectImage(ImageDto imageDto) {
        return amazonRekognition.detectLabels(prepareDetectLabelsRequestFromImage(imageDto)).getLabels();
    }

    public DetectLabelsResult detectImageV2(ImageDto imageDto) {
        return amazonRekognition.detectLabels(prepareDetectLabelsRequestFromImage(imageDto));
    }

    private DetectLabelsRequest prepareDetectLabelsRequestFromS3Object(String bucket, String imageName) {
        return new DetectLabelsRequest()
                .withImage(new Image()
                        .withS3Object(new S3Object()
                                .withName(imageName)
                                .withBucket(bucket)));

    }

    private DetectLabelsRequest prepareDetectLabelsRequestFromImage(ImageDto imageDto) {
        return new DetectLabelsRequest()
                .withImage(new Image()
                        .withBytes(FileHelper.getByteBufferFromImage(imageDto)));
    }

    @NotNull
    private PutObjectRequest createPutObjectRequest(ImageDto imageDto, String imageName) {
        return new PutObjectRequest(BUCKET, imageName, FileHelper.convert(imageDto.getFile()));
    }
}
