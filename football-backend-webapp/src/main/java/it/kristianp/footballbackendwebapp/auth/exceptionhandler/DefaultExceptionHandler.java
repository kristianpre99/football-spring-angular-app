package it.kristianp.footballbackendwebapp.auth.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({AuthenticationException.class})
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleAuthenticationException(Exception ex) {

        String re = "Authentication failed at controller advice";
        Map<String, String> map = new HashMap<>();
        map.put("error", re);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
    }
}
