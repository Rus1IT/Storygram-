package root.storygram.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import root.storygram.enums.MediaType;

import java.time.OffsetDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "post_media")
public class PostMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "media_id")
    @Setter(AccessLevel.NONE)
    @Builder.Default
    private Long mediaId = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id",referencedColumnName = "post_id", nullable = false)
    private Post post;

    @NotNull
    @Column(name = "media_url", nullable = false)
    private String mediaUrl;

    @NotNull
    @Column(name = "short_code", nullable = false)
    private String shortCode;

    @NotNull
    @Column(name = "media_type", nullable = false)
    private String mediaType;

    @NotNull
    @Column(name = "position", nullable = false)
    private Integer position;

    @CreationTimestamp
    @Setter(AccessLevel.NONE)
    @Builder.Default
    @Column(name = "created_at", nullable = false,updatable = false)
    private OffsetDateTime createdAt = null;
}
