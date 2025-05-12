package root.storygram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import root.storygram.entity.PostComment;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

}
