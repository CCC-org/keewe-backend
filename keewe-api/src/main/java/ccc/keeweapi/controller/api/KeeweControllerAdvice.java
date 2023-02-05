package ccc.keeweapi.controller.api;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@Slf4j
public class KeeweControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiResponse<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.info("handleIllegalArgumentException: {}", ex.getMessage(), ex);
        return ApiResponse.failure(KeeweRtnConsts.ERR400, ex.getMessage());
    }

    @ExceptionHandler(KeeweException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiResponse<?> handleKeeweException(KeeweException ex) {
        log.error("KeeweException[{}]: {}", ex.getKeeweRtnConsts(), ex.getMessage());
        return ApiResponse.failure(ex.getKeeweRtnConsts(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.info("MethodArgumentNotValidException: {}", ex.getMessage());
        String messages = ex.getFieldErrors().stream()
                .map(this::fieldErrorMessage)
                .collect(Collectors.joining("\n"));
        return ApiResponse.failure(KeeweRtnConsts.ERR400, messages);
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiResponse<?> handleContraintViolationException(InvalidDataAccessApiUsageException ex) {
        log.info("ConstraintViolationException: {}", ex.getMessage(), ex);
        return ApiResponse.failure(KeeweRtnConsts.ERR400, ex.getCause().getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleException(HttpServletRequest request, Exception ex) {
        ex.printStackTrace();
        log.error("Request: [{}] Exception: {}", request.getRequestURL(), ex.getMessage());
        return ApiResponse.failure(KeeweRtnConsts.ERR999, KeeweRtnConsts.ERR999.getDescription());
    }

    private String fieldErrorMessage(FieldError fieldError) {
        String field = fieldError.getField();
        Object rejectedValue = fieldError.getRejectedValue();
        String message = fieldError.getDefaultMessage();
        return String.format("field name: [%s], rejected value: [%s] message: [%s]", field, rejectedValue, message);
    }
}
