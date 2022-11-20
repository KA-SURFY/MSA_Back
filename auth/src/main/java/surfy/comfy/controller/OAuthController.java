package surfy.comfy.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import surfy.comfy.auth.JwtTokenProvider;
import surfy.comfy.service.OAuthService;
import surfy.comfy.data.token.TokenResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class OAuthController {
    private final OAuthService oAuthService;
    Logger logger= LoggerFactory.getLogger(OAuthController.class);
    private final JwtTokenProvider jwtTokenProvider;
    @RequestMapping("/auth")
    public void requestAuth(HttpServletRequest request, HttpServletResponse response){
        logger.info("==============================================");
        logger.info("preHandle method: {}",request.getMethod());
        logger.info("JwtToken 호출");
        String accessToken = request.getHeader("accesstoken");
        logger.info("AccessToken: {}",accessToken);
        String refreshToken = request.getHeader("refreshtoken");
        logger.info("RefreshToken: {}",refreshToken);
        logger.info("Request URL: {}",request.getRequestURL());

        if(accessToken.equals("null") && refreshToken.equals("null")){
            logger.info("tokens are null");
            response.setStatus(401);
            response.addHeader("msg", "Check the tokens.");
        }
        else if (!accessToken.equals("null") && jwtTokenProvider.isValidAccessToken(accessToken)) { // 유효한 accessToken
            logger.info(">JwtTokenInterceptor - isValidAccessToken");
            response.setStatus(200);
            response.addHeader("msg", "Token is accessed.");
        }
        // access token은 유효하지 않고, refresh token은 유효한 경우
        else if(!jwtTokenProvider.isValidAccessToken(accessToken)&&jwtTokenProvider.isValidRefreshToken(refreshToken)){
            logger.info(">JwtTokenInterceptor - invalid AccessToken && valid RefreshToken");
            TokenResponse tokenResponse=oAuthService.issueAccessToken(accessToken, refreshToken);
            if(tokenResponse==null){
                response.setStatus(401);
                response.addHeader("msg", "Check the tokens.");
            }
            else{
                logger.info("New accessToken: {}",tokenResponse.getACCESS_TOKEN());
                logger.info("NewrefreshToken: {}",tokenResponse.getREFRESH_TOKEN());
                response.setStatus(200);
                response.addHeader("msg", "Reissue access token.");
            }
        }
        // refresh token도 유효하지 않은 경우
        else{
            response.setStatus(401);
            response.addHeader("msg", "Check the tokens.");
            logger.info("response: {}",response);
        }
    }
}
