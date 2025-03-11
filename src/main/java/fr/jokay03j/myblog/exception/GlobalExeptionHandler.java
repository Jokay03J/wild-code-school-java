package fr.jokay03j.myblog.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExeptionHandler {
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Error> handleResourceNotFound(ResourceNotFoundException exception, HttpServletRequest request) {
    Map<String, String> errors = new HashMap<>();
    return new ResponseEntity<Error>(
        new Error(exception, HttpStatus.NOT_FOUND, request.getRequestURI(), errors),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Error> handleUnknown(Exception exception, HttpServletRequest request) {
    Map<String, String> errors = new HashMap<>();
    return new ResponseEntity<Error>(
        new Error(exception, HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI(), errors),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Error> handleValidationExceptions(MethodArgumentNotValidException ex,
      HttpServletRequest request) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return new ResponseEntity<Error>(
        new Error(ex, HttpStatus.BAD_REQUEST, request.getRequestURI(), errors),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<Error> handleBadRequest(BadRequestException exception, HttpServletRequest request) {
    Map<String, String> errors = new HashMap<>();
    return new ResponseEntity<Error>(
        new Error(exception, HttpStatus.BAD_REQUEST, request.getRequestURI(), errors),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Error> handleNotFound(NotFoundException exception, HttpServletRequest request) {
    Map<String, String> errors = new HashMap<>();
    return new ResponseEntity<Error>(
        new Error(exception, HttpStatus.NOT_FOUND, request.getRequestURI(), errors),
        HttpStatus.NOT_FOUND);
  }
}

class Error {
  private String message;
  private HttpStatus status;
  private String path;
  private int code;
  private Map<String, String> errors;
  private StackTraceElement[] stackTrace;

  public Error(Exception exception, HttpStatus status, String path, Map<String, String> errors) {
    this.message = exception.getMessage();
    this.stackTrace = exception.getStackTrace();
    this.status = status;
    this.code = status.value();
    this.path = path;
    this.errors = errors != null ? errors : new HashMap<>();
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public StackTraceElement[] getStackTrace() {
    return stackTrace;
  }

  public void setStackTrace(StackTraceElement[] stackTrace) {
    this.stackTrace = stackTrace;
  }

  public Map<String, String> getErrors() {
    return errors;
  }

  public void setErrors(Map<String, String> errors) {
    this.errors = errors;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public void setStatus(HttpStatus status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}