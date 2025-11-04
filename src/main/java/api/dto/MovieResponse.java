package api.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MovieResponse {
    private Long id;
    private String name;
    private Integer price;
    private String description;
    private String imageUrl;
    private String location;
    private Boolean published;
    private Integer genreId;
}
