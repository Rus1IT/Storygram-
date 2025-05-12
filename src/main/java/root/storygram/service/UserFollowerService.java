package root.storygram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import root.storygram.dto.response.UserFollowerRequest;
import root.storygram.entity.Profile;
import root.storygram.entity.UserFollower;
import root.storygram.enums.AccountType;
import root.storygram.enums.FollowingRequestStatus;
import root.storygram.exception.AlreadyExistException;
import root.storygram.exception.NotFoundException;
import root.storygram.mapper.UserFollowerMapper;
import root.storygram.repository.UserFollowerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserFollowerService {

    private final UserFollowerRepository userFollowerRepository;
    private final ProfileService profileService;
    private final UserFollowerMapper userFollowerMapper;
    private final AccessControlService accessControlService;

    public void followTo(String followingName) {
        Profile currentProfile = profileService.getCurrentProfile();
        Profile followingProfile = profileService.getProfile(followingName);

        validateNotSelfFollow(currentProfile, followingProfile);
        checkNotAlreadyFollow(currentProfile, followingProfile);

        FollowingRequestStatus followingRequestStatus = getFollowingRequestStatus(followingProfile);
        UserFollower userFollower = UserFollower.builder()
                .follower(currentProfile)
                .following(followingProfile)
                .requestStatus(followingRequestStatus)
                .build();

        userFollowerRepository.save(userFollower);
    }

    public void acceptFollowingRequest(String followingName) {
        Profile currentProfile = profileService.getCurrentProfile();
        Profile followingProfile = profileService.getProfile(followingName);

        UserFollower userFollower = getUserFollower(followingProfile, currentProfile);
        validatePendingRequest(userFollower.getRequestStatus());

        userFollower.setRequestStatus(FollowingRequestStatus.ACCEPTED);
        userFollowerRepository.save(userFollower);

    }

    public void rejectFollowingRequest(String followingName) {
        Profile currentProfile = profileService.getCurrentProfile();
        Profile followingProfile = profileService.getProfile(followingName);

        UserFollower userFollower = getUserFollower(followingProfile, currentProfile);
        validatePendingRequest(userFollower.getRequestStatus());

        userFollowerRepository.delete(userFollower);
    }

    public void deleteFollower(String followerName) {
        Profile currentProfile = profileService.getCurrentProfile();
        Profile followerProfile = profileService.getProfile(followerName);

        UserFollower userFollower = getUserFollower(followerProfile, currentProfile);
        validateAcceptedRequest(userFollower.getRequestStatus());

        userFollowerRepository.delete(userFollower);
    }


    public void unfollowFrom(String followingName) {
        Profile currentProfile = profileService.getCurrentProfile();
        Profile followingProfile = profileService.getProfile(followingName);

        UserFollower userFollower = getUserFollower(currentProfile, followingProfile);
        userFollowerRepository.delete(userFollower);
    }

    public Page<UserFollower> getFollowerRequestPage(String username, Pageable pageable) {
        Profile profile = accessControlService.resolveProfile(username);

        Page<UserFollower>  userFollowerPage =  accessControlService.isCurrentUser(profile)
                ? userFollowerRepository.findAllByFollowing(profile, pageable)
                : userFollowerRepository.findAllByFollowingAndRequestStatus(profile, FollowingRequestStatus.ACCEPTED, pageable);

        PaginationService.validatePage(userFollowerPage);
        return userFollowerPage;
    }

    public List<UserFollowerRequest> toUserFollowerRequests(String username, List<UserFollower> userFollowerList) {
        Profile profile = accessControlService.resolveProfile(username);
        return accessControlService.isCurrentUser(profile)
                ? userFollowerMapper.userFollowerListToFollowerRequestList(userFollowerList)
                : userFollowerMapper.userFollowerListToFollowerRequestListWithoutStatus(userFollowerList);
    }


    public Page<UserFollower> getFollowingRequestPage(String username, Pageable pageable) {
        Profile profile = accessControlService.resolveProfile(username);
        Page<UserFollower> userFollowingPage =  userFollowerRepository.findAllByFollower(profile, pageable);
        PaginationService.validatePage(userFollowingPage);
        return userFollowingPage;

    }

    public List<UserFollowerRequest> toUserFollowingRequest(String username, List<UserFollower> userFollowerList) {
        Profile profile = accessControlService.resolveProfile(username);
        return accessControlService.isCurrentUser(profile)
                ? userFollowerMapper.userFollowerListToFollowingRequestList(userFollowerList)
                : userFollowerMapper.userFollowerListToFollowingRequestListWithoutStatus(userFollowerList);
    }


    private void validateNotSelfFollow(Profile currentProfile, Profile followingProfile) {
        if (currentProfile.equals(followingProfile)) {
            throw new IllegalArgumentException("You can't follow for yourself");
        }
    }

    private void validatePendingRequest(FollowingRequestStatus followingRequestStatus) {
        if (followingRequestStatus != FollowingRequestStatus.PENDING) {
            throw new IllegalArgumentException("Following request is not in a pending state.");
        }
    }

    private void validateAcceptedRequest(FollowingRequestStatus status) {
        if (status != FollowingRequestStatus.ACCEPTED) {
            throw new IllegalArgumentException("You can only delete accepted followers.");
        }
    }

    private void checkNotAlreadyFollow(Profile currentProfile, Profile followingProfile) {
        userFollowerRepository.findByFollowerAndFollowing(currentProfile, followingProfile)
                .ifPresent(existing -> {
                    throw new AlreadyExistException("You already followed", "follow");
                });
    }


    private FollowingRequestStatus getFollowingRequestStatus(Profile profile) {
        if (profile.getAccount().getType() == AccountType.CLOSED) {
            return FollowingRequestStatus.PENDING;
        }
        return FollowingRequestStatus.ACCEPTED;
    }

    private UserFollower getUserFollower(Profile currentProfile, Profile followingProfile) {
        return userFollowerRepository.findByFollowerAndFollowing(currentProfile, followingProfile)
                .orElseThrow(() -> new NotFoundException("You are not following", "follow"));
    }

}
