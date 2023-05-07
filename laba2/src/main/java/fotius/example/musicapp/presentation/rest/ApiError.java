package fotius.example.musicapp.presentation.rest;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ApiError {
    private String message;
    private LocalDateTime timestamp;
    private List<String> subErrors;
}
