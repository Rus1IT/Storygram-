package root.storygram.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import root.storygram.entity.Post;
import root.storygram.entity.PostLike;
import root.storygram.entity.Profile;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByProfileAndPost(Profile profile, Post post);

    Page<PostLike> findByPost(Post post, Pageable pageable);
    Page<PostLike> findByProfile(Profile profile, Pageable pageable);
}
