package fotius.example.musicapp.presentation.rest;

import fotius.example.musicapp.domain.SongService;
import fotius.example.musicapp.domain.model.Song;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/songs")
@RequiredArgsConstructor
public class SongController {
    private final SongService songService;

    @PostMapping
    public ResponseEntity<Song> create(@RequestBody Song song) {
        Song createdSong = songService.create(SongService.CreateSongCommand.builder()
                .name(song.getName())
                .artist(song.getArtist())
                .location(song.getLocation())
                .lyrics(song.getLyrics())
                .build());
        return ResponseEntity.created(URI.create("/api/songs/" + song.getId())).body(createdSong);
    }

    @GetMapping("{id}")
    public Song getById(@PathVariable("id") long id) {
        return songService.getById(id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") long id) {
        songService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{id}")
    public Song updateById(@PathVariable("id") long id, @RequestBody Song song) {
        return songService.update(id, SongService.UpdateSongCommand.builder()
                .location(song.getLocation())
                .addedLikes(song.getLikes())
                .lyrics(song.getLyrics())
                .build());
    }
}
