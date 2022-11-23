package surfy.comfy.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import surfy.comfy.entity.Token;
import surfy.comfy.repository.read.ReadTokenRepository;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    Logger logger= LoggerFactory.getLogger(JwtTokenProvider.class);
    private final ReadTokenRepository readTokenRepository;
    private String SECRET_KEY="236979CB6F1AD6B6A6184A31E6BE37DB3818CC36871E26235DD67DCFE404149232r32rwerf";

    private String REFRESH_KEY="afkljeoiwajtgfasjdfhwerklawejriwjrewkhrjwajfejfkjawekraewradfaer3247hfkjashf9o3wrhkjgdfakhf3e9o5y8y4hfkjhfakjdhfwkerhwkahrfklwejr1l2kjeksekf";

    // 1 * 60 * 1000L;   // 1분
    // 1 * 30 * 1000L;   // 30초
    // 60 * 60 * 24 * 7 * 1000L; //1주
    private final long ACCESS_TOKEN_VALID_TIME = 1 * 10 * 1000L;

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        System.out.println("SECRET_KEY: "+SECRET_KEY);
        System.out.println("REFRESH_KEY = " + REFRESH_KEY);
        //SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
        //REFRESH_KEY = Base64.getEncoder().encodeToString(REFRESH_KEY.getBytes());
    }

    // JWT 토큰 생성
    public String createAccessToken(String email) {
        Claims claims = Jwts.claims();//.setSubject(userPk); // JWT payload 에 저장되는 정보단위
        claims.put("email", email);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME)) // set Expire Time
                .signWith(getSignKey(SECRET_KEY))  // 사용할 암호화 알고리즘과
                .compact();
    }

    private Key getSignKey(String key){
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims getClaimsFormToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Claims getClaimsToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey(REFRESH_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isValidAccessToken(String token) {
        System.out.println("isValidAccessToken is : " +token);
        try {
            Claims accessClaims = getClaimsFormToken(token);
            System.out.println("Access expireTime: " + accessClaims.getExpiration());
            System.out.println("Access email: " + accessClaims.get("email"));
            return true;
        } catch (ExpiredJwtException exception) {
            System.out.println("Token Expired email : " + exception.getClaims().get("email"));
            return false;
        } catch (JwtException exception) {
            System.out.println("Access Token Tampered");
            return false;
        } catch (NullPointerException exception) {
            System.out.println("Token is null");
            return false;
        }
    }
    public boolean isValidRefreshToken(String tokenId) {
        System.out.println("isValidRefreshTokenId is : " +tokenId);
        String refreshToken=readTokenRepository.findByRefreshTokenIdxEncrypted(tokenId).get().getRefreshToken();
        try{
            Claims refreshClaims = getClaimsToken(refreshToken);
            logger.info("Refresh expireTime: {}",refreshClaims.getExpiration());
            logger.info("Refresh email: {}",refreshClaims.get("email"));

            return true;
        }  catch (ExpiredJwtException exception) { // 리프레시 토큰 만료
            Token token= readTokenRepository.findByRefreshToken(refreshToken).get();
            readTokenRepository.delete(token);

            System.out.println("Token Expired email : " + exception.getClaims().get("email"));
            return false;
        } catch (JwtException exception) {
            System.out.println("Refresh Token Tampered");
            return false;
        } catch (NullPointerException exception) {
            System.out.println("Token is null");
            return false;
        }
//        try {
//
//            Claims refreshClaims = getClaimsToken(token);
//            logger.info("Refresh expireTime: {}",refreshClaims.getExpiration());
//            logger.info("Refresh email: {}",refreshClaims.get("email"));
//
//            return true;
//        } catch (ExpiredJwtException exception) { // 리프레시 토큰 만료
//            Token refreshToken= tokenRepository.findByRefreshToken(token).get();
//            tokenRepository.delete(refreshToken);
//            System.out.println("Token Expired email : " + exception.getClaims().get("email"));
//            return false;
//        } catch (JwtException exception) {
//            System.out.println("Refresh Token Tampered");
//            return false;
//        } catch (NullPointerException exception) {
//            System.out.println("Token is null");
//            return false;
//        }
    }
}