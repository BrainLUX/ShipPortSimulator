package com.dmitriev.oop.exception;

public class InvalidJwtAuthenticationException extends RuntimeException{

  public InvalidJwtAuthenticationException(String message) {
    super(message);
  }
}
