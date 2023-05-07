package fotius.example.musicapp.domain;

import fotius.example.musicapp.domain.error.InvalidRequestException;
import fotius.example.musicapp.domain.error.SongNotFoundException;
import fotius.example.musicapp.domain.model.Song;
import fotius.example.musicapp.domain.persistence.SongRepository;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SongService {

    @Data
    @Builder
    @AllArgsConstructor
    public static class CreateSongCommand {

        @NotBlank(message = "Name is required to be provided")
        @Size(max = 100, message = "Name must not be longer than 100 chars")
        private String name;

        @NotBlank(message = "Arist is required to be provided")
        @Size(max = 50, message = "Artist must not be longer than 50 chars")
        private String artist;

        @NotBlank(message = "Location is required to be provided")
        @Size(max = 255, message = "Location must not be longer than 255 chars")
        private String location;

        @Nullable
        private String lyrics;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class UpdateSongCommand {

        @Nullable
        @Size(max = 255, message = "Location must not be longer than 255 chars")
        private String location;

        @Nullable
        @Min(value = 1)
        private Integer addedLikes;

        @Nullable
        private String lyrics;
    }

    private final SongRepository repository;
    private final ApplicationEventPublisher eventPublisher;
    private final Validator validator;

    @Transactional
    public Song create(CreateSongCommand command) {
        final var violations = validator.validate(command);
        if (!violations.isEmpty()) {
            throw new InvalidRequestException("Failed to create song", violations);
        }
        final var song = Song.builder()
            .name(command.name)
            .artist(command.artist)
            .location(command.location)
            .addedAt(LocalDateTime.now())
            .likes(0)
            .lyrics(command.lyrics)
            .build();
        repository.save(song);
        return song;
    }

    @Transactional
    public Song update(Long songId, UpdateSongCommand command) {
        final var violations = validator.validate(command);
        if (!violations.isEmpty()) {
            throw new InvalidRequestException("Failed to update song '%d'".formatted(songId), violations);
        }
        final Song song = repository.findById(songId).orElseThrow(() -> new SongNotFoundException(songId));
        if (command.lyrics != null) {
            song.setLyrics(command.lyrics);
        }
        if (command.location != null) {
            song.setLocation(command.location);
        }
        if (command.addedLikes != null) {
            song.setLikes(song.getLikes() + command.addedLikes);
        }
        repository.save(song);
        return song;
    }

    @Transactional
    public void delete(Long songId) {
        final Optional<Song> song = repository.findById(songId);
        if (song.isPresent()) {
            eventPublisher.publishEvent(new BeforeSongIsDeleted(song.get()));
            repository.delete(song.get());
        }
    }

    public Song getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new SongNotFoundException(id));
    }

    public Slice<Song> findByArtistOrderedByAddedAt(
        String artist,
        int page,
        int perPage,
        Sort.Direction direction
    ) {
        return repository.findByArtist(
            artist,
            PageRequest.of(
                page,
                perPage,
                Sort.by(direction, "addedAt")
            )
        );
    }

    public Slice<Song> findAllOrderedByAddedAt(int page, int perPage, Sort.Direction direction) {
        return repository.findAll(
            PageRequest.of(
                page,
                perPage,
                Sort.by(direction, "addedAt")
            )
        );
    }
}
