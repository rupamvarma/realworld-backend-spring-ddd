package io.realworld.backend.application.exception;

public class NoRoleFoundException extends InvalidRequestException {
  public NoRoleFoundException(String message) {
    super(message);
  }
}
