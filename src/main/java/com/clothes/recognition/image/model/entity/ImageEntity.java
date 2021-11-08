package com.clothes.recognition.image.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "image")
@Getter
@Setter
@NoArgsConstructor
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long UserId;

    @Column(name = "name")
    private String name;

    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    @Column(name="pic")
    private byte[] pic;

    @Column(name = "receive_date_time")
    private LocalDateTime receiveDateTime;

    @CreationTimestamp
    @Column(name = "stored_in_db_datetime", nullable = false, updatable = false)
    private LocalDateTime storedInDbDateTime;

    @Lob
    @Column(name = "labels_json", nullable = false)
    private String labelsJson;

    @Column(name = "md5_sum")
    private String md5Sum;

    public ImageEntity(Long id, Long userId, String name, byte[] pic, LocalDateTime receiveDateTime, String labelsJson, String md5Sum) {
        this.id = id;
        UserId = userId;
        this.name = name;
        this.pic = pic;
        this.receiveDateTime = receiveDateTime;
        this.labelsJson = labelsJson;
        this.md5Sum = md5Sum;
    }
}
