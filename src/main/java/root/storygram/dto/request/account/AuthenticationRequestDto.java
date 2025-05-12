package root.storygram.dto.request.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import root.storygram.enums.LoginType;

@Data
public class AuthenticationRequestDto {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Login is required")
    private String login;

    @NotNull(message = "Login type is required")
    private LoginType loginType;

    @NotBlank(message = "Password is required")
    private String password;

}
