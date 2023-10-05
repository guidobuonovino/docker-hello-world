package docker.repository.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import docker.dto.Customer;

public interface ICustomerRepo extends JpaRepository<Customer, Integer> ,JpaSpecificationExecutor<Customer>, PagingAndSortingRepository<Customer, Integer> {

	@Query("SELECT c FROM Customer c")
	public Page<Customer> findAllByCustomQuery(Pageable p);
	
	@Query("SELECT c FROM Customer c WHERE firstName = :firstName AND lastName = :lastName")
	public Page<Customer> findAllByCustomQueryUserParam(Pageable p, @Param("lastName") String lastName, @Param("firstName") String firstName);

	public Page<Customer> findAllByLastNameAndFirstName(Pageable p, String lastName, String firstName);
	
}
