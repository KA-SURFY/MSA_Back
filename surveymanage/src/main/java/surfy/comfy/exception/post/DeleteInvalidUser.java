package surfy.comfy.exception.post;

import surfy.comfy.config.BaseResponseStatus;
import surfy.comfy.exception.BusinessException;

public class DeleteInvalidUser extends BusinessException {
    public DeleteInvalidUser(){super(BaseResponseStatus.DELETE_INVALID_USER);}
}
