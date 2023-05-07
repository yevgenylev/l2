package fotius.example.musicapp.domain.error;

public class SongNotFoundException extends EntityNotFoundException {
    public SongNotFoundException(Long id) {
        super("Song with id '%d' not found".formatted(id));
    }
}
