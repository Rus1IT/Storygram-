package root.storygram.dto.response;

import lombok.Data;
import root.storygram.enums.AccountRole;
import root.storygram.enums.AccountType;

import java.time.OffsetDateTime;

@Data
public class AccountData {

    private String username;
    private String login;
    private String loginType;
    private String password;
    private OffsetDateTime refreshAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastLoginAt;
    private boolean isActive;
    private boolean isVerified;
    private AccountRole role;
    private AccountType type;
}
