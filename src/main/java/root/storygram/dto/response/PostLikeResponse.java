package root.storygram.dto.response;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class PostLikeResponse {
    private String profilePicture;
    private String username;
    private String fullName;
    private String postShortCode;
    private OffsetDateTime likedAt;
}
