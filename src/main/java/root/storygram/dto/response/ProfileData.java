package root.storygram.dto.response;

import lombok.Data;
import root.storygram.entity.ProfileLink;

import java.util.Date;
import java.util.List;

@Data
public class ProfileData {

    private String firstName;
    private String lastName;
    private Date birthDay;
    private String profilePictureUrl;
    private String bio;
    private Integer followerCount;
    private Integer followingCount;
    private Integer postCount;
    private List<ProfileLinkData> links;
}
