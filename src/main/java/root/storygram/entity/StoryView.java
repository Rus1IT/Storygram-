package root.storygram.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "story_view")
@Builder
public class StoryView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Builder.Default
    @Column(name = "view_id")
    private Long viewId = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id",referencedColumnName = "story_id", nullable = false)
    private Story story;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id",referencedColumnName = "profile_id", nullable = false)
    private Profile profile;

    @CreationTimestamp
    @Column(name = "created_at",nullable = false, updatable = false)
    @Setter(value = AccessLevel.NONE)
    @Builder.Default
    private OffsetDateTime createdAt = null;

}
