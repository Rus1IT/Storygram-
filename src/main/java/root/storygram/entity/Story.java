package root.storygram.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import root.storygram.enums.MediaType;
import root.storygram.enums.Visibility;

import java.time.OffsetDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "story")
@Builder
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(value = AccessLevel.NONE)
    @Builder.Default
    @Column(name = "story_id")
    private Long storyId = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id",referencedColumnName = "profile_id",nullable = false)
    private Profile profile;

    @NotNull
    @Column(name = "media_url",nullable = false)
    private String mediaUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "media_type",nullable = false)
    private MediaType mediaType;

    @CreationTimestamp
    @Column(name = "created_at",nullable = false, updatable = false)
    @Setter(value = AccessLevel.NONE)
    @Builder.Default
    private OffsetDateTime createdAt = null;

    @Column(name = "story_text")
    private String storyText;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "visibility",nullable = false)
    private Visibility visibility;

    @NotNull
    @Column(name = "expiry_at",nullable = false)
    private OffsetDateTime expiryAt;

    @Column(name = "is_pinned")
    private boolean isPinned;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "highlight_id",referencedColumnName = "highlight_id")
    private Highlight highlight;





}
