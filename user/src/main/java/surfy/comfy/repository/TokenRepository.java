package surfy.comfy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import surfy.comfy.entity.Token;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Long> {

    Optional<Token> findByRefreshToken(String refreshToken);
    Optional<Token> findByMember_Id(Long memberId);
    Optional<Token> findByRefreshTokenIdxEncrypted(String tokenIdx);



}
