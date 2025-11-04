package api.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MovieRequest {
    private String name;
    private String imageUrl;
    private Integer price;
    private String description;
    private String location;
    private Boolean published;
    private Integer genreId;
}
