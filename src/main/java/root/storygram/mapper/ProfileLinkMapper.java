package root.storygram.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import root.storygram.dto.request.profilelink.CreateProfileLinkRequest;
import root.storygram.dto.response.ProfileData;
import root.storygram.dto.response.ProfileLinkData;
import root.storygram.entity.ProfileLink;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfileLinkMapper {

    ProfileLink createProfileLinkRequestToProfileLink(CreateProfileLinkRequest createProfileLinkRequest);
    void updateProfileLinkRequestToProfileLink(CreateProfileLinkRequest createProfileLinkRequest, @MappingTarget ProfileLink profileLink);
    List<ProfileLinkData> profileLinkListToProfileDataList(List<ProfileLink> profileLinkList);
    List<ProfileLinkData> toProfileLinkDataList(List<ProfileLink> profileLinks);
}
