package root.storygram.controller;

import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import root.storygram.dto.request.profile.CreateProfileRequestDto;
import root.storygram.dto.request.profile.UpdateProfileRequestDto;
import root.storygram.dto.response.ApiResponse;
import root.storygram.dto.response.ProfileData;
import root.storygram.service.ProfileService;

import java.io.IOException;
import java.io.InputStream;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/{username}")
    public ResponseEntity<?> getProfile(@PathVariable(value = "username", required = false) String username) {
        ProfileData profileData = profileService.getProfileData(username);
        return ResponseEntity.status(OK).body(ApiResponse.success(profileData, "Profile retrieved successfully"));
    }

    @GetMapping
    public ResponseEntity<?> getCurrentProfile() {
        ProfileData profileData = profileService.getCurrentDataProfile();
        return ResponseEntity.status(OK).body(ApiResponse.success(profileData, "Profile retrieved successfully"));
    }

    @GetMapping("/image/{username}")
    public ResponseEntity<byte[]> downloadProfileImage(@PathVariable String username) throws IOException {
        try (InputStream inputStream = profileService.getProfileImage(username)) {
            byte[] imageBytes = IOUtils.toByteArray(inputStream);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        }
    }

    @GetMapping("/image")
    public ResponseEntity<byte[]> downloadCurrentProfileImage() throws IOException {
        try (InputStream inputStream = profileService.getCurrentProfileImage()) {
            byte[] imageBytes = IOUtils.toByteArray(inputStream);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<?> createProfile(@RequestBody CreateProfileRequestDto createProfileRequestDto) {
        profileService.addProfile(createProfileRequestDto);
        return ResponseEntity.status(CREATED).body(ApiResponse.success(null, "Profile created successfully"));
    }

    @PostMapping("/image")
    public ResponseEntity<?> addImage(@RequestParam("file") MultipartFile file) throws IOException {
        profileService.addProfileImage(file);
        return ResponseEntity.status(OK).body(ApiResponse.success(null, "Profile image added successfully"));
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequestDto updateProfileRequestDto) {
        profileService.updateProfile(updateProfileRequestDto);
        return ResponseEntity.status(OK).body(ApiResponse.success(null, "Profile updated successfully"));
    }

    @PutMapping("/image")
    public ResponseEntity<?> updateProfilePicture(@RequestParam("file") MultipartFile file) throws IOException {
        profileService.updateProfilePicture(file);
        return ResponseEntity.status(OK).body(ApiResponse.success(null, "Profile image updated successfully"));
    }

    @DeleteMapping("/image")
    public ResponseEntity<?> deleteProfileImage() {
        profileService.deleteProfileImage();
        return ResponseEntity.status(OK).body(ApiResponse.success(null, "Profile image deleted successfully"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProfile() {
        profileService.deleteProfile();
        return ResponseEntity.status(OK).body(ApiResponse.success(null, "Profile deleted successfully"));
    }

}
