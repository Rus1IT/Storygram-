package root.storygram.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "post_comment")
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    @Setter(AccessLevel.NONE)
    @Builder.Default
    private Long commentId = null;

    @Column(name = "short_code", updatable = false, nullable = false)
    @Setter(AccessLevel.NONE)
    private String shortCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id",referencedColumnName = "post_id",nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id",referencedColumnName = "profile_id",nullable = false)
    private Profile profile;

    @Column(name = "text", nullable = false)
    private String text;

    @CreationTimestamp
    @Setter(AccessLevel.NONE)
    @Builder.Default
    @Column(name = "created_at", nullable = false,updatable = false)
    private OffsetDateTime createdAt = null;

    @UpdateTimestamp
    @Setter(AccessLevel.NONE)
    @Builder.Default
    @Column(name = "updated_at",nullable = false)
    private OffsetDateTime updatedAt = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private PostComment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<PostComment> replies = new ArrayList<>();
}
