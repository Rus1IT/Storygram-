package root.storygram.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import root.storygram.enums.PostStatus;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Builder.Default
    @Column(name = "post_id")
    private Long postId = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id",referencedColumnName = "profile_id", nullable = false)
    private Profile profile;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PostLike> likes;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PostMedia> postMedias;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PostComment> comments;

    @Column(name = "text")
    private String text;

    @CreationTimestamp
    @Column(name = "created_at",nullable = false, updatable = false)
    @Setter(value = AccessLevel.NONE)
    @Builder.Default
    private OffsetDateTime createdAt = null;

    @NotNull
    @UpdateTimestamp
    @Column(name = "updated_at",nullable = false)
    @Builder.Default
    private OffsetDateTime updatedAt = OffsetDateTime.now();


    @Column(name = "post_status",nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PostStatus status;

    @Column(name = "is_pinned")
    @Builder.Default
    private boolean isPinned = false;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "short_code")
    private String shortCode;

}
