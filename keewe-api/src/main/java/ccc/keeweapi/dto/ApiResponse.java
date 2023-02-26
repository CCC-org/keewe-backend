package ccc.keeweapi.dto;

import ccc.keewecore.consts.KeeweRtnConsts;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ApiResponse<T> {
    @JsonIgnore
    private final KeeweRtnConsts status;
    private final String message;
    private final int code;
    private final T data;

    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<> (
                KeeweRtnConsts.NRM000,
                KeeweRtnConsts.NRM000.getDescription(),
                KeeweRtnConsts.NRM000.getCode(),
                null
        );
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<> (
                KeeweRtnConsts.NRM000,
                KeeweRtnConsts.NRM000.getDescription(),
                KeeweRtnConsts.NRM000.getCode(),
                data
        );
    }

    public static <T> ApiResponse<T> failure(KeeweRtnConsts status) {
        return new ApiResponse<>(
                status,
                status.getDescription(),
                status.getCode(),
                null
        );
    }

    public static <T> ApiResponse<T> failure(KeeweRtnConsts status, String message) {
        return new ApiResponse<>(
                status,
                message,
                status.getCode(),
                null
        );
    }
}
