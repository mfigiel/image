package com.clothes.recognition.image.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "image_image_result")
@AllArgsConstructor
@NoArgsConstructor
public class ImageResultIdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_id")
    private Long imageEntityId;

    @Column(name = "label")
    private String imageResultlabel;
}
