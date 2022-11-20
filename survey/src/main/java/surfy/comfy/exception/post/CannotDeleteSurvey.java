package surfy.comfy.exception.post;

import surfy.comfy.config.BaseResponseStatus;
import surfy.comfy.exception.BusinessException;

public class CannotDeleteSurvey extends BusinessException {
    public CannotDeleteSurvey(){super(BaseResponseStatus.POST_EXIST);}
}