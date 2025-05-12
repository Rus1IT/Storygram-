package root.storygram.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UpdateTimestamp;
import root.storygram.enums.AccountRole;
import root.storygram.enums.AccountType;
import root.storygram.enums.LoginType;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

@Entity
@Table(name = "account")
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    @Setter(AccessLevel.NONE)
    private Long accountId = null;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    private Profile profile;

    @NotNull
    @Column(name = "username",nullable = false,unique = true,length = 50)
    private String username;

    @NotNull
    @NaturalId
    @Column(name = "login", nullable = false,unique = true,length = 100,updatable = false)
    private String login;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "login_type", nullable = false, updatable = false)
    private LoginType loginType;

    @NotNull
    @Column(name = "password", nullable = false,length = 255)
    private String password;


    @UpdateTimestamp
    @NotNull
    @Column(name = "refresh_at", nullable = false)
    private OffsetDateTime refreshAt;

    @CreationTimestamp
    @Column(name = "created_at",nullable = false, updatable = false)
    @Setter(value = AccessLevel.NONE)
    private OffsetDateTime createdAt = null;

    @NotNull
    @Column(name = "last_login_at", nullable = false)
    private OffsetDateTime lastLoginAt;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified = false;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private AccountRole role = AccountRole.USER;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType type = AccountType.OPEN;

    @PrePersist
    protected void onCreate() {
        refreshAt = OffsetDateTime.now();
        lastLoginAt = OffsetDateTime.now();
    }

}
