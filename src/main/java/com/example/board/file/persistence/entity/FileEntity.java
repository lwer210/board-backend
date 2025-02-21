package com.example.board.file.persistence.entity;

import com.example.board.auth.persistence.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "file_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(
        name = "file_seq_generator",
        sequenceName = "file_seq",
        initialValue = 1,
        allocationSize = 50
)
public class FileEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_seq_generator")
    @Comment("파일 시퀀스")
    private Long id;

    @Column(name = "original_file_name", length = 200)
    @Comment("기존 파일명")
    private String originalFileName;

    @Column(name = "file_name", length = 200)
    @Comment("파일명")
    private String fileName;

    @Column(name = "extensioin", length = 20)
    @Comment("확장자")
    private String extension;

    @Column(name = "save_path", length = 200)
    @Comment("저장 경로")
    private String savePath;
}
