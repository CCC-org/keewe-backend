package ccc.keeweapi.controller.api;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.List;
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
        String message = String.format("KeeweException[%s]: %s", ex.getKeeweRtnConsts(), ex.getMessage());
        log.error(message, ex);
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

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiResponse<?> handleConstraintViolationException(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .map(this::constraintViolationMessage)
                .collect(Collectors.joining(", "));
        log.info("handleConstraintViolationException: {}", ex.getMessage());
        return ApiResponse.failure(KeeweRtnConsts.ERR400, message);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleException(HttpServletRequest request, Exception ex) {
        String message = String.format("request(%s), exception(%s)", request.getRequestURL(), ex.getMessage());
        log.error(message, ex);
        return ApiResponse.failure(KeeweRtnConsts.ERR999, KeeweRtnConsts.ERR999.getDescription());
    }

    private String fieldErrorMessage(FieldError fieldError) {
        String field = fieldError.getField();
        Object rejectedValue = fieldError.getRejectedValue();
        String message = fieldError.getDefaultMessage();
        return String.format("field name: [%s], rejected value: [%s] message: [%s]", field, rejectedValue, message);
    }

    private String constraintViolationMessage(ConstraintViolation constraintViolation) {
        String propertyName = getPropertyName(constraintViolation.getPropertyPath());
        return String.format("%s: %s", propertyName, constraintViolation.getMessage());
    }

    private String getPropertyName(Path path) {
        return path.toString().split("\\.", 100)[1];
    }
}
