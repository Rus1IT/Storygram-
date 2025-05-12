package root.storygram.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import root.storygram.dto.request.profile.CreateProfileRequestDto;
import root.storygram.dto.request.profile.UpdateProfileRequestDto;
import root.storygram.dto.response.ProfileData;
import root.storygram.dto.response.ProfileLinkData;
import root.storygram.entity.Profile;
import root.storygram.entity.ProfileLink;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    Profile createProfileRequestDtoToProfile(CreateProfileRequestDto createProfileRequestDto);

    void updateProfileRequestDtoToProfile(UpdateProfileRequestDto updateProfileRequestDto, @MappingTarget Profile profile);

    @Mapping(target = "followerCount", expression = "java(profile.getFollowers() != null ? profile.getFollowers().size() : 0)")
    @Mapping(target = "followingCount", expression = "java(profile.getFollowing() != null ? profile.getFollowing().size() : 0)")
    @Mapping(target = "postCount", expression = "java(profile.getPosts() != null ? profile.getPosts().size() : 0)")
    ProfileData createProfileToProfileData(Profile profile);



}
