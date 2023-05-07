package fotius.example.musicapp.domain.error;

public class SongIsAlreadyInPlaylistException extends MusicAppException {
    public SongIsAlreadyInPlaylistException(Long playlistId, Long songId) {
        super("Song '%d' is already in the playlist '%d'".formatted(songId, playlistId));
    }
}
