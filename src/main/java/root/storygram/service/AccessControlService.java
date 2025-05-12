package root.storygram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import root.storygram.entity.Post;
import root.storygram.entity.Profile;
import root.storygram.entity.UserFollower;
import root.storygram.enums.AccountType;
import root.storygram.enums.FollowingRequestStatus;
import root.storygram.enums.PostStatus;
import root.storygram.exception.AccessDeniedException;
import root.storygram.exception.NotFoundException;
import root.storygram.repository.UserFollowerRepository;

@Service
@RequiredArgsConstructor
public class AccessControlService {

    private final ProfileService profileService;
    private final UserFollowerRepository userFollowerRepository;

    public Profile resolveProfile(String username) {
        Profile profile =  (username == null)
                ? profileService.getCurrentProfile()
                : profileService.getProfile(username);

        if (!isCurrentUser(profile)) {
            validateClosedProfileAccess(profile);
        }
        return profile;
    }

    private void validateClosedProfileAccess(Profile profile) {
        if (profile.getAccount().getType() == AccountType.CLOSED) {
            Profile currentProfile = profileService.getCurrentProfile();
            UserFollower userFollower = getUserFollower(currentProfile, profile);
            if (userFollower.getRequestStatus() != FollowingRequestStatus.ACCEPTED) {
                throw new AccessDeniedException("Access Denied: You are not allowed to view this closed profile", "account");
            }
        }
    }

    private UserFollower getUserFollower(Profile currentProfile, Profile followingProfile){
        return userFollowerRepository.findByFollowerAndFollowing(currentProfile, followingProfile)
                .orElseThrow(() -> new NotFoundException("You are not following", "follow"));
    }

    public boolean isCurrentUser(Profile profile) {
        Profile currentProfile = profileService.getCurrentProfile();
        return profile.getAccount().getUsername().equals(currentProfile.getAccount().getUsername());
    }

    public void validatePostAccess(Profile profile, Post post) {
        if (!isCurrentUser(profile) && post.getStatus() != PostStatus.PUBLIC) {
            throw new AccessDeniedException("Access Denied: Only public posts are viewable", "post");
        }
    }
}
