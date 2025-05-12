package root.storygram.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostCommentRequest {

    @NotBlank(message = "Comment is required")
    private String comment;

    private String parentCommentShortCode;
}
