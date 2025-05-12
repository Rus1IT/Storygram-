package root.storygram.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import root.storygram.dto.request.profilelink.CreateProfileLinkRequest;
import root.storygram.dto.response.ApiResponse;
import root.storygram.dto.response.PaginationMetaResponse;
import root.storygram.dto.response.ProfileLinkData;
import root.storygram.entity.ProfileLink;
import root.storygram.service.PaginationService;
import root.storygram.service.ProfileLinkService;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/v1/profile/link")
public class ProfileLinkController {

    private final ProfileLinkService profileLinkService;

    @PostMapping
    public ResponseEntity<?> addProfileLink(@Valid @RequestBody CreateProfileLinkRequest profileLinkRequest) {
        profileLinkService.addLink(profileLinkRequest);
        return ResponseEntity.status(CREATED)
                .body(ApiResponse.success(null, "Profile link created successfully"));
    }

    @GetMapping
    public ResponseEntity<?> getProfileLink(Pageable pageable){
        Page<ProfileLink> profileLinkPage = profileLinkService.getLinkList(pageable);
        List<ProfileLinkData> profileLinkDataList = profileLinkService.toProfileLinkData(profileLinkPage.getContent());
        PaginationMetaResponse pageMeta = PaginationService.toPaginationMetaResponse(profileLinkPage);

        return ResponseEntity.status(OK)
                .body(ApiResponse.success(profileLinkDataList, "Profile links retrieved successfully", pageMeta));
    }

    @PutMapping
    public ResponseEntity<?> updateProfileLink(@Valid @RequestBody CreateProfileLinkRequest profileLinkRequest){
        profileLinkService.updateLink(profileLinkRequest);
        return ResponseEntity.status(OK)
                .body(ApiResponse.success(null, "Profile link updated successfully"));
    }

    @DeleteMapping("/{linkTitle}")
    public ResponseEntity<?> deleteProfileLink(@PathVariable String linkTitle){
        profileLinkService.deleteLink(linkTitle);
        return ResponseEntity.status(OK)
                .body(ApiResponse.success(null, "Profile link deleted successfully"));
    }
}
