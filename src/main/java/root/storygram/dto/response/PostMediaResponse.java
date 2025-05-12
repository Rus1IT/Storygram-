package root.storygram.dto.response;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class PostMediaResponse {
    private String mediaUrl;
    private String shortCode;
    private String mediaType;
    private Integer position;
    private OffsetDateTime createdAt;
}
