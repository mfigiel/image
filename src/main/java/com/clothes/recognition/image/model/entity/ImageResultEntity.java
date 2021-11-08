package com.clothes.recognition.image.model.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "image_result")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageResultEntity {
    @Id
    @Column(name = "label", unique = true, updatable = false, nullable = false)
    private String label;

    @Column(name = "parent_label")
    private String parentLabel;

    @Column(name = "confidence")
    private Float confidence;

    @Column(updatable = false)
    private Boolean use;
}
