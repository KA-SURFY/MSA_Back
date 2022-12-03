package surfy.comfy.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import surfy.comfy.auth.JwtTokenProvider;
import surfy.comfy.auth.oauth.GoogleOauth;
import surfy.comfy.data.oauth.GoogleUser;
import surfy.comfy.repository.read.ReadMemberRepository;
import surfy.comfy.repository.read.ReadTokenRepository;
import surfy.comfy.repository.write.WriteMemberRepository;
import surfy.comfy.repository.write.WriteTokenRepository;
import surfy.comfy.type.SocialLoginType;
import surfy.comfy.data.token.TokenResponse;
import surfy.comfy.entity.Member;
import surfy.comfy.entity.Token;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final GoogleOauth googleOauth;
    Logger logger= LoggerFactory.getLogger(OAuthService.class);

    private final ReadMemberRepository readMemberRepository;
    private final ReadTokenRepository readTokenRepository;
    private final WriteMemberRepository writeMemberRepository;
    private final WriteTokenRepository writeTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse oAuthLogin(SocialLoginType socialLoginType, String accessToken) throws IOException, NoSuchAlgorithmException {
        switch (socialLoginType) {
            case GOOGLE: {
                //구글로 일회성 코드를 보내 액세스 토큰이 담긴 응답객체를 받아옴
                //ResponseEntity<String> accessTokenResponse = googleOauth.requestAccessToken(code);
                //응답 객체가 JSON형식으로 되어 있으므로, 이를 deserialization해서 자바 객체에 담을 것이다.
                //GoogleOAuthToken oAuthToken = googleOauth.getAccessToken(accessTokenResponse);
                //액세스 토큰을 다시 구글로 보내 구글에 저장된 사용자 정보가 담긴 응답 객체를 받아온다.
                //ResponseEntity<String> userInfoResponse = googleOauth.requestUserInfo(oAuthToken);
                ResponseEntity<String> userInfoResponse = googleOauth.requestUserInfo(accessToken);
                //다시 JSON 형식의 응답 객체를 자바 객체로 역직렬화한다.
                GoogleUser googleUser = googleOauth.getUserInfo(userInfoResponse);
                logger.info("googleUser: {}",googleUser);
                String user_id = googleUser.getEmail();
                logger.info("userId: {}", user_id);

                String jwtAccessToken=jwtTokenProvider.createAccessToken(googleUser.getEmail());
                String jwtRefreshToken=jwtTokenProvider.createRefreshToken(googleUser.getEmail());
                logger.info("[발급된 refresh token]: {}",jwtRefreshToken);

                if(!isJoinedUser(googleUser)){
                    signUp(googleUser);
                    logger.info("[회원가입]");
                }

                logger.info("[로그인]");
                Member member = readMemberRepository.findByEmail(googleUser.getEmail()).orElseThrow(IllegalArgumentException::new);
                Token token;
                if(readTokenRepository.findByMemberId(member.getId()).isPresent()){
                    token=writeTokenRepository.findByMemberId(member.getId()).get();
                }
                else{
                    token=new Token();
                }
                token.setMemberId(member.getId());
                token.setRefreshToken(jwtRefreshToken);
                Long idx=writeTokenRepository.save(token).getId();

                token=writeTokenRepository.findById(idx).get();
                String refreshId=jwtTokenProvider.tokenIndexEncrypt(String.valueOf(token));
                token.setRefreshTokenIdxEncrypted(refreshId);
                writeTokenRepository.save(token);

                //logger.info("[로그인] - refresh token 재발급");
                TokenResponse tokenResponse=new TokenResponse(jwtAccessToken,refreshId,member.getId(),member.getName(),member.getEmail());

                return tokenResponse;
            }
            default: {
                throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
            }
        }
    }

    private boolean isJoinedUser(GoogleUser googleUser) {
        Optional<Member> member = readMemberRepository.findByEmail(googleUser.getEmail());
        logger.info("Joined User: {}", member);
        return member.isPresent();
    }

    private void signUp(GoogleUser googleUser) {

        Member member = googleUser.toUserSignUp();
        writeMemberRepository.save(member);
    }

    @Transactional
    public String logout(Long memberId){
        Member member=readMemberRepository.findById(memberId).get();
        Token token=writeTokenRepository.findByMemberId(member.getId()).get();

        writeTokenRepository.delete(token);
        return "로그아웃 성공";
    }
}
