package one.backbone.messagingassignment.exception;

import lombok.extern.slf4j.Slf4j;
import one.backbone.messagingassignment.model.dto.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

/**
 * The type User management exception handler.
 */
@Slf4j
@RestControllerAdvice
public class UserManagementExceptionHandler {

    /**
     * Handle t user not found exception base response.
     *
     * @param ex      the ex
     * @param request the request
     * @return the base response
     */
    @ExceptionHandler(value = UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseResponse handleTUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        return BaseResponse.builder()
                .responseStatus(HttpStatus.NOT_FOUND.value())
                .responseMessage(ex.getMessage())
                .responseTimestamp(Instant.now())
                .build();
    }

    @ExceptionHandler(value = UsernameAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public BaseResponse handleUsernameAlreadyExistException(UsernameAlreadyExistException ex, WebRequest request) {
        return BaseResponse.builder()
                .responseStatus(HttpStatus.CONFLICT.value())
                .responseMessage(ex.getMessage())
                .responseTimestamp(Instant.now())
                .build();
    }

}
