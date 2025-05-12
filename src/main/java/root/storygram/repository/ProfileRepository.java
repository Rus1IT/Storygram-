package root.storygram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import root.storygram.entity.Profile;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByAccountUsername(String username);
    boolean existsByProfilePictureUrl(String username);
}
