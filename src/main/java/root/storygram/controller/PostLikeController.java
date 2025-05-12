package root.storygram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import root.storygram.dto.response.ApiResponse;
import root.storygram.dto.response.PaginationMetaResponse;
import root.storygram.dto.response.PostLikeResponse;
import root.storygram.entity.PostLike;
import root.storygram.service.PaginationService;
import root.storygram.service.PostLikeService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/profile")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping("{username}/post/{postShortCode}/like")
    public ResponseEntity<?> likePost(@PathVariable String username,@PathVariable String postShortCode) {
        postLikeService.likePost(username, postShortCode);
        return ResponseEntity.status(CREATED)
                .body(ApiResponse.success(null, "Post liked successfully"));
    }

    @GetMapping("{username}/post/{postShortCode}/like")
    public ResponseEntity<?> getLikePost(@PathVariable String username, @PathVariable String postShortCode, Pageable pageable) {
        Page<PostLike> postLikePage = postLikeService.getPostLikePage(username, postShortCode, pageable);
        List<PostLikeResponse> postLikeResponseList = postLikeService.toPostLikeResponseList(postLikePage.getContent());
        PaginationMetaResponse pageMeta = PaginationService.toPaginationMetaResponse(postLikePage);

        return ResponseEntity.status(OK)
                .body(ApiResponse.success(postLikeResponseList, "Post likes retrieved successfully", pageMeta));
    }

    @DeleteMapping("{username}/post/{postShortCode}/unlike")
    public ResponseEntity<?> unlikePost(@PathVariable String username, @PathVariable String postShortCode) {
        postLikeService.unlikePost(username, postShortCode);
        return ResponseEntity.status(OK)
                .body(ApiResponse.success(null, "Post unliked successfully"));
    }
}
