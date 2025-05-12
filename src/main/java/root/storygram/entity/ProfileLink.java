package root.storygram.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profile_link")
public class ProfileLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "link_id")
    @Setter(AccessLevel.NONE)
    @Builder.Default
    private Long linkId = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id",referencedColumnName = "profile_id",nullable = false)
    private Profile profile;

    @NotNull
    @Column(name = "link_url",nullable = false)
    private String linkUrl;

    @NotNull
    @Column(name = "link_title",nullable = false)
    private String linkTitle;

    @CreationTimestamp
    @Column(name = "created_at",nullable = false, updatable = false)
    @Setter(value = AccessLevel.NONE)
    @Builder.Default
    private OffsetDateTime createdAt = null;
}
