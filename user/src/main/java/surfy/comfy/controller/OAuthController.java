package surfy.comfy.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import surfy.comfy.exception.UserNotFoundException;
import surfy.comfy.type.SocialLoginType;
import surfy.comfy.config.BaseResponse;
import surfy.comfy.data.token.TokenResponse;
import surfy.comfy.service.OAuthService;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
public class OAuthController {
    private final OAuthService oAuthService;
    Logger logger= LoggerFactory.getLogger(OAuthController.class);

    // 구글 로그인 - 프론트에서 accessToken을 받음.
    @GetMapping(value="/login/google/{accessToken}")
    public BaseResponse<TokenResponse> googleLogin(@PathVariable(name="accessToken") String accessToken) throws IOException, NoSuchAlgorithmException {
//    public BaseResponse<TokenResponse> login(@PathVariable(name = "socialLoginType") String socialLoginPath, @RequestParam(name="accessToken") String accessToken) throws IOException {
        //logger.info("[login] socialLoginType: {}",socialLoginPath);
        logger.info("[login] accessToken: {}",accessToken);
        SocialLoginType socialLoginType= SocialLoginType.valueOf("google".toUpperCase());
        TokenResponse tokenResponse=oAuthService.oAuthLogin(socialLoginType,accessToken);
        logger.info("tokenResponse: {}",tokenResponse);

        return new BaseResponse<>(tokenResponse);
    }

    @DeleteMapping("/logout/{memberId}")
    public BaseResponse<String> logout(@PathVariable(name="memberId")Long memberId){
        logger.info("[LOGOUT]");
        try{
            String response=oAuthService.logout(memberId);
            return new BaseResponse<>(response);
        }
        catch(UserNotFoundException e){
            throw new UserNotFoundException();
        }
    }

}
