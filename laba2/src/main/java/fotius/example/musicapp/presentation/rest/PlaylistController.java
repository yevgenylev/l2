package fotius.example.musicapp.presentation.rest;

import fotius.example.musicapp.domain.PlaylistService;
import fotius.example.musicapp.domain.model.Playlist;
import fotius.example.musicapp.domain.model.PlaylistPage;
import fotius.example.musicapp.domain.model.Song;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
public class PlaylistController {
    private final PlaylistService playlistService;

    @PostMapping
    public ResponseEntity<Playlist> create(@RequestBody Playlist playlist) {
        Playlist createdPlaylist = playlistService.create(PlaylistService.CreatePlaylistCommand.builder()
                .name(playlist.getName())
                .author(playlist.getAuthor())
                .build());
        return ResponseEntity.created(URI.create("/api/playlists/" + createdPlaylist.getId()))
                .body(createdPlaylist);
    }

    @PostMapping("{id}/songs/{songId}")
    public Playlist addSong(@PathVariable("id") long id, @PathVariable("songId") long songId) {
        return playlistService.addSong(id, songId);
    }

    @DeleteMapping("{id}/songs/{songId}")
    public ResponseEntity<Playlist> deleteSong(@PathVariable("id") long id, @PathVariable("songId") long songId) {
        Playlist playlist = playlistService.deleteSong(id, songId);
        return ResponseEntity.ok(playlist);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") long id) {
        playlistService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<Playlist> getById(@PathVariable("id") long id) {
        Playlist playlist = playlistService.getById(id);
        return ResponseEntity.ok(playlist);
    }

    @GetMapping()
    public PlaylistPage getAllSorted(
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "sort_type", defaultValue = "ASC") Sort.Direction sort_direction,
            @RequestParam(value = "page", defaultValue = "0") int page) {
        int PAGE_SIZE = 10;
        Slice<Playlist> playlists = author == null ?
                playlistService.findAllOrderedByCreatedAt(page, PAGE_SIZE, sort_direction) :
                playlistService.findByAuthorOrderedByCreatedAt(author, page, PAGE_SIZE, sort_direction);
        UriComponents url = UriComponentsBuilder.fromPath("/api/playlists/page")
                .queryParam("author", author)
                .queryParam("sort_type", sort_direction)
                .queryParam("page", "{page}")
                .build();
        return PlaylistPage.builder()
                .content(playlists.getContent())
                .nextPageUrl(playlists.hasNext() ? url.expand(page + 1).toUriString() : null)
                .previousPageUrl(playlists.hasPrevious() ? url.expand(page - 1).toUriString() : null)
                .pageNumber(page)
                .build();
    }
}
