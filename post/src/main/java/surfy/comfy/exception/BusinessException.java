package surfy.comfy.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import surfy.comfy.config.BaseResponseStatus;

public class BusinessException extends RuntimeException{

    private BaseResponseStatus errorCode;
    private final Logger logger= LoggerFactory.getLogger(BusinessException.class);
    public BusinessException(String message, BaseResponseStatus errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(BaseResponseStatus errorCode) {
        super(errorCode.getMessage());
        logger.info("[BusinessException] - errorCode: {}",errorCode);
        this.errorCode = errorCode;
    }




    public BaseResponseStatus getErrorCode() {
        return errorCode;
    }
}
