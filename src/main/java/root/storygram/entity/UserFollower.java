package root.storygram.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import root.storygram.enums.FollowingRequestStatus;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_follower", uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "following_id"}))
public class UserFollower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Builder.Default
    @Column(name = "id")
    private Long id = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id",referencedColumnName = "profile_id", nullable = false)
    private Profile follower;

    @ManyToOne
    @JoinColumn(name = "following_id",referencedColumnName = "profile_id",nullable = false)
    private Profile following;

    @NotNull
    @Column(name = "request_status",nullable = false)
    @Enumerated(EnumType.STRING)
    private FollowingRequestStatus requestStatus;

    @CreationTimestamp
    @Column(name = "created_at",nullable = false, updatable = false)
    @Setter(value = AccessLevel.NONE)
    @Builder.Default
    private OffsetDateTime createdAt = null;


}
