package root.storygram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import root.storygram.dto.request.PostCommentRequest;
import root.storygram.entity.PostComment;
import root.storygram.repository.PostCommentRepository;

@Service
@RequiredArgsConstructor
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final UniqueIdService uniqueIdService;

    public void addComment(PostCommentRequest postCommentRequest){
//        PostComment postComment = PostComment.builder()
//                .
    }
}
