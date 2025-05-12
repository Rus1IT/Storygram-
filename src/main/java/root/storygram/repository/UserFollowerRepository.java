package root.storygram.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import root.storygram.entity.Profile;
import root.storygram.entity.UserFollower;
import root.storygram.enums.FollowingRequestStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFollowerRepository extends JpaRepository<UserFollower, Long> {
    Optional<UserFollower> findByFollowerAndFollowing(Profile follower, Profile following);

    List<UserFollower> findAllByFollowingAndRequestStatus(Profile follower, FollowingRequestStatus requestStatus);

    @Query("SELECT uf FROM UserFollower uf WHERE uf.following = :following ORDER BY " +
            "CASE WHEN uf.requestStatus = 'PENDING' THEN 0 ELSE 1 END, uf.id ASC")
    Page<UserFollower> findAllByFollowing(@Param("following") Profile following, Pageable pageable);

    @Query("SELECT uf FROM UserFollower uf WHERE uf.follower = :follower ORDER BY " +
            "CASE WHEN uf.requestStatus = 'PENDING' THEN 0 ELSE 1 END, uf.id ASC")
    Page<UserFollower> findAllByFollower(@Param("follower") Profile follower, Pageable pageable);

    Page<UserFollower> findAllByFollowerAndRequestStatus(Profile follower, FollowingRequestStatus requestStatus, Pageable pageable);

    Page<UserFollower> findAllByFollowingAndRequestStatus(Profile profile, FollowingRequestStatus followingRequestStatus, Pageable pageable);
}
