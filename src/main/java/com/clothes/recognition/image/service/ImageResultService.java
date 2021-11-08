package com.clothes.recognition.image.service;

import com.amazonaws.services.rekognition.model.Label;
import com.clothes.recognition.image.model.entity.ImageResultEntity;
import com.clothes.recognition.image.model.entity.ImageResultIdEntity;
import com.clothes.recognition.image.model.mapper.ImageResultEntityMapper;
import com.clothes.recognition.image.repository.ImageResultIdRepository;
import com.clothes.recognition.image.repository.ImageResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageResultService {

    private final ImageResultEntityMapper imageResultEntityMapper;
    private final ImageResultRepository imageResultRepository;
    private final ImageResultIdRepository imageResultIdRepository;

    public void saveImageResultWithLabelToUsed(Long imageEntityId, List<Label> labels, String labelToUsed) {
        Set<ImageResultEntity> imageResultEntity = labels.stream().map(label -> imageResultEntityMapper.map(label, labelToUsed)).collect(Collectors.toSet());
        imageResultEntity.stream().forEach(imageResult -> imageResultRepository.save(imageResult));
        imageResultEntity.stream().forEach(imageResult -> imageResultIdRepository.save(new ImageResultIdEntity(null, imageEntityId, imageResult.getLabel())));
    }

    public void saveImageResult(Long imageEntityId, List<Label> labels) {
        Set<ImageResultEntity> imageResultEntity = labels.stream().map(label -> imageResultEntityMapper.map(label)).collect(Collectors.toSet());
        imageResultEntity.stream().forEach(imageResult -> imageResultRepository.save(imageResult));
        imageResultEntity.stream().forEach(imageResult -> imageResultIdRepository.save(new ImageResultIdEntity(null, imageEntityId, imageResult.getLabel())));
    }

    public List<ImageResultEntity> getLabelsInUse() {
        return imageResultRepository.getImageResultEntitiesByUse(true);
    }

    public String startRecognizeProcessForExistingImage(Long idByMd5Sum) {
        List<ImageResultIdEntity> labels = imageResultIdRepository.getImageResultIdEntitiesByImageEntityId(idByMd5Sum);
        return getLabelInUseForImage(labels.stream().map(label -> label.getImageResultlabel()).collect(Collectors.toSet()));
    }

    public String getLabelInUseForImage(Set<String> labels) {
        List<ImageResultEntity> labelsInUse = getLabelsInUse();
        return labelsInUse.stream()
                .filter(labelInUse -> labels.stream().anyMatch(label -> label.equals(labelInUse.getLabel())))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No labels in use"))
                .getLabel();
    }
}
