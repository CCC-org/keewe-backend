package ccc.keeweapi.api;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
