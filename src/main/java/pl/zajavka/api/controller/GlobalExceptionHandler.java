package pl.zajavka.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import pl.zajavka.domain.exception.AlreadyExistException;
import pl.zajavka.domain.exception.NotFoundException;
import pl.zajavka.domain.exception.UpdatingCancelledVisitException;
import pl.zajavka.domain.exception.WrongStatusException;

import java.util.Optional;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        String message = String.format("Other exception occurred: [%s]", ex.getMessage());
        log.error(message, ex);
        ModelAndView modelView = new ModelAndView("error");
        modelView.addObject("errorMessage", message);
        return modelView;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNoResourceFound(NotFoundException ex) {
        String message = String.format("Could not find a resource: [%s]", ex.getMessage());
        log.error(message, ex);
        ModelAndView modelView = new ModelAndView("error");
        modelView.addObject("errorMessage", message);
        return modelView;
    }

    @ExceptionHandler(AlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleException(AlreadyExistException ex) {
        String message = String.format("Resource already exist : [%s]", ex.getMessage());
        log.error(message, ex);
        ModelAndView modelView = new ModelAndView("error");
        modelView.addObject("errorMessage", message);
        return modelView;
    }
        @ExceptionHandler(UpdatingCancelledVisitException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ModelAndView handleException (UpdatingCancelledVisitException ex){
            String message = String.format("Visit is cancelled : [%s]", ex.getMessage());
            log.error(message, ex);
            ModelAndView modelView = new ModelAndView("error");
            modelView.addObject("errorMessage", message);
            return modelView;
        }
        @ExceptionHandler(WrongStatusException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ModelAndView handleException (WrongStatusException ex){
            String message = String.format("Wrong status : [%s]", ex.getMessage());
            log.error(message, ex);
            ModelAndView modelView = new ModelAndView("error");
            modelView.addObject("errorMessage", message);
            return modelView;
        }

        @ExceptionHandler(BindException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ModelAndView handleException (BindException ex){
            String message = String.format("Bad request for field: [%s], wrong value: [%s]",
                    Optional.ofNullable(ex.getFieldError()).map(FieldError::getField).orElse(null),
                    Optional.ofNullable(ex.getFieldError()).map(FieldError::getRejectedValue).orElse(null));
            log.error(message, ex);
            ModelAndView modelView = new ModelAndView("error");
            modelView.addObject("errorMessage", message);
            return modelView;
        }
    }
