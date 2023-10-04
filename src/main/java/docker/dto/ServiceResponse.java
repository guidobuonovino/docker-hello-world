package docker.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceResponse extends SerializableDTO {

	private static final long serialVersionUID = 3663036354966852935L;

	@NotNull
	@JsonProperty("status")
	private Integer status;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@JsonProperty("message")
	private String message;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@JsonProperty("errors")
	private List<String> errors;

	public ServiceResponse(Integer status) {
		this.status = status;
	}

	public ServiceResponse(Integer status, String message) {
		this.status = status;
		this.message = message;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public void addError(String error) {
		if (CollectionUtils.isEmpty(this.errors))
			this.errors = new ArrayList<>();
		errors.add(error);
	}
}
