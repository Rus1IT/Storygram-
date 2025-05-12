package root.storygram.dto.response;

import lombok.Builder;
import lombok.Data;
import root.storygram.security.SecurityUtil;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Data
@Builder
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private String currentUsername;
    private T data;

    @Builder.Default
    private HashMap<String, Object> meta = defaultMeta();

    @Builder.Default
    private List<ErrorDetail> errors = Collections.emptyList();

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .currentUsername(SecurityUtil.getCurrentUsername())
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String message, PaginationMetaResponse pageMeta) {
        HashMap<String, Object> combinedMeta = defaultMeta();
        if (pageMeta != null) {
            combinedMeta.put("pagination", pageMeta);
        }
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .meta(combinedMeta)
                .currentUsername(SecurityUtil.getCurrentUsername())
                .build();
    }

    public static <T> ApiResponse<T> error(String message, List<ErrorDetail> errors) {
        return ApiResponse.<T>builder()
                .success(false)
                .data(null)
                .message(message)
                .errors(errors)
                .currentUsername(SecurityUtil.getCurrentUsername())
                .build();
    }

    private static HashMap<String, Object> defaultMeta() {
        HashMap<String, Object> meta = new HashMap<>();
        meta.put("version", "1.0");
        meta.put("date", OffsetDateTime.now(ZoneOffset.UTC));
        return meta;
    }

    @Data
    @Builder
    public static class ErrorDetail {
        private String field;
        private String message;
    }
}
