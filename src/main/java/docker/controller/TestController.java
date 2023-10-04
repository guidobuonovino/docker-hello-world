package docker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import docker.dto.ServiceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Validated
@Tag(name = "TestController", description = "API di test")
public class TestController {

	// @formatter:off
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ServiceResponse.class)), description = "OK"),
		@ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ServiceResponse.class)), description = "Bad Request"),
		@ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ServiceResponse.class)), description = "Not Found"),
		@ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ServiceResponse.class)), description = "Internal Server Error")}
	)
	@Operation(	extensions = @Extension(properties = @ExtensionProperty(name = "x-auth-type", value = "none")), // da aggiungere per ogni metodo
				summary = "testService",
				description = "Servizio di test")
	@GetMapping(value = "/test")
	public ResponseEntity<ServiceResponse> testMethod(

			@Parameter(required = false, description = "Test input param")
			@RequestParam(name = "test-input-param", required = false) String testInputParam

	// @formatter:on
	) {
		ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.OK.value());
		serviceResponse.setMessage("The test input param is " + testInputParam);

		return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
	}
}