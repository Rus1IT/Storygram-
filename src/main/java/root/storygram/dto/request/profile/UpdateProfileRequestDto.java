package root.storygram.dto.request.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UpdateProfileRequestDto {

    @NotBlank(message = "Firstname is required")
    @Size(max = 100, message = "Firstname can't be more than 100 characters")
    private String firstName;

    @NotBlank(message = "Lastname is required")
    @Length(max = 100, message = "Lastname can't be more than 100 characters")
    private String lastName;

    @Length(max = 500)
    private String bio;

}
