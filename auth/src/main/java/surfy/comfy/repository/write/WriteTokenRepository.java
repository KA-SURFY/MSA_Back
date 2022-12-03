package surfy.comfy.repository.write;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import surfy.comfy.entity.Token;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface WriteTokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByRefreshToken(String refreshToken);
    Optional<Token> findByMemberId(Long memberId);
    Optional<Token> findByRefreshTokenIdxEncrypted(String tokenIdx);



}