package fotius.example.musicapp.domain.persistence;

import fotius.example.musicapp.domain.model.Song;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    Slice<Song> findByArtist(String artist, Pageable pageable);
}
