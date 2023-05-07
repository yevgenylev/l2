package fotius.example.musicapp.domain;

import fotius.example.musicapp.domain.error.InvalidRequestException;
import fotius.example.musicapp.domain.error.PlaylistNotFoundException;
import fotius.example.musicapp.domain.error.SongIsAlreadyInPlaylistException;
import fotius.example.musicapp.domain.model.Playlist;
import fotius.example.musicapp.domain.model.Song;
import fotius.example.musicapp.domain.persistence.PlaylistRepository;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    @Data
    @Builder
    @AllArgsConstructor
    public static class CreatePlaylistCommand {

        @NotBlank(message = "Name is required to be provided")
        @Size(max = 100, message = "Name must not be longer than 100 chars")
        private String name;

        @NotBlank(message = "Author is required to be provided")
        @Size(max = 50, message = "Author must not be longer than 50 chars")
        private String author;
    }

    private final SongService songService;
    private final PlaylistRepository repository;
    private final Validator validator;

    @Transactional
    public Playlist create(CreatePlaylistCommand command) {
        final var violations = validator.validate(command);
        if (!violations.isEmpty()) {
            throw new InvalidRequestException("Failed to create playlist", violations);
        }
        final Playlist playlist = Playlist.builder()
            .name(command.name)
            .author(command.author)
            .createdAt(LocalDateTime.now())
            .build();
        repository.save(playlist);
        return playlist;
    }

    @Transactional
    public Playlist addSong(Long playlistId, Long songId) {
        final Playlist playlist = getById(playlistId);
        final Song song = songService.getById(songId);
        if (playlist.getSongs().contains(song)) {
            throw new SongIsAlreadyInPlaylistException(playlistId, songId);
        }
        playlist.getSongs().add(song);
        repository.save(playlist);
        return playlist;
    }

    @Transactional
    public Playlist deleteSong(Long playlistId, Long songId) {
        final Playlist playlist = getById(playlistId);
        final Song song = songService.getById(songId);
        playlist.getSongs().remove(song);
        repository.save(playlist);
        return playlist;
    }

    @Transactional
    public void delete(Long playlistId) {
        repository.findById(playlistId).ifPresent(repository::delete);
    }

    public Playlist getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new PlaylistNotFoundException(id));
    }

    public Slice<Playlist> findAllOrderedByCreatedAt(int page, int perPage, Sort.Direction order) {
        return repository.findAll(
            PageRequest.of(
                page,
                perPage,
                Sort.by(order, "createdAt")
            )
        );
    }

    public Slice<Playlist> findByAuthorOrderedByCreatedAt(
        String author,
        int page,
        int perPage,
        Sort.Direction order
    ) {
        return repository.findByAuthor(
            author,
            PageRequest.of(
                page,
                perPage,
                Sort.by(order, "createdAt")
            )
        );
    }

    @EventListener(BeforeSongIsDeleted.class)
    void removeSongFromAllPlaylists(BeforeSongIsDeleted event) {
        for (Playlist playlist : repository.findBySongs(event.song())) {
            playlist.getSongs().removeIf(event.song()::equals);
            repository.save(playlist);
        }
    }
}
