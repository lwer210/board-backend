package com.example.board.auth.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_token_info")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class RefreshTokenEntity {

    @Id
    @Column(name = "token", length = 500)
    @Comment("refresh 토큰")
    private String token;

    @Column(name = "expired_at", nullable = false)
    @Comment("만료 일시")
    private LocalDateTime expiredAt;

    @Column(name = "issued_at", nullable = false)
    @Comment("생성 일시")
    private LocalDateTime issuedAt;

    @Column(name = "use_yn", nullable = false)
    @Comment("사용 여부")
    private String useYn;

    @Column(name = "user_seq", nullable = false)
    @Comment("사용자 시퀀스")
    private Long userId;
}
