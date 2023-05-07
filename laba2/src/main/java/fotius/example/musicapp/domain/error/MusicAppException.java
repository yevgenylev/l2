package fotius.example.musicapp.domain.error;

public class MusicAppException extends RuntimeException {
    public MusicAppException(String message) {
        super(message);
    }

    public MusicAppException(String message, Throwable cause) {
        super(message, cause);
    }
}
