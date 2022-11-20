package surfy.comfy.exception.token;

import surfy.comfy.config.BaseResponseStatus;
import surfy.comfy.exception.BusinessException;

public class InvalidRefreshToken extends BusinessException {
    public InvalidRefreshToken(){super(BaseResponseStatus.INVALID_JWT);}

}
