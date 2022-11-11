//package surfy.comfy.auth.oauth;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import net.minidev.json.JSONObject;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.http.*;
//
//import java.util.HashMap;
//import java.util.Map;
//import org.slf4j.Logger;
//
//
//@Component
//@RequiredArgsConstructor
//public class KakaoOauth {
//    private final ObjectMapper objectMapper;
//    private final RestTemplate restTemplate = new RestTemplate();
//    private final Logger logger =LoggerFactory.getLogger(KakaoOauth.class);
//    @Value("${spring.OAuth2.kakao.client-id}")
//    private String KAKAO_SNS_CLIENT_ID;
//
//    public KakaoOAuthToken getAccessToken(String authorizedCode) throws JsonProcessingException{
//        // HttpHeader 오브젝트 생성
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        // HttpBody 오브젝트 생성
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type", "authorization_code");
//        params.add("client_id", KAKAO_SNS_CLIENT_ID);
//        params.add("redirect_uri", "http://localhost:3000/auth/kakao/callback");
//        params.add("code", authorizedCode);
//
//        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
//        RestTemplate rt = new RestTemplate();
//        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
//                new HttpEntity<>(params, headers);
//
//        // Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음.
//        ResponseEntity<String> response = rt.exchange(
//                "https://kauth.kakao.com/oauth/token",
//                HttpMethod.POST,
//                kakaoTokenRequest,
//                String.class
//        );
//
//        // JSON -> 액세스 토큰 파싱
//        KakaoOAuthToken kakaoOAuthToken=objectMapper.readValue(response.getBody(),KakaoOAuthToken.class);
//        logger.info("accessToken: {}",kakaoOAuthToken.getAccess_token());
//        return kakaoOAuthToken;
//    }
//
//    public KakaoUser requestUserInfo(KakaoOAuthToken oAuth Token){
//        String KAKAO_USERINFO_REQUEST_URL="https://kapi.kakao.com/v2/user/me";
//
//
//    }
//}
