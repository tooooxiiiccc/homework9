package db.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Movie {
    private Long id;
    private String name;
    private Integer price;
    private String description;
    private String imageUrl;
    private String location;
    private Boolean published;
    private Double rating;
    private Long genreId;
    private LocalDateTime createdAt;

}
