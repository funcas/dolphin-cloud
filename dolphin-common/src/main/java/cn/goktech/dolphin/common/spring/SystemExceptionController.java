package cn.goktech.dolphin.common.spring;

import cn.goktech.dolphin.common.ApiResult;
import cn.goktech.dolphin.common.enumeration.ApiResultEnum;
import cn.goktech.dolphin.common.exception.ServiceException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author funcas
 * @version 1.0
 * @date 2018年10月14日
 */
@RestController
@ControllerAdvice
public class SystemExceptionController {

    @ExceptionHandler(ServiceException.class)
    public ApiResult serviceException(ServiceException exception) {
        return ApiResult.builder()
                .apiResultEnum(ApiResultEnum.SERVICE_EXCEPTION)
                .retMessage(exception.getMessage())
                .build();
    }

    @ExceptionHandler
    public ApiResult globalException(Throwable throwable) {
        throwable.printStackTrace();
        return ApiResult.builder().apiResultEnum(ApiResultEnum.UNKNOWN_EXCEPTION).retMessage(throwable.getMessage()).build();
    }

    @ExceptionHandler(BindException.class)
    public ApiResult bindException(BindException exception) {
        return ApiResult.builder().apiResultEnum(ApiResultEnum.VALIDATION_FAILURE)
                .result(exception.getBindingResult().getAllErrors()).build();
    }
}
