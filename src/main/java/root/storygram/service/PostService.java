package root.storygram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import root.storygram.dto.request.UpdatePostPinRequest;
import root.storygram.dto.request.post.PostRequest;
import root.storygram.dto.response.PostResponseDto;
import root.storygram.entity.Post;
import root.storygram.entity.Profile;
import root.storygram.enums.PostStatus;
import root.storygram.exception.AccessDeniedException;
import root.storygram.exception.NotFoundException;
import root.storygram.mapper.PostMapper;
import root.storygram.repository.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final ProfileService profileService;
    private final PostMapper postMapper;
    private final UniqueIdService uniqueIdService;
    private final AccessControlService accessControlService;

    protected Post getNewPost() {
        Profile profile = profileService.getCurrentProfile();
        String shortCode = uniqueIdService.generateUniqueShortId();
        Post post = Post.builder()
                .profile(profile)
                .status(PostStatus.DRAFT)
                .shortCode(shortCode)
                .build();

        postRepository.save(post);
        return post;
    }

    public PostResponseDto getPostResponse(String username, String shortCode) {
        Profile profile = accessControlService.resolveProfile(username);
        Post post = getPostByProfileAndShortCode(profile, shortCode);

        accessControlService.validatePostAccess(profile, post);
        return postMapper.postToPostResponseDto(post);
    }

    public Post getPost(String username, String shortCode) {
        Profile profile = accessControlService.resolveProfile(username);
        Post post = getPostByProfileAndShortCode(profile, shortCode);

        accessControlService.validatePostAccess(profile, post);
        return post;
    }

    public PostResponseDto publishPost(String shortCode, PostRequest postRequest) {
        Post post = getPostByShortCode(shortCode);
        postMapper.updatePost(postRequest, post);
        post.setStatus(PostStatus.PUBLIC);
        postRepository.save(post);

        return postMapper.postToPostResponseDto(post);
    }

    public PostResponseDto draftPost(String shortCode, PostRequest postRequest) {
        Post post = getPostByShortCode(shortCode);
        validateDraftPost(post);

        postMapper.updatePost(postRequest, post);
        post.setStatus(PostStatus.DRAFT);
        postRepository.save(post);

        return postMapper.postToPostResponseDto(post);
    }

    public void archivePost(String shortCode) {
        Post post = getPostByShortCode(shortCode);
        validateArchivePost(post);

        post.setStatus(PostStatus.ARCHIVED);
        postRepository.save(post);
    }


    public void updatePinnedState(String shortCode, UpdatePostPinRequest updatePostPinRequest) {
        Post post = getPostByShortCode(shortCode);
        validatePinnedPost(post);

        post.setPinned(updatePostPinRequest.isPinned());
        postRepository.save(post);
    }

    public Page<Post> getAllPostsByStatuses(List<PostStatus> statuses, Pageable pageable) {
        Profile profile = profileService.getCurrentProfile();
        Page<Post> postPage = Optional.ofNullable(statuses)
                .filter(list -> !list.isEmpty())
                .map(statusList -> postRepository.findAllByProfileAndStatusIn(profile, statuses, pageable))
                .orElseGet(() -> postRepository.findAllByProfile(profile, pageable));

        PaginationService.validatePage(postPage);
        return postPage;
    }

    public List<PostResponseDto> toPostResponseDto(List<Post> posts) {
        return postMapper.postListToPostResponseDtoList(posts);
    }

    public void deletePost(String shortCode){
        Post post = getPostByShortCode(shortCode);
        postRepository.delete(post);
    }


    private Post getPostByShortCode(String shortCode) {
        Profile profile = profileService.getCurrentProfile();
        return postRepository.findByProfileAndShortCode(profile, shortCode)
                .orElseThrow(() -> new NotFoundException(String.format("Post not found: %s", shortCode), "post"));
    }


    public Post getPostByProfileAndShortCode(Profile profile, String shortCode) {
        return postRepository.findByProfileAndShortCode(profile, shortCode)
                .orElseThrow(() -> new NotFoundException(String.format("Post not found: %s", shortCode), "post"));
    }

    private void validateDraftPost(Post post) {
        if (post.getStatus() != PostStatus.DRAFT) {
            throw new AccessDeniedException("Post cannot draft after published", "post");
        }
    }

    private void validateArchivePost(Post post) {
        if (post.getStatus() != PostStatus.PUBLIC && post.getStatus() != PostStatus.ARCHIVED) {
            throw new AccessDeniedException("Post cannot archive", "post");
        }
    }

    private void validatePinnedPost(Post post){
        if ((post.getStatus() != PostStatus.PUBLIC)) {
            throw new AccessDeniedException("Only public post can be pinned", "post");
        }
    }


}
