package api.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReviewRequest {
    private String text;
    private Integer rating;
}



