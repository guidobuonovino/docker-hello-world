package docker.exceptions;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import docker.dto.ServiceResponse;
import docker.utils.I18nHelper;

@ControllerAdvice
public class ExceptionHandlerComponent extends ResponseEntityExceptionHandler {

	private Logger log = LoggerFactory.getLogger(ExceptionHandlerComponent.class);

	@Autowired
	private I18nHelper i18n;

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<Object> exceptionResponse(Exception ex) {

		String exMessage = i18n.getMessage("error.unexpected_error");

		if (!StringUtils.isEmpty(ex.getMessage()))
			exMessage += ": " + ex.getMessage();

		ServiceResponse response = new ServiceResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		response.addError(exMessage);

		log.error(exMessage, ex);

		return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.valueOf(response.getStatus()));
	}

	@ExceptionHandler(value = { ConstraintViolationException.class })
	public ResponseEntity<Object> violationException(ConstraintViolationException ex) {

		ServiceResponse apiError = new ServiceResponse(HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.getReasonPhrase());

		if (ex.getMessage() != null) {
			String[] errMsg = ex.getMessage().split(", ");
			for (String err : errMsg) {
				apiError.addError(err);
				log.error("VALIDATION ERROR: {}", err);
			}
		}

		return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.valueOf(apiError.getStatus()));
	}

	@ExceptionHandler(value = { BusinessException.class })
	public ResponseEntity<Object> businessExceptionResponse(BusinessException ex) {

		String exMessage = i18n.getMessage(ex.getMessage());

		ServiceResponse response = new ServiceResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		response.addError(exMessage);

		log.error(exMessage, ex);

		return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.valueOf(response.getStatus()));
	}

	@ExceptionHandler(value = { BadRequestException.class })
	public ResponseEntity<Object> badRequestExceptionResponse(BadRequestException ex) {

		String exMessage = i18n.getMessage(ex.getMessage());

		ServiceResponse response = new ServiceResponse(HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.getReasonPhrase());
		response.addError(exMessage);

		log.error(exMessage, ex);

		return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.valueOf(response.getStatus()));
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

		log.info("--------------------------------------");

		ServiceResponse apiError = new ServiceResponse(HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.getReasonPhrase());

		for (FieldError fieldError : ex.getBindingResult().getFieldErrors())
			apiError.addError(fieldError.getField() + ": " + fieldError.getDefaultMessage());

		apiError.getErrors().forEach(err -> log.error("VALIDATION ERROR: {}", err));

		return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.valueOf(apiError.getStatus()));
	}

	@ExceptionHandler(value = { EntityNotFoundException.class })
	public ResponseEntity<Object> entityNotFoundExceptionExceptionResponse(EntityNotFoundException ex) {

		String exMessage = i18n.getMessage(ex.getMessage());

		ServiceResponse response = new ServiceResponse(HttpStatus.NOT_FOUND.value(),
				HttpStatus.NOT_FOUND.getReasonPhrase());
		response.addError(exMessage);

		log.error(exMessage, ex);

		return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.valueOf(response.getStatus()));
	}
}