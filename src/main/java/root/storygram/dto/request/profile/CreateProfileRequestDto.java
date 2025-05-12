package root.storygram.dto.request.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Data
public class CreateProfileRequestDto {

    @NotBlank(message = "Firstname is required")
    @Size(max = 100, message = "Firstname can't be more than 100 characters")
    private String firstName;

    @NotBlank(message = "Lastname is required")
    @Length(max = 100, message = "Lastname can't be more than 100 characters")
    private String lastName;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate birthDay;

    @Length(max = 500)
    private String bio;

}
