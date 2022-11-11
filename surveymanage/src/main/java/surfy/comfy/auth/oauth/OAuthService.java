package surfy.comfy.auth.oauth;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import surfy.comfy.auth.JwtTokenProvider;
import surfy.comfy.data.token.TokenResponse;
import surfy.comfy.entity.Member;
import surfy.comfy.exception.token.RefreshTokenNotFound;
import surfy.comfy.repository.MemberRepository;
import surfy.comfy.repository.TokenRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuthService {
    Logger logger= LoggerFactory.getLogger(OAuthService.class);

    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    // refresh token으로 access token 재발급
    public TokenResponse issueAccessToken(HttpServletRequest request){
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
        logger.info("accessToken = {}",accessToken);
        logger.info("refreshToken = {}",refreshToken);

        //accessToken이 만료됐고 refreshToken이 맞으면 accessToken을 새로 발급(refreshToken의 내용을 통해서)
        if(!jwtTokenProvider.isValidAccessToken(accessToken)){  //클라이언트에서 토큰 재발급 api로의 요청을 확정해주면 이 조건문은 필요없다.
            logger.info("Expired Access Token");
            if(jwtTokenProvider.isValidRefreshToken(refreshToken)){     //들어온 Refresh 토큰이 유효한지
                logger.info("Valid Refresh Token");
                String token=tokenRepository.findByRefreshTokenIdxEncrypted(refreshToken).get().getRefreshToken();
                Claims claimsToken = jwtTokenProvider.getClaimsToken(token);
                String email = (String)claimsToken.get("email");
                Optional<Member> member = memberRepository.findByEmail(email);
                String tokenFromDB = tokenRepository.findByMember_Id(member.get().getId()).get().getRefreshToken();
                logger.info("refresh token from DB: {}",tokenFromDB);
                if(token.equals(tokenFromDB)) {   //DB의 refresh토큰과 지금들어온 토큰이 같은지 확인
                    accessToken = jwtTokenProvider.createAccessToken(email);
                    logger.info("reissued access token: {}",accessToken);

                }
                else{
                    //DB의 Refresh토큰과 들어온 Refresh토큰이 다르면 중간에 변조된 것임
                    logger.error("Refresh Token Tampered");
                    throw new RefreshTokenNotFound();
                }
            }
            else{
                //입력으로 들어온 Refresh 토큰이 유효하지 않음
                logger.error("Invalid Refresh Token");
                throw new RefreshTokenNotFound();
            }
        }
        logger.info("[issueAccessToken]");
        logger.info("[accessToken] :{}",accessToken);
        logger.info("[refreshToken]: {}",refreshToken);
        return TokenResponse.builder()
                .ACCESS_TOKEN(accessToken)
                .REFRESH_TOKEN(refreshToken)
                .build();
    }
}
