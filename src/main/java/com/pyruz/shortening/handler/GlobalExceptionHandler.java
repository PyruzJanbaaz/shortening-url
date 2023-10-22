package com.pyruz.shortening.handler;

import com.pyruz.shortening.model.dto.base.BaseDTO;
import com.pyruz.shortening.model.dto.base.MetaDTO;
import com.pyruz.shortening.model.dto.base.ServiceExceptionDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
    final ApplicationProperties applicationProperties;
    final ApplicationMessages applicationMessages;

    public GlobalExceptionHandler(ApplicationProperties applicationProperties, ApplicationMessages applicationMessages) {
        this.applicationProperties = applicationProperties;
        this.applicationMessages = applicationMessages;
    }

    // --> ServiceLevelValidation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        BaseDTO baseDTO = new BaseDTO();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            String errorMessage = applicationProperties.getProperty("application.message..general.validation.error.text");
            Integer errorCode = HttpStatus.BAD_REQUEST.value();
            String fieldName = error.getField();
            String validationErrorType = (Objects.requireNonNull(error.getCodes()).length == 4) ? (Objects.requireNonNull(error.getCodes()))[3] : null;
            String sizeError = "";
            if (validationErrorType != null) {
                switch (validationErrorType) {
                    case "NotBlank":
                        errorMessage = applicationProperties.getProperty("application.message.missing.parameter.text");
                        break;
                    case "Min":
                        errorMessage = applicationProperties.getProperty("application.message.invalid.min.length.text");
                        sizeError = ((Objects.requireNonNull(error.getArguments())).length == 2) ? (Objects.requireNonNull(error.getArguments()))[1].toString() : "";
                        break;
                    case "Max":
                        errorMessage = applicationProperties.getProperty("application.message.invalid.max.length.text");
                        sizeError = ((Objects.requireNonNull(error.getArguments())).length == 2) ? (Objects.requireNonNull(error.getArguments()))[1].toString() : "";
                        break;
                    case "URL":
                        errorMessage = applicationProperties.getProperty("application.message.validation.error.text");
                        sizeError = ((Objects.requireNonNull(error.getArguments())).length == 6) ? (Objects.requireNonNull(error.getArguments()))[1].toString() : "";
                        break;
                    case "Size":
                        errorMessage = applicationProperties.getProperty("application.message.invalid.length.text");
                        if ((Objects.requireNonNull(error.getArguments())).length == 3) {
                            Integer maxValue = TypesHelper.tryParseInt((Objects.requireNonNull(error.getArguments()))[1]);
                            Integer minValue = TypesHelper.tryParseInt((Objects.requireNonNull(error.getArguments()))[2]);
                            if (minValue != 0) {
                                sizeError = applicationProperties.getProperty("application.message.less") + " " + minValue + " ";
                            }
                            if (maxValue != Integer.MAX_VALUE) {
                                if (!sizeError.isEmpty())
                                    sizeError = String.format("%s %s ", sizeError, applicationProperties.getProperty("application.message.and"));
                                sizeError += applicationProperties.getProperty("application.message.more") + " " + maxValue + " ";
                            }
                        }
                        break;
                    default:
                        sizeError = "";
                        break;
                }
                MetaDTO metaDTO = new MetaDTO(errorCode, String.format(errorMessage, fieldName, sizeError));
                baseDTO.setMeta(metaDTO);
                break;
            }
        }
        return new ResponseEntity<>(baseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public final ResponseEntity<BaseDTO> handleMissingParameterException(MissingServletRequestParameterException ex) {
        MetaDTO metaDTO = MetaDTO.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(
                        String.format(
                                applicationMessages.getProperty("application.message.missing.parameter.text"),
                                ex.getParameterName()
                        )
                ).build();
        return new ResponseEntity<>(BaseDTO.builder().meta(metaDTO).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<BaseDTO> handleConstraintViolationException(ConstraintViolationException ex) {
        String errorMessage = applicationMessages.getProperty("application.message..general.validation.error.text");
        for (ConstraintViolation<?> error : ex.getConstraintViolations()) {
            if(!error.getMessage().isEmpty()) {
                errorMessage = String.format(
                        applicationMessages.getProperty("application.message.validation.error.text"),
                        ((PathImpl) error.getPropertyPath()).getLeafNode().getName()
                );
                break;
            }
        }
        MetaDTO metaDTO = MetaDTO.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage).build();
        return new ResponseEntity<>(BaseDTO.builder().meta(metaDTO).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public Object handleNoHandlerFoundException(NoHandlerFoundException ex) {
        BaseDTO baseDTO = BaseDTO.builder()
                .meta(
                        MetaDTO.builder()
                                .code(HttpStatus.BAD_REQUEST.value())
                                .message(ex.getMessage())
                                .build()
                )
                .build();
        return new ResponseEntity<>(baseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public Object handleGlobalException(Exception ex) {
        BaseDTO baseDTO = BaseDTO.builder()
                .meta(
                        MetaDTO.builder()
                                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .message(ex.getMessage())
                                .build()
                )
                .build();
        return new ResponseEntity<>(baseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ServiceExceptionDTO.class)
    public final ResponseEntity<BaseDTO> handleServiceException(ServiceExceptionDTO ex) {
        BaseDTO baseDTO = BaseDTO.builder()
                .meta(
                        MetaDTO.builder()
                                .code(ex.getCode())
                                .message(ex.getMessage())
                                .build()
                )
                .build();
        HttpStatus httpStatus = ex.getCode() == null ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.resolve(ex.getCode());
        assert httpStatus != null;
        return new ResponseEntity<>(baseDTO, httpStatus);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public Object handleHttpClientErrorException(HttpClientErrorException ex) {
        BaseDTO baseDTO = BaseDTO.builder()
                .meta(
                        MetaDTO.builder()
                                .code(ex.getStatusCode().value())
                                .message(ex.getStatusText())
                                .build()
                )
                .build();
        return new ResponseEntity<>(baseDTO, ex.getStatusCode());
    }
}
