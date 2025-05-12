package root.storygram.dto.response;

import lombok.Data;
import root.storygram.enums.PostStatus;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class PostResponseDto {
    private String shortCode;
    private String text;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private PostStatus status;
    private boolean isPinned;
    private Double latitude;
    private Double longitude;
    private Long likesCount;
    private Long commentsCount;
    private List<PostMediaResponse> postMedias;
}
