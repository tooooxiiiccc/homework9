package api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieResponse {
    private Long id;
    private String name;
    private Integer price;
    private String description;
    private String imageUrl;
    private String location;
    private Boolean published;
    private Integer genreId;
    private Double rating;
}
