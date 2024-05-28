package it.kristianp.footballbackendwebapp.auth.exception.handler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import it.kristianp.footballbackendwebapp.auth.exception.UserAlreadyExistsException;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception exception) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String description = "Internal server error.";

        if (exception instanceof UserAlreadyExistsException) {
            status = HttpStatus.BAD_REQUEST;
            description = exception.getMessage();
        }
        if (exception instanceof BadCredentialsException) {
            status = HttpStatus.UNAUTHORIZED;
            description = "The username or password is incorrect";
        }
        if (exception instanceof AccountStatusException) {
            status = HttpStatus.FORBIDDEN;
            description = "The account is locked";
        }
        if (exception instanceof AccessDeniedException) {
            status = HttpStatus.FORBIDDEN;
            description = "You are not authorized to access this resource";
        }
        if (exception instanceof SignatureException) {
            status = HttpStatus.FORBIDDEN;
            description = "The JWT signature is invalid";
        }
        if (exception instanceof ExpiredJwtException) {
            status = HttpStatus.FORBIDDEN;
            description = "The JWT token has expired";
        }

        ProblemDetail errorDetail = ProblemDetail.forStatus(status);
        errorDetail.setProperty("description", description);

        return errorDetail;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, getErrors(ex));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    private static String getErrors(BindException ex) {
        return Optional.of(ex.getBindingResult())
                .map(bindingResult -> bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.toList()))
                .orElseGet(Collections::emptyList)
                .toString();
    }
}