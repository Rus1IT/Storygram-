package root.storygram.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import root.storygram.entity.Post;
import root.storygram.entity.Profile;
import root.storygram.enums.PostStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByProfileAndStatusIn(Profile profile, List<PostStatus> statuses, Pageable pageable);
    Page<Post> findAllByProfile(Profile profile, Pageable pageable);

    Optional<Post> findByProfileAndShortCode(Profile profile, String shortCode);
}
