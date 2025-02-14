package com.example.board.article.persistence.entity;

import com.example.board.auth.persistence.entity.BaseEntity;
import com.example.board.auth.persistence.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "article_info")
public class ArticleEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_seq")
    @Comment("게시글 시퀀스")
    private Long id;

    @Column(name = "title", length = 100)
    @Comment("게시글 제목")
    private String title;

    @Column(name = "public_yn", length = 10)
    @Comment("게시글 공개 여부")
    private String publicYn;

    @Column(name = "content", length = 500)
    @Comment("게시글 본문")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private UserEntity user;
}
