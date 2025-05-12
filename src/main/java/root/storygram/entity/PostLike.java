package root.storygram.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "post_like")
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Builder.Default
    @Column(name = "like_id")
    private Long likeId = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id",referencedColumnName = "post_id",nullable = false, unique = true)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id",referencedColumnName = "profile_id",nullable = false, unique = true)
    private Profile profile;

    @CreationTimestamp
    @Setter(AccessLevel.NONE)
    @Builder.Default
    @Column(name = "liked_at",nullable = false,updatable = false)
    private OffsetDateTime likedAt = null;


}
