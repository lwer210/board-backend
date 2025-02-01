package com.example.board.auth.persistence.repository;

import com.example.board.auth.persistence.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenEntityRepository extends JpaRepository<RefreshTokenEntity, Long> {
    boolean existsByUserId(Long userId);

    void deleteByUserId(Long userId);
}
