package root.storygram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import root.storygram.dto.request.profilelink.CreateProfileLinkRequest;
import root.storygram.dto.response.ProfileLinkData;
import root.storygram.entity.Profile;
import root.storygram.entity.ProfileLink;
import root.storygram.exception.AlreadyExistException;
import root.storygram.exception.NotFoundException;
import root.storygram.mapper.ProfileLinkMapper;
import root.storygram.repository.ProfileLinkRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileLinkService {

    private final ProfileLinkRepository profileLinkRepository;
    private final ProfileService profileService;
    private final ProfileLinkMapper profileLinkMapper;

    public void addLink(CreateProfileLinkRequest profileLinkRequest){
        Profile profile = profileService.getCurrentProfile();
        validateProfileLink(profileLinkRequest.getLinkTitle());

        ProfileLink profileLink = profileLinkMapper.createProfileLinkRequestToProfileLink(profileLinkRequest);
        profileLink.setProfile(profile);
        profileLinkRepository.save(profileLink);
    }

    public void updateLink(CreateProfileLinkRequest profileLinkRequest){
        ProfileLink profileLink = getLink(profileLinkRequest.getLinkTitle());
        profileLinkMapper.updateProfileLinkRequestToProfileLink(profileLinkRequest, profileLink);
        profileLinkRepository.save(profileLink);
    }

    public Page<ProfileLink> getLinkList(Pageable pageable){
        Profile profile = profileService.getCurrentProfile();
        Page<ProfileLink> profileLinkPage = profileLinkRepository.findAllByProfile(profile, pageable);

        PaginationService.validatePage(profileLinkPage);
        return profileLinkPage;
    }

    public List<ProfileLinkData> toProfileLinkData(List<ProfileLink> profileLinkList){
        return profileLinkMapper.profileLinkListToProfileDataList(profileLinkList);
    }

    public void deleteLink(String linkTitle){
        ProfileLink profileLink = getLink(linkTitle);
        profileLinkRepository.delete(profileLink);
    }


    private ProfileLink getLink(String linkTitle){
        Profile profile = profileService.getCurrentProfile();
        return profileLinkRepository.findByProfileAndLinkTitle(profile,linkTitle)
                .orElseThrow(() -> new NotFoundException(String.format("Profile link %s not found", linkTitle), "link"));
    }

    private void validateProfileLink(String linkTitle){
        Profile profile = profileService.getCurrentProfile();
        if(profileLinkRepository.findByProfileAndLinkTitle(profile, linkTitle).isPresent()){
            throw new AlreadyExistException(String.format("Profile link %s already exist", linkTitle), "link");
        }
    }

}
