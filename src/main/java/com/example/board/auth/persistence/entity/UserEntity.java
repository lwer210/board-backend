package com.example.board.auth.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

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

    @Column(name = "email", length = 150, nullable = false)
    @Comment("사용자 이메일")
    private String email;

    @Column(name = "nickname", length = 100, nullable = false)
    @Comment("사용자 닉네임")
    private String nickname;

    @Column(name = "password", length = 200, nullable = false)
    @Comment("사용자 비밀번호")
    private String password;

    @Column(name = "role", length = 20, nullable = false)
    @Comment("사용자 권한")
    private String role; // TODO 추후 Enum 클래스로 변경 필요

    // TODO 게시글 Entity 연관관계 설정 필요
}
