package fotius.example.musicapp.presentation.rest;

import fotius.example.musicapp.domain.error.InvalidRequestException;
import fotius.example.musicapp.domain.error.SongIsAlreadyInPlaylistException;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class CommonErrorHandler {

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiError> handleInvalidRequestException(InvalidRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                new ApiError(
                    ex.getMessage(),
                    LocalDateTime.now(),
                    ex.getViolations()
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .toList()
                )
            );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                new ApiError(
                    ex.getMessage(),
                    LocalDateTime.now(),
                    List.of()
                )
            );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception ex) {
        log.error("Failed to process request due to exception", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                new ApiError(
                    ex.getMessage(),
                    LocalDateTime.now(),
                    List.of()
                )
            );
    }

    @ExceptionHandler(SongIsAlreadyInPlaylistException.class)
    public ResponseEntity<ApiError> handleSongIsAlreadyInPlaylistException(SongIsAlreadyInPlaylistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(
                new ApiError(
                    ex.getMessage(),
                    LocalDateTime.now(),
                    List.of()
                )
            );
    }
}
