package root.storygram.controller;

import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import root.storygram.dto.response.ApiResponse;
import root.storygram.dto.response.PostMediaResponse;
import root.storygram.service.PostMediaService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class PostMediaController {
    private final PostMediaService postMediaService;

    @PostMapping("/post/media")
    public ResponseEntity<?> addPost(@RequestParam(name = "file") MultipartFile[] file) throws IOException {
        postMediaService.uploadMedia(file);
        return ResponseEntity.status(OK)
                .body(ApiResponse.success(null, "Post uploaded succesfully"));
    }

    @GetMapping("/post/{postShortCode}/media/{mediaShortCode}")
    public ResponseEntity<?> getPostMedia(@PathVariable(name = "postShortCode") String postShortCode, @PathVariable(name = "mediaShortCode") String mediaShortCode) throws IOException {
        try (InputStream inputStream = postMediaService.getMedia(null, postShortCode, mediaShortCode)) {
            byte[] imageBytes = IOUtils.toByteArray(inputStream);
            HttpHeaders headers = new HttpHeaders();

            String mediaType = postMediaService.getMediaType(null, postShortCode, mediaShortCode);
            headers.setContentType(MediaType.valueOf(mediaType));
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        }
    }

    @GetMapping("/{username}/post/{postShortCode}/media/{mediaShortCode}")
    public ResponseEntity<?> getPostMedia(@PathVariable(name = "username") String username, @PathVariable(name = "postShortCode") String postShortCode, @PathVariable(name = "mediaShortCode") String mediaShortCode) throws IOException {
        try (InputStream inputStream = postMediaService.getMedia(username, postShortCode, mediaShortCode)) {
            byte[] imageBytes = IOUtils.toByteArray(inputStream);
            HttpHeaders headers = new HttpHeaders();

            String mediaType = postMediaService.getMediaType(username, postShortCode, mediaShortCode);
            headers.setContentType(MediaType.valueOf(mediaType));
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        }
    }
}
