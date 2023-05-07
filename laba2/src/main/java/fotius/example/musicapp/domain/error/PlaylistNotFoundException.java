package fotius.example.musicapp.domain.error;

public class PlaylistNotFoundException extends EntityNotFoundException {
    public PlaylistNotFoundException(Long id) {
        super("Playlist with id '%d' not found".formatted(id));
    }
}
