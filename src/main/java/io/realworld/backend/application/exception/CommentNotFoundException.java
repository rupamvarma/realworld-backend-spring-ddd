package io.realworld.backend.application.exception;
public class CommentNotFoundException extends InvalidRequestException {

    public CommentNotFoundException(String message) {
        super(message);
    }
}
