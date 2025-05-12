package root.storygram.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import root.storygram.enums.FollowingRequestStatus;

@Data
public class UserFollowerRequest {
    private String profileImageUrl;
    private String username;
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private FollowingRequestStatus status;
}
