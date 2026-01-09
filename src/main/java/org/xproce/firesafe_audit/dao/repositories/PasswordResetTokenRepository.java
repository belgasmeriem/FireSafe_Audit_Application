package org.xproce.firesafe_audit.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.xproce.firesafe_audit.dao.entities.PasswordResetToken;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    List<PasswordResetToken> findByUserId(Long userId);

    List<PasswordResetToken> findByUserIdOrderByDateCreationDesc(Long userId);

    @Query("SELECT t FROM PasswordResetToken t WHERE t.user.id = :userId " +
            "AND t.utilise = false " +
            "AND t.dateExpiration > :now " +
            "ORDER BY t.dateCreation DESC")
    List<PasswordResetToken> findValidTokensByUser(@Param("userId") Long userId,
                                                   @Param("now") LocalDateTime now);

    @Query("SELECT t FROM PasswordResetToken t WHERE t.token = :token " +
            "AND t.utilise = false " +
            "AND t.dateExpiration > :now")
    Optional<PasswordResetToken> findValidToken(@Param("token") String token,
                                                @Param("now") LocalDateTime now);

    @Query("SELECT t FROM PasswordResetToken t WHERE t.dateExpiration < :now AND t.utilise = false")
    List<PasswordResetToken> findExpiredTokens(@Param("now") LocalDateTime now);

    @Query("SELECT COUNT(t) FROM PasswordResetToken t WHERE t.user.id = :userId AND t.utilise = false")
    long countValidTokensByUser(@Param("userId") Long userId);

    boolean existsByToken(String token);

    @Modifying
    @Query("DELETE FROM PasswordResetToken t WHERE t.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM PasswordResetToken t WHERE t.dateExpiration < :date")
    void deleteExpiredTokens(LocalDateTime date);

    @Modifying
    @Query("DELETE FROM PasswordResetToken t WHERE t.dateExpiration < :date")
    void deleteByDateExpirationBefore(@Param("date") LocalDateTime date);

    @Modifying
    @Query("DELETE FROM PasswordResetToken t WHERE t.utilise = true AND t.dateUtilisation < :date")
    void deleteOldUsedTokens(@Param("date") LocalDateTime date);
}