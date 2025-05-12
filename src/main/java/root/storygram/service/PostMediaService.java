package root.storygram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import root.storygram.entity.Post;
import root.storygram.entity.PostMedia;
import root.storygram.entity.Profile;
import root.storygram.exception.NotFoundException;
import root.storygram.repository.PostMediaRepository;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class PostMediaService {

    private final PostMediaRepository postMediaRepository;
    private final S3Service s3Service;
    private final UniqueIdService uniqueIdService;
    private final PostService postService;
    private final AccessControlService accessControlService;

    @Value("${spring.aws.folders.post}")
    private String folderName;

    public void uploadMedia(MultipartFile[] multipartFiles) throws IOException {
        validateFilesNotEmpty(multipartFiles);
        Post post = postService.getNewPost();

        for (int i = 0; i < multipartFiles.length; i++) {
            MultipartFile file = multipartFiles[i];
            validateFileNotEmpty(file);

            String fileExtension = FileService.getExtension(file.getContentType());
            String shortCode = uniqueIdService.generateUniqueShortId();
            String filename = String.format("%s/%s.%s", folderName, shortCode, fileExtension);
            s3Service.upload(file.getInputStream(), filename);

            PostMedia postMedia = PostMedia.builder().post(post).mediaType(file.getContentType()).mediaUrl(filename).shortCode(shortCode).position(i).build();
            postMediaRepository.save(postMedia);
        }
    }

    public InputStream getMedia(String username, String postShortCode, String mediaShortCode) {
        Profile profile = accessControlService.resolveProfile(username);
        Post post = postService.getPostByProfileAndShortCode(profile, postShortCode);
        accessControlService.validatePostAccess(profile, post);

        PostMedia postMedia = getPostMedia(profile, postShortCode, mediaShortCode);
        return s3Service.download(postMedia.getMediaUrl());
    }

    public String getMediaType(String username, String postShortCode, String mediaShortCode) {
        Profile profile = accessControlService.resolveProfile(username);
        PostMedia postMedia = getPostMedia(profile, postShortCode, mediaShortCode);
        return postMedia.getMediaType();
    }

    private PostMedia getPostMedia(Profile profile, String postShortCode, String mediaShortCode) {
        Post post = postService.getPostByProfileAndShortCode(profile, postShortCode);
        return postMediaRepository.findByPostAndShortCode(post, mediaShortCode).orElseThrow(() -> new NotFoundException(String.format("Post %s media %s not found", postShortCode, mediaShortCode), "postMedia"));
    }

    private void validateFilesNotEmpty(MultipartFile[] multipartFiles) {
        if (multipartFiles == null || multipartFiles.length == 0) {
            throw new IllegalArgumentException("No files to be uploaded");
        }
    }

    private void validateFileNotEmpty(MultipartFile multipartFile) {
        if (multipartFile == null) {
            throw new IllegalArgumentException("No files to be uploaded");
        }
    }
}
