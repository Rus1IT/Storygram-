package root.storygram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import root.storygram.dto.response.ApiResponse;
import root.storygram.dto.response.PaginationMetaResponse;
import root.storygram.dto.response.UserFollowerRequest;
import root.storygram.entity.UserFollower;
import root.storygram.service.PaginationService;
import root.storygram.service.UserFollowerService;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/profile")
public class UserFollowerController {

    private final UserFollowerService userFollowerService;

    @PostMapping("/follow/{username}")
    public ResponseEntity<?> followTo(@PathVariable String username) {
        userFollowerService.followTo(username);
        return ResponseEntity.status(CREATED)
                .body(ApiResponse.success(null, String.format("Send following request to %s",username)));
    }

    @PatchMapping("/accept/{username}")
    public ResponseEntity<?> acceptFollower(@PathVariable String username) {
        userFollowerService.acceptFollowingRequest(username);
        return ResponseEntity.status(OK)
                .body(ApiResponse.success(null, String.format("Following request from %s accepted",username)));
    }

    @DeleteMapping("/reject/{username}")
    public ResponseEntity<?> rejectFollower(@PathVariable String username) {
        userFollowerService.rejectFollowingRequest(username);
        return ResponseEntity.status(OK)
                .body(ApiResponse.success(null, String.format("Following request from %s rejected",username)));
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<?> deleteFollower(@PathVariable String username) {
        userFollowerService.deleteFollower(username);
        return ResponseEntity.status(OK)
                .body(ApiResponse.success(null, String.format("Follow %s deleted",username)));
    }

    @GetMapping("/followers")
    public ResponseEntity<?> getFollowers(Pageable pageable){
        Page<UserFollower> userFollowerRequestPage = userFollowerService.getFollowerRequestPage(null, pageable);
        List<UserFollowerRequest> userFollowerRequestList = userFollowerService.toUserFollowerRequests(null, userFollowerRequestPage.getContent());
        PaginationMetaResponse pageMeta = PaginationService.toPaginationMetaResponse(userFollowerRequestPage);

        return ResponseEntity.status(OK)
                .body(ApiResponse.success(userFollowerRequestList, "Followers successfully retrieved", pageMeta));
    }

    @GetMapping("/{username}/followers")
    public ResponseEntity<?> getFollowers(@PathVariable String username, Pageable pageable){
        Page<UserFollower> userFollowerRequestPage = userFollowerService.getFollowerRequestPage(username, pageable);
        List<UserFollowerRequest> userFollowerRequestList = userFollowerService.toUserFollowerRequests(username, userFollowerRequestPage.getContent());
        PaginationMetaResponse pageMeta = PaginationService.toPaginationMetaResponse(userFollowerRequestPage);

        return ResponseEntity.status(OK)
                .body(ApiResponse.success(userFollowerRequestList, "Followers successfully retrieved", pageMeta));
    }

    @GetMapping("/followings")
    public ResponseEntity<?> getFollowings(Pageable pageable){
        Page<UserFollower> userFollowerRequestPage = userFollowerService.getFollowingRequestPage(null, pageable);
        List<UserFollowerRequest> userFollowingRequestList = userFollowerService.toUserFollowingRequest(null, userFollowerRequestPage.getContent());
        PaginationMetaResponse pageMeta = PaginationService.toPaginationMetaResponse(userFollowerRequestPage);

        return ResponseEntity.status(OK)
                .body(ApiResponse.success(userFollowingRequestList, "Followings successfully retrieved", pageMeta));
    }

    @GetMapping("/{username}/followings")
    public ResponseEntity<?> getFollowings(@PathVariable String username, Pageable pageable){
        Page<UserFollower> userFollowerRequestPage = userFollowerService.getFollowingRequestPage(username, pageable);
        List<UserFollowerRequest> userFollowingRequestList = userFollowerService.toUserFollowingRequest(username, userFollowerRequestPage.getContent());
        PaginationMetaResponse pageMeta = PaginationService.toPaginationMetaResponse(userFollowerRequestPage);

        return ResponseEntity.status(OK)
                .body(ApiResponse.success(userFollowingRequestList, "Followings successfully retrieved", pageMeta));
    }

    @DeleteMapping("/unfollow/{username}")
    public ResponseEntity<?> unfollowFrom(@PathVariable String username){
        userFollowerService.unfollowFrom(username);
        return ResponseEntity.status(OK)
                .body(ApiResponse.success(null, "Unfollowed from " + username));
    }


}
