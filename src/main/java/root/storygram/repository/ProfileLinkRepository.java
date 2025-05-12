package root.storygram.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import root.storygram.entity.Profile;
import root.storygram.entity.ProfileLink;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileLinkRepository extends JpaRepository<ProfileLink, Long> {

    Optional<ProfileLink> findByProfileAndLinkTitle(Profile profile, String linkTitle);

    Page<ProfileLink> findAllByProfile(Profile profile, Pageable pageable);
}
