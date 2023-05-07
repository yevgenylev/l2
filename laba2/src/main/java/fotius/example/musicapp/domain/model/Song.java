package fotius.example.musicapp.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Song {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String artist;
    private LocalDateTime addedAt;
    private String location;
    private Integer likes;
    private String lyrics;
}
