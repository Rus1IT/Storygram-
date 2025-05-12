package root.storygram.dto.request.profilelink;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateProfileLinkRequest {
    @NotBlank(message = "Link url is required")
    @Size(min = 5, max = 255, message = "Length of link must be between 5 and 255 characters")
    private String linkUrl;

    @NotBlank(message = "Link name is required")
    @Size(min = 1, max = 50, message = "Length of link name must be between 1 and 50 characters")
    private String linkTitle;
}
