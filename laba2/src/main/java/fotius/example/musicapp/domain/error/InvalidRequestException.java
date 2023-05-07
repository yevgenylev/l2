package fotius.example.musicapp.domain.error;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;

import java.util.Set;

public class InvalidRequestException extends MusicAppException {

    @Getter
    private final Set<? extends ConstraintViolation<?>> violations;

    public InvalidRequestException(String message, Set<? extends ConstraintViolation<?>> violations) {
        super(message);
        this.violations = violations;
    }
}
