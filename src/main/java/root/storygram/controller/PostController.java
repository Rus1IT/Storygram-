package root.storygram.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import root.storygram.dto.request.UpdatePostPinRequest;
import root.storygram.dto.request.post.PostRequest;
import root.storygram.dto.response.ApiResponse;
import root.storygram.dto.response.PaginationMetaResponse;
import root.storygram.dto.response.PostResponseDto;
import root.storygram.entity.Post;
import root.storygram.enums.PostStatus;
import root.storygram.service.PaginationService;
import root.storygram.service.PostService;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class PostController {
    private final PostService postService;

    @GetMapping("/post")
    public ResponseEntity<?> getAllPosts(@RequestParam(value = "status", required = false)List<PostStatus> postStatuses, Pageable pageable) {
        Page<Post> postPage = postService.getAllPostsByStatuses(postStatuses, pageable);
        List<PostResponseDto> posts = postService.toPostResponseDto(postPage.getContent());
        PaginationMetaResponse pageMeta = PaginationService.toPaginationMetaResponse(postPage);
        return ResponseEntity.status(OK)
                .body(ApiResponse.success(posts, "Posts successfully retrieved", pageMeta));
    }

    @GetMapping("{username}/post/{shortCode}")
    public ResponseEntity<?> getPostByShortCode(@PathVariable(name = "username", required = false) String username, @PathVariable String shortCode) {
        PostResponseDto postResponseDto = postService.getPostResponse(username, shortCode);
        return ResponseEntity.status(OK)
                .body(ApiResponse.success(postResponseDto, "Post successfully retrieved"));
    }

    @GetMapping("/post/{shortCode}")
    public ResponseEntity<?> getCurrentPostByShortCode(@PathVariable String shortCode) {
        PostResponseDto postResponseDto = postService.getPostResponse(null, shortCode);
        return ResponseEntity.status(OK)
                .body(ApiResponse.success(postResponseDto, "Post successfully retrieved"));
    }

    @PutMapping("/post/{shortCode}/publish")
    public ResponseEntity<?> publishPost(@PathVariable String shortCode, @Valid @RequestBody PostRequest postRequest) {
        PostResponseDto postResponseDto = postService.publishPost(shortCode, postRequest);
        return ResponseEntity.status(OK)
                .body(ApiResponse.success(postResponseDto, "Post successfully published"));
    }

    @PutMapping("/post/{shortCode}/draft")
    public ResponseEntity<?> draftPost(@PathVariable String shortCode, @Valid @RequestBody PostRequest postRequest) {
        PostResponseDto postResponseDto = postService.draftPost(shortCode, postRequest);
        return ResponseEntity.status(OK)
                .body(ApiResponse.success(postResponseDto, "Post successfully drafted"));
    }

    @PatchMapping("/{shortCode}/archive")
    public ResponseEntity<ApiResponse<Void>> archivePost(@PathVariable String shortCode) {
        postService.archivePost(shortCode);
        return ResponseEntity.status(OK)
                .body(ApiResponse.success(null, "Post successfully archived"));
    }

    @PatchMapping("/post/{shortCode}")
    public ResponseEntity<ApiResponse<Void>> updatePostPinnedState(
            @PathVariable String shortCode,
            @Valid @RequestBody UpdatePostPinRequest request) {

        postService.updatePinnedState(shortCode, request);
        return ResponseEntity.ok(ApiResponse.success(null, "Post pinned state successfully updated"));
    }

    @DeleteMapping("/post/{shortCode}")
    public ResponseEntity<?> deletePost(@PathVariable String shortCode) {
        postService.deletePost(shortCode);
        return ResponseEntity.status(OK)
                .body(ApiResponse.success(null, "Post successfully deleted"));
    }


}
