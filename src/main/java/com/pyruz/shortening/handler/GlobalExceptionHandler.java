package com.pyruz.shortening.handler;

import com.pyruz.shortening.model.dto.base.ExceptionDTO;
import com.pyruz.shortening.model.enums.ValidationType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@ControllerAdvice
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    final MessageSource messageSource;

    @ExceptionHandler(CustomServiceException.class)
    public ResponseEntity<ExceptionDTO> serviceExceptionHandler(CustomServiceException ex) {
        return ResponseEntity.status(ex.status.value())
                .body(new ExceptionDTO(ex.status.getReasonPhrase(), Collections.singletonList(messageSource
                        .getMessage(ex.getMessage(), ex.getObjects(), Locale.ENGLISH))));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionDTO> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionDTO(HttpStatus.BAD_REQUEST.getReasonPhrase(), Collections.singletonList(ex.getMessage())));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionDTO> dataIntegrityViolationExceptionHandler(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionDTO(HttpStatus.BAD_REQUEST.getReasonPhrase(), Collections.singletonList(ex.getMessage())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> errorMessages = new ArrayList<>();
        if (!ex.getBindingResult().getFieldErrors().isEmpty()) {
            for (FieldError error : ex.getBindingResult().getFieldErrors()) {
                String fieldName = error.getField();
                String validationErrorType = (Objects.requireNonNull(error.getCodes()).length == 4)
                        ? (Objects.requireNonNull(error.getCodes()))[3] : null;
                if (validationErrorType != null) {
                    if (validationErrorType.equalsIgnoreCase(ValidationType.NOT_BLANK.toString())
                            || validationErrorType.equalsIgnoreCase(ValidationType.NOT_NULL.toString())
                            || validationErrorType.equalsIgnoreCase(ValidationType.NOT_EMPTY.toString())) {
                        errorMessages.add(messageSource.getMessage("application.message.missing.parameter",
                                new Object[] { fieldName }, Locale.ENGLISH));
                    }
                    if (validationErrorType.equalsIgnoreCase(ValidationType.MIN.toString())) {
                        errorMessages.add(messageSource.getMessage("application.message.invalid.min.length",
                                new Object[] { fieldName, (Objects.requireNonNull(error.getArguments()))[1] },
                                Locale.ENGLISH));
                    }
                    if (validationErrorType.equalsIgnoreCase(ValidationType.MAX.toString())) {
                        errorMessages.add(messageSource.getMessage("application.message.invalid.max.length",
                                new Object[] { fieldName, (Objects.requireNonNull(error.getArguments()))[1] },
                                Locale.ENGLISH));
                    }
                    if (validationErrorType.equalsIgnoreCase(ValidationType.PATTERN.toString())) {
                        errorMessages.add(messageSource.getMessage("application.message.invalid.parameter",
                                new Object[] { fieldName, (Objects.requireNonNull(error.getArguments()))[1] },
                                Locale.ENGLISH));
                    }
                    if (validationErrorType.equalsIgnoreCase(ValidationType.SIZE.toString())) {
                        String sizeError = null;
                        if ((Objects.requireNonNull(error.getArguments())).length == 3) {
                            int maxValue = Integer
                                    .parseInt((Objects.requireNonNull(error.getArguments()))[1].toString());
                            int minValue = Integer
                                    .parseInt((Objects.requireNonNull(error.getArguments()))[2].toString());
                            if (minValue != 0) {
                                sizeError = messageSource.getMessage("application.message.less", null,
                                        Locale.ENGLISH) + " " + minValue + " ";
                            }
                            if (maxValue != Integer.MAX_VALUE) {
                                if (sizeError != null)
                                    sizeError = String.format("%s %s ", sizeError,
                                            messageSource.getMessage("application.message.and", null,
                                                    Locale.ENGLISH));
                                sizeError += messageSource.getMessage("application.message.more", null,
                                        Locale.ENGLISH) + " " + maxValue + " ";
                            }
                        }
                        errorMessages.add(messageSource.getMessage("application.message.invalid.length",
                                new Object[] { fieldName, sizeError }, Locale.ENGLISH));
                    }
                }
            }
        } else if (!ex.getBindingResult().getAllErrors().isEmpty()) {
            var errors = ex.getBindingResult().getAllErrors().stream().toList();
            if (Arrays.stream(Objects.requireNonNull(errors.get(0).getCodes()))
                    .anyMatch(el -> el.toUpperCase().contains(ValidationType.DATE_RANGE.toString().replace("_", ""))))
                errorMessages.add(messageSource.getMessage("application.message.invalid.date.range", null,
                        Locale.ENGLISH));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionDTO(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorMessages));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ResponseEntity<ExceptionDTO> handleMissingParameterException(
            MissingServletRequestParameterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionDTO(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        Collections.singletonList(messageSource.getMessage("application.message.missing.parameter",
                                new Object[] { ex.getParameterName() }, Locale.ENGLISH))));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ResponseEntity<ExceptionDTO> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> errorMessages = new ArrayList<>();
        for (ConstraintViolation<?> error : ex.getConstraintViolations()) {
            if (!error.getMessage().isEmpty()) {
                errorMessages.add(messageSource.getMessage("application.message.invalid.parameter",
                        new Object[] { ((PathImpl) error.getPropertyPath()).getLeafNode().getName() },
                        Locale.ENGLISH));
            }
        }
        if (errorMessages.isEmpty()) {
            errorMessages.add(messageSource.getMessage("application.message.invalid.request.body", null,
                    Locale.ENGLISH));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionDTO(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorMessages));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionDTO(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        Collections.singletonList(messageSource.getMessage("application.message.invalid.request.body",
                                null, Locale.ENGLISH))));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionDTO> handleAllOtherExceptions(Exception ex) {
        String errorMessage = "An unexpected error occurred";
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), Collections.singletonList(errorMessage)));
    }
}
