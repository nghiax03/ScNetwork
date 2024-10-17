package com.nghianguyen.scnetwork.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "picture_main")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PictureMain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "upload_time", nullable = false, updatable = false,
    columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime uploadTime;

}
