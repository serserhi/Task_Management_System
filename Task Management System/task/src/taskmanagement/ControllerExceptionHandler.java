package taskmanagement;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler  {

    @ExceptionHandler({AccountNotFoundException.class, TaskNotFoundException.class})
    public ResponseEntity<CustomErrorMessage> handleAccountNotFound(
            Exception e, WebRequest request) {

        CustomErrorMessage body = new CustomErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                e.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(AssigneeNotValidException.class)
    public ResponseEntity<CustomErrorMessage> handleAssigneeNotValid(
            AssigneeNotValidException e, WebRequest request) {

        CustomErrorMessage body = new CustomErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                e.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountNotAuthorException.class)
    public ResponseEntity<CustomErrorMessage> handleAccountNotAuthor(
            AccountNotAuthorException e, WebRequest request) {

        CustomErrorMessage body = new CustomErrorMessage(
                HttpStatus.FORBIDDEN.value(),
                LocalDateTime.now(),
                e.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }


    //Para capturar los errores de validación tanto de request parameters, path variables e incluso
    //campos del request body


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorMessage> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e, WebRequest request) {

        // Collect all field error messages
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        CustomErrorMessage body = new CustomErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                errorMessage,
                request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    //Manejo de la excepción cuando uso un metodo no soportado como por ejemplo
    //PUT a /api/accounts
    //Más aquí: https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/mvc/method/annotation/ResponseEntityExceptionHandler.html

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<CustomErrorMessage> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException e, WebRequest request) {

        CustomErrorMessage body = new CustomErrorMessage(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                LocalDateTime.now(),
                "Request method " + e.getMethod() + " not supported (Custom Error Message)",
                request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }



}