package root.storygram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import root.storygram.entity.Profile;
import root.storygram.entity.UserFollower;
import root.storygram.enums.FollowingRequestStatus;
import root.storygram.repository.UserFollowerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountFollowerService {
    private final UserFollowerRepository userFollowerRepository;

    public void acceptAllPendingRequests(Profile profile){
        List<UserFollower> pendingFollowers = userFollowerRepository
                .findAllByFollowingAndRequestStatus(profile, FollowingRequestStatus.PENDING);
        pendingFollowers.forEach(follower -> follower.setRequestStatus(FollowingRequestStatus.ACCEPTED));
        userFollowerRepository.saveAll(pendingFollowers);
    }
}
