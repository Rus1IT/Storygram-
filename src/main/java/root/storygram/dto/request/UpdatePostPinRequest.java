package root.storygram.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdatePostPinRequest {

    @NotNull
    private boolean isPinned;
}
