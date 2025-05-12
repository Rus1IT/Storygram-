package root.storygram.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "highlight")
@Builder
public class Highlight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Builder.Default
    @Column(name = "highlight_id")
    private Long highlightId = null;

    @ManyToOne
    @JoinColumn(name = "profile_id",referencedColumnName = "profile_id",nullable = false)
    private Profile profile;

    @OneToMany(mappedBy = "highlight", cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Story> stories;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "picture_url", nullable = false)
    private String pictureUrl;

    @CreationTimestamp
    @Column(name = "created_at",nullable = false, updatable = false)
    @Setter(value = AccessLevel.NONE)
    @Builder.Default
    private OffsetDateTime createdAt = null;

}
