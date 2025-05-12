package root.storygram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import root.storygram.entity.Post;
import root.storygram.entity.PostMedia;

import java.util.Optional;

@Repository
public interface PostMediaRepository extends JpaRepository<PostMedia, Long> {
    Optional<PostMedia> findByPostAndShortCode(Post post, String shortCode);
}
