package pl.zajavka.domain.exception;

public class WrongStatusException extends RuntimeException{

    public WrongStatusException(final String message) {super(message);}
}
