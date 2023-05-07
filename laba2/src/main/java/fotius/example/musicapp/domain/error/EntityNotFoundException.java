package fotius.example.musicapp.domain.error;

public class EntityNotFoundException extends MusicAppException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
