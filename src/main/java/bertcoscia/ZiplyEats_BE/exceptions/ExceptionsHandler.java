package bertcoscia.ZiplyEats_BE.exceptions;

import bertcoscia.ZiplyEats_BE.payloads.responses.ErrorsRespDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorsRespDTO handleBadRequest(BadRequestException ex) {
        return new ErrorsRespDTO(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
    public ErrorsRespDTO handleUnauthorized(UnauthorizedException ex) {
        return new ErrorsRespDTO(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) // 403
    public ErrorsRespDTO handleForbidden(AuthorizationDeniedException ex) {
        return new ErrorsRespDTO("You do not have the permission to perform the requested action", LocalDateTime.now());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    public ErrorsRespDTO handleNotFound(NotFoundException ex) {
        return new ErrorsRespDTO(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    public ErrorsRespDTO handleGenericErrors(Exception ex) {
        ex.printStackTrace();
        return new ErrorsRespDTO("Server problem. We will work to fix it as soon as possible.", LocalDateTime.now());
    }

}
