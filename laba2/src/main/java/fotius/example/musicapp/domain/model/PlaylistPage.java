package fotius.example.musicapp.domain.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistPage {

    private List<Playlist> content;
    private int pageNumber;
    private String nextPageUrl;
    private String previousPageUrl;
}
