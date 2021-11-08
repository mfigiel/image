package com.clothes.recognition.image.model.mapper;

import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.services.rekognition.model.Parent;
import com.clothes.recognition.image.model.entity.ImageResultEntity;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ImageResultEntityMapper {
    public ImageResultEntity map(Label label, String labelToUsed) {
        if (label == null) return null;
        if (label.getName().equals(labelToUsed)) {
            return new ImageResultEntity(label.getName(), getParentName(label.getParents()), label.getConfidence(), true);
        }
        return new ImageResultEntity(label.getName(), getParentName(label.getParents()), label.getConfidence(), false);
    }


    private String getParentName(List<Parent> label) {
        if (label == null) return null;
        return label.stream().findFirst().map(Parent::getName).orElse(null);
    }

    public ImageResultEntity map(Label label) {
        if (label == null) return null;
        return new ImageResultEntity(label.getName(), getParentName(label.getParents()), label.getConfidence(), false);
    }
}
