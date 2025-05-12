package root.storygram.dto.request.account;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthorizationRequestDto {

    @NotBlank(message = "Login or username is required")
    @JsonProperty("account")
    @JsonAlias({"username", "login"})
    private String account;

    @NotBlank(message = "Password is required")
    private String password;
}
