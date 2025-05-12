package root.storygram.mapper;

import org.mapstruct.*;
import root.storygram.dto.response.UserFollowerRequest;
import root.storygram.entity.UserFollower;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserFollowerMapper {

    @Named("toFollowingRequest")
    @Mapping(source = "following.profilePictureUrl", target = "profileImageUrl")
    @Mapping(source = "following.account.username", target = "username")
    @Mapping(target = "name", expression = "java(userFollower.getFollowing().getFirstName() + \" \" + userFollower.getFollowing().getLastName())")
    @Mapping(source = "requestStatus", target = "status")
    UserFollowerRequest userFollowerToFollowingRequest(UserFollower userFollower);

    @Named("toFollowerRequest")
    @Mapping(source = "follower.profilePictureUrl", target = "profileImageUrl")
    @Mapping(source = "follower.account.username", target = "username")
    @Mapping(target = "name", expression = "java(userFollower.getFollower().getFirstName() + \" \" + userFollower.getFollower().getLastName())")
    @Mapping(source = "requestStatus", target = "status")
    UserFollowerRequest userFollowerToFollowerRequest(UserFollower userFollower);

    @Named("toFollowerRequestWithoutStatus")
    @Mapping(source = "follower.profilePictureUrl", target = "profileImageUrl")
    @Mapping(source = "follower.account.username", target = "username")
    @Mapping(target = "name", expression = "java(userFollower.getFollower().getFirstName() + \" \" + userFollower.getFollower().getLastName())")
    @Mapping(target = "status", ignore = true)
    UserFollowerRequest userFollowerToFollowerRequestWithoutStatus(UserFollower userFollower);

    @Named("toFollowingRequestWithoutStatus")
    @Mapping(source = "following.profilePictureUrl", target = "profileImageUrl")
    @Mapping(source = "following.account.username", target = "username")
    @Mapping(target = "name", expression = "java(userFollower.getFollowing().getFirstName() + \" \" + userFollower.getFollowing().getLastName())")
    @Mapping(target = "status", ignore = true)
    UserFollowerRequest userFollowerToFollowingRequestWithoutStatus(UserFollower userFollower);

    @IterableMapping(qualifiedByName = "toFollowingRequest")
    List<UserFollowerRequest> userFollowerListToFollowingRequestList(List<UserFollower> followers);

    @IterableMapping(qualifiedByName = "toFollowerRequest")
    List<UserFollowerRequest> userFollowerListToFollowerRequestList(List<UserFollower> followers);

    @IterableMapping(qualifiedByName = "toFollowingRequestWithoutStatus")
    List<UserFollowerRequest> userFollowerListToFollowingRequestListWithoutStatus(List<UserFollower> followers);

    @IterableMapping(qualifiedByName = "toFollowerRequestWithoutStatus")
    List<UserFollowerRequest> userFollowerListToFollowerRequestListWithoutStatus(List<UserFollower> followers);
}

