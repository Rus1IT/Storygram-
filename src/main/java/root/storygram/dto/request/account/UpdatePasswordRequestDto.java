package root.storygram.dto.request.account;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePasswordRequestDto {

    @NotBlank(message = "Current password is required")
    @Size(max = 255)
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Size(max = 255)
    private String newPassword;

    @NotBlank(message = "New password is required")
    @Size(max = 255)
    private String confirmPassword;

    @AssertTrue(message = "New password and confirm password do not match")
    public boolean isPasswordsMatch() {
        return newPassword.equals(confirmPassword);
    }
}
