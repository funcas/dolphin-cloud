package cn.goktech.dolphin.common.exception;

import cn.goktech.dolphin.common.ApiResult;
import cn.goktech.dolphin.common.enumeration.ApiResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author funcas
 * @version 1.0
 * @date 2019年06月12日
 */
@RestController
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult serviceException(ServiceException e) {
        return ApiResult.builder()
                .apiResultEnum(ApiResultEnum.SERVICE_EXCEPTION).result(e.getMessage()).build();
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult bindException(MethodArgumentNotValidException e) {
        return ApiResult.builder()
                .apiResultEnum(ApiResultEnum.VALIDATION_FAILURE)
                .result(e.getBindingResult().getAllErrors()).build();
    }

    @ExceptionHandler(SystemException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ApiResult systemException(SystemException e) {
        return ApiResult.builder()
                .apiResultEnum(ApiResultEnum.SERVICE_EXCEPTION)
                .result(e.getMessage()).build();

    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResult accessDeniedException(AccessDeniedException e) {
        return ApiResult.builder()
                .apiResultEnum(ApiResultEnum.UNAUTHORIZED)
                .result(e.getMessage()).build();

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult globalException(Throwable throwable) {
        if(log.isErrorEnabled()) {
            log.error(null, throwable);
        }
        return ApiResult.builder()
                .apiResultEnum(ApiResultEnum.FAILURE).result(throwable.getMessage()).build();
    }

}
