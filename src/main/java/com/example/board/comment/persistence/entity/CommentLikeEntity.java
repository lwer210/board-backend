package com.example.board.comment.persistence.entity;

import com.example.board.auth.persistence.entity.BaseEntity;
import com.example.board.comment.persistence.entity.id.CommentLikeId;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "comment_like")
public class CommentLikeEntity extends BaseEntity {

    @EmbeddedId
    private CommentLikeId id;

}
