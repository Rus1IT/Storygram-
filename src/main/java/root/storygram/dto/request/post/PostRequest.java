package root.storygram.dto.request.post;


import java.math.BigDecimal;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PostRequest {

    @Size(min = 1, max = 200, message = "Text must be between 1 and 200 characters")
    private String text;

    @DecimalMin(value = "-90.0", message = "Latitude must be at least -90")
    @DecimalMax(value = "90.0", message = "Latitude must be at most 90")
    private BigDecimal latitude;

    @DecimalMin(value = "-180.0", message = "Longitude must be at least -180")
    @DecimalMax(value = "180.0", message = "Longitude must be at most 180")
    private BigDecimal longitude;
}
