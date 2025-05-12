package root.storygram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import root.storygram.dto.response.PostLikeResponse;
import root.storygram.entity.Post;
import root.storygram.entity.PostLike;
import root.storygram.entity.Profile;
import root.storygram.exception.NotFoundException;
import root.storygram.mapper.PostLikeMapper;
import root.storygram.repository.PostLikeRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostService postService;
    private final ProfileService profileService;
    private final PostLikeMapper postLikeMapper;

    public void likePost(String username, String postShortCode) {
        Profile currentProfile = profileService.getCurrentProfile();
        Post post = postService.getPost(username, postShortCode);
        PostLike postLike = PostLike.builder()
                .profile(currentProfile)
                .post(post)
                .build();
        postLikeRepository.save(postLike);
    }

    public void unlikePost(String username, String postShortCode) {
        Profile currentProfile = profileService.getCurrentProfile();
        Post post = postService.getPost(username, postShortCode);
        PostLike postLike = getPostLike(currentProfile, post);
        postLikeRepository.delete(postLike);
    }

    public Page<PostLike> getPostLikePage(String username, String postShortCode, Pageable pageable) {
        Post post = postService.getPost(username, postShortCode);
        Page postLikePage = postLikeRepository.findByPost(post, pageable);

        PaginationService.validatePage(postLikePage);
        return postLikePage;
    }

    public List<PostLikeResponse> toPostLikeResponseList(List<PostLike> postLikes) {
        return postLikeMapper.postLikeListToPostLikeResponseList(postLikes);
    }


    public PostLike getPostLike(Profile profile, Post post) {
        return postLikeRepository.findByProfileAndPost(profile, post)
                .orElseThrow(() -> new NotFoundException("Like for post not found", "like"));
    }
}
