package bertcoscia.ZiplyEats_BE.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
    public NotFoundException(UUID id) {super("Could not find resource with id " + id);}
}
