package surfy.comfy.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

import org.slf4j.Logger;
import surfy.comfy.auth.JwtTokenProvider;
import surfy.comfy.data.token.TokenResponse;
import surfy.comfy.entity.Member;
import surfy.comfy.repository.read.ReadMemberRepository;
import surfy.comfy.repository.read.ReadTokenRepository;

@Service
@RequiredArgsConstructor
public class OAuthService {
    Logger logger= LoggerFactory.getLogger(OAuthService.class);

    private final ReadMemberRepository readMemberRepository;
    private final ReadTokenRepository readTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // refresh token으로 access token 재발급
    public TokenResponse issueAccessToken(String accessToken, String refreshToken){
        logger.info("accessToken = {}",accessToken);
        logger.info("refreshToken = {}",refreshToken);

        //accessToken이 만료됐고 refreshToken이 맞으면 accessToken을 새로 발급(refreshToken의 내용을 통해서)
        if(!jwtTokenProvider.isValidAccessToken(accessToken)){  //클라이언트에서 토큰 재발급 api로의 요청을 확정해주면 이 조건문은 필요없다.
            logger.info("Expired Access Token");
            if(jwtTokenProvider.isValidRefreshToken(refreshToken)){     //들어온 Refresh 토큰이 유효한지
                logger.info("Valid Refresh Token");
                String token=readTokenRepository.findByRefreshTokenIdxEncrypted(refreshToken).get().getRefreshToken();
                Claims claimsToken = jwtTokenProvider.getClaimsToken(token);
                String email = (String)claimsToken.get("email");
                Optional<Member> member = readMemberRepository.findByEmail(email);
                String tokenFromDB = readTokenRepository.findByMember_Id(member.get().getId()).get().getRefreshToken();
                logger.info("refresh token from DB: {}",tokenFromDB);
                if(token.equals(tokenFromDB)) {   //DB의 refresh토큰과 지금들어온 토큰이 같은지 확인
                    accessToken = jwtTokenProvider.createAccessToken(email);
                    logger.info("reissued access token: {}",accessToken);

                }
                else{
                    //DB의 Refresh토큰과 들어온 Refresh토큰이 다르면 중간에 변조된 것임
                    logger.error("Refresh Token Tampered");
                    return null;
                }
            }
            else{
                //입력으로 들어온 Refresh 토큰이 유효하지 않음
                logger.error("Invalid Refresh Token");
                return null;
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
