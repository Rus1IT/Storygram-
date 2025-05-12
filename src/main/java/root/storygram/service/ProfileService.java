package root.storygram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import root.storygram.dto.response.ProfileData;
import root.storygram.exception.AlreadyExistException;
import root.storygram.security.SecurityUtil;
import root.storygram.dto.request.profile.CreateProfileRequestDto;
import root.storygram.dto.request.profile.UpdateProfileRequestDto;
import root.storygram.entity.Account;
import root.storygram.entity.Profile;
import root.storygram.exception.NotFoundException;
import root.storygram.mapper.ProfileMapper;
import root.storygram.repository.ProfileRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final AccountService accountService;
    private final ProfileMapper profileMapper;
    private final S3Service s3Service;
    private final UniqueIdService uniqueIdService;

    @Value("${spring.aws.folders.profileImage}")
    private String PROFILE_PICTURE_FOLDER;

    public void addProfile(CreateProfileRequestDto createProfileRequestDto) {
        Account account = accountService.getCurrentAccount();

        Profile profile = profileMapper.createProfileRequestDtoToProfile(createProfileRequestDto);
        profile.setAccount(account);
        profileRepository.save(profile);
    }

    public void addProfileImage(MultipartFile file) throws IOException {
        validateUniqueField(file.getOriginalFilename(), profileRepository::existsByProfilePictureUrl);

        String fileExtension = FileService.getImageExtension(file.getContentType());
        String fileName = String.format("%s/%s.%s", PROFILE_PICTURE_FOLDER, uniqueIdService.generateUniqueId(), fileExtension);
        s3Service.upload(file.getInputStream(), fileName);

        Profile profile = getCurrentProfile();
        profile.setProfilePictureUrl(fileName);
        profileRepository.save(profile);
    }

    public InputStream getProfileImage(String username) {
        Profile profile = getProfileByUsername(username);
        validateProfilePicture(profile.getProfilePictureUrl());

        return s3Service.download(profile.getProfilePictureUrl());
    }

    public InputStream getCurrentProfileImage(){
        Profile profile = getCurrentProfile();
        validateProfilePicture(profile.getProfilePictureUrl());

        return s3Service.download(profile.getProfilePictureUrl());
    }

    public void deleteProfileImage(){
        Profile profile = getCurrentProfile();
        validateProfilePicture(profile.getProfilePictureUrl());

        s3Service.delete(profile.getProfilePictureUrl());
        profile.setProfilePictureUrl(null);
        profileRepository.save(profile);
    }

    public void updateProfile(UpdateProfileRequestDto updateProfileRequestDto) {
        Profile profile = getCurrentProfile();

        profileMapper.updateProfileRequestDtoToProfile(updateProfileRequestDto, profile);
        profileRepository.save(profile);
    }

    public void updateProfilePicture(MultipartFile file) throws IOException {
        FileService.validationImageExtension(Objects.requireNonNull(file.getContentType()));

        String fileName = getCurrentProfile().getProfilePictureUrl();
        s3Service.update(file.getInputStream(), fileName);
    }

    public ProfileData getCurrentDataProfile() {
        Profile profile = getCurrentProfile();

        ProfileData profileData = profileMapper.createProfileToProfileData(profile);
        addMetaData(profile, profileData);
        return profileData;
    }

    public Profile getCurrentProfile() {
        String username = SecurityUtil.getCurrentUsername();
        return getProfileByUsername(username);
    }

    public ProfileData getProfileData(String username){
        Profile profile = getProfileByUsername(username);

        ProfileData profileData = profileMapper.createProfileToProfileData(profile);
        addMetaData(profile, profileData);
        return profileData;
    }

    public Profile getProfile(String username) {
        return getProfileByUsername(username);
    }

    public void deleteProfile() {
        Profile profile = getCurrentProfile();
        profileRepository.delete(profile);
    }


    private Profile getProfileByUsername(String username) {
        return profileRepository.findByAccountUsername(username)
                .orElseThrow(() -> new NotFoundException(String.format("Profile %s not found", username), "profile"));
    }

    private void validateUniqueField(String value,
                                     Predicate<String> existsCheck) throws AlreadyExistException {
        if(existsCheck.test(value)){
            throw new AlreadyExistException(String.format("%s %s already exist", "picture", value), "picture");
        }

    }

    private void addMetaData(Profile profile, ProfileData profileData) {
        profileData.setFollowerCount(profile.getFollowers().size());
        profileData.setFollowingCount(profile.getFollowing().size());
        profileData.setPostCount(profile.getPosts().size());
        profileData.setProfilePictureUrl(profile.getProfilePictureUrl());
    }

    private void validateProfilePicture(String profilePicture) {
        if(profilePicture == null){
            throw new NotFoundException("Profile image not found", "picture");
        }
    }
}
