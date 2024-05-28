package it.kristianp.footballbackendwebapp.auth.exceptionhandler;

import org.springframework.http.*;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Deprecated
//@RestControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

//    @ExceptionHandler({AuthenticationException.class})
//    @ResponseBody
//    public ProblemDetail handleAuthenticationException(AuthenticationException ex) {
//        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
//    }


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
