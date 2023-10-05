package docker.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import docker.dto.Customer;
import docker.dto.ServiceResponse;
import docker.repository.impl.ICustomerRepo;
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

	@Autowired private ICustomerRepo customerRepo;

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

	@PostMapping(value = "/test/valid")
	public ResponseEntity<Object> testMethod1( @RequestBody Customer customer, Pageable pageable) {
		ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.OK.value());

		Page<Customer> pg = customerRepo.findAllByCustomQuery(pageable);
		pg = customerRepo.findAllByLastNameAndFirstName(pageable, "Hayes", "Rachel");
		pg = customerRepo.findAllByCustomQueryUserParam(pageable, "Hayes", "Rachel");
		//	      Page<Customer> pg = customerRepo.findAll(org.springframework.data.domain.Example.of(qry), pageable);

		Specification<Customer> spec = (root, query, cb) -> {

			Predicate predicate1 =  cb.equal(root.get("firstName"), "Rachel");
			Predicate pfinal = cb.and(predicate1);
			return pfinal;  
		};
		pg = customerRepo.findAll(spec, pageable);


		try (Connection connection = DriverManager
				.getConnection("jdbc:h2:mem:demo;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE", "sa", null);

				/***
	    		   {
 					"firstName":"' OR 1=1 OR first_name ='"
					}
				 */
				// Step 2:Create a statement using connection object

//				PreparedStatement preparedStatement = connection.prepareStatement("Select * from northwind.user where first_name = \'" + customer.getFirstName() + "\'")) {
				PreparedStatement preparedStatement = connection.prepareStatement("Select * from northwind.user where first_name = ?")) {
				preparedStatement.setString(1, customer.getFirstName());
					//	              preparedStatement.setInt(2, 1);

					// Step 3: Execute the query or update query
					ResultSet rs =preparedStatement.executeQuery();
					// Fetch each row from the result set
					List<String> ls = new ArrayList<>();
					while (rs.next()) {
						int i = rs.getInt("userid");
						String str = rs.getString("first_name");
						ls.add(str);
					}
					return new ResponseEntity<>(ls, HttpStatus.OK);
				} catch (Exception e) {

				}

				return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
	}
}