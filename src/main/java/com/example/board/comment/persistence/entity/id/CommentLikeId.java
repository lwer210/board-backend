package com.example.board.comment.persistence.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CommentLikeId implements Serializable {

    @Column(name = "user_seq")
    @Comment("사용자 시퀀스")
    private Long userSeq;

    @Column(name = "comment_seq")
    @Comment("댓글 시퀀스")
    private Long commentSeq;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentLikeId that = (CommentLikeId) o;
        return Objects.equals(userSeq, that.userSeq) && Objects.equals(commentSeq, that.commentSeq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userSeq, commentSeq);
    }
}
