package com.example.board.auth.persistence.entity;

import com.example.board.article.persistence.entity.ArticleEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_info")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    @Comment("사용자 시퀀스")
    private Long id;

    @Column(name = "email", length = 150, nullable = false, unique = true)
    @Comment("사용자 이메일")
    private String email;

    @Column(name = "nickname", length = 100, nullable = false, unique = true)
    @Comment("사용자 닉네임")
    private String nickname;

    @Column(name = "password", length = 200, nullable = false)
    @Comment("사용자 비밀번호")
    private String password;

    @Column(name = "role", length = 20, nullable = false)
    @Comment("사용자 권한")
    private String role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleEntity> articles = new ArrayList<>();
}
