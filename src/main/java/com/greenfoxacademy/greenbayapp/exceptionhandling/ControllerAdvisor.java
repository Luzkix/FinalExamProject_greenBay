package com.greenfoxacademy.greenbayapp.exceptionhandling;

import com.greenfoxacademy.greenbayapp.exceptionhandling.DTO.ErrorDTO;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers,
                                                                HttpStatus status, WebRequest request) {

    List<FieldError> errors = ex.getBindingResult().getFieldErrors();
    //if more errors, create one message which consists of all the errors
    if (errors.size() > 1) {
      return new ResponseEntity<>(new ErrorDTO(createTextFromFieldErrors(errors)), HttpStatus.BAD_REQUEST);
    }
    //individually specify messages/statuses for errors where i want http status to be different from .BAD_REQUEST
    if (errors.get(0).getDefaultMessage().equals("Password must consist between 8-12 characters!")) {
      return new ResponseEntity<>(new ErrorDTO(errors.get(0).getDefaultMessage()), HttpStatus.NOT_ACCEPTABLE);
    }
    //otherwise HttpStatus.BAD_REQUEST will be returned with default message
    return new ResponseEntity<>(new ErrorDTO(errors.get(0).getDefaultMessage()), HttpStatus.BAD_REQUEST);
  }

  private String createTextFromFieldErrors(List<FieldError> errors) {
    String result = errors.stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .sorted() //it sorts the errors based on the first letter of error message. Otherwise the order is random.
        .map(each -> each + " ")
        .collect(Collectors.joining());

    return result.substring(0,result.length()-1);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorDTO> handleExceptions(RuntimeException ex) {
    HttpStatus status = HttpStatus.UNAUTHORIZED;

    if (ex.getMessage().equals("Username or email is already taken!")) status = HttpStatus.CONFLICT;
    if (ex.getMessage().equals("Username or password is incorrect!")) status = HttpStatus.UNAUTHORIZED;

    log.error(ex.getMessage());
    return new ResponseEntity<>(new ErrorDTO(ex.getMessage()), status);
  }
}
