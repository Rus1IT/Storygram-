package root.storygram.dto.request.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUsernameRequestDto {

    @NotBlank(message = "newUsername is required")
    @Size(min = 2, max = 50)
    private String newUsername;
}
