package com.zpp.cybercs.exception;

import com.zpp.cybercs.common.BaseResponse;
import com.zpp.cybercs.common.ErrorCode;
import com.zpp.cybercs.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {BindException.class, ValidationException.class, MethodArgumentNotValidException.class})
    public BaseResponse<String> handleValidatedException(Exception e) {
        log.error("参数校验异常", e);
        BaseResponse<String> result = null;
        if (e instanceof MethodArgumentNotValidException) {

            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            result = ResultUtils.error(ErrorCode.PARAMS_ERROR,
                    ex.getBindingResult().getAllErrors().stream()
                            .map(ObjectError::getDefaultMessage)
                            .collect(Collectors.joining(";"))
            );
        } else if (e instanceof ConstraintViolationException) {

            ConstraintViolationException ex = (ConstraintViolationException) e;
            result = ResultUtils.error(ErrorCode.PARAMS_ERROR,
                    ex.getConstraintViolations().stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.joining(";"))
            );
        } else if (e instanceof BindException) {

            BindException ex = (BindException) e;
            result = ResultUtils.error(ErrorCode.PARAMS_ERROR,
                    ex.getAllErrors().stream()
                            .map(ObjectError::getDefaultMessage)
                            .collect(Collectors.joining(";"))
            );
        }
        return result;
    }

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<String> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public BaseResponse<String> runtimeExceptionHandler(Exception e) {
        log.error("Exception", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }
}
