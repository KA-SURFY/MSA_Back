package surfy.comfy.exception.token;


import surfy.comfy.config.BaseResponseStatus;
import surfy.comfy.exception.BusinessException;

public class RefreshTokenNotFound extends BusinessException {
    public RefreshTokenNotFound(){super(BaseResponseStatus.INVALID_JWT);}

}
