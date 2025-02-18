package com.example.board.comment.persistence.entity;

import com.example.board.article.persistence.entity.ArticleEntity;
import com.example.board.auth.persistence.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "comment_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_seq")
    @Comment("댓글 시퀀스")
    private Long id;

    @Column(name = "answer", length = 500)
    @Comment("댓글")
    private String answer;

    @Column(name = "like_count")
    @Comment("좋아요 수")
    private Long like;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_seq", nullable = false)
    private ArticleEntity article;
}
