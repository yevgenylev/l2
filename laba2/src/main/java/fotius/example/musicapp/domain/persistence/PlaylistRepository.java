package fotius.example.musicapp.domain.persistence;

import fotius.example.musicapp.domain.model.Playlist;
import fotius.example.musicapp.domain.model.Song;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    Slice<Playlist> findByAuthor(String author, Pageable pageable);

    List<Playlist> findBySongs(Song song);
}
