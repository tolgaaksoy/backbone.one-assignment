package one.backbone.messagingassignment.exception;


import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import one.backbone.messagingassignment.model.dto.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class ConstraintsValidationHandler {

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        return BaseResponse.builder()
                .responseStatus(HttpStatus.BAD_REQUEST.value())
                .responseMessage(ex.getMessage())
                .responseTimestamp(Instant.now())
                .build();
    }

}
