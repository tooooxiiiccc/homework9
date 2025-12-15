package api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewResponse {
    private Long id;
    private String text;
    private Integer rating;
    private Long movieId;
    private Long userId;
}



