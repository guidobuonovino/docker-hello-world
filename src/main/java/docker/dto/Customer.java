package docker.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
@Entity
@Table(name = "customers")
public class Customer  {

    //@GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Integer id;
    @Pattern(regexp = "[A-Za-z\\d\\s]" , message = "Incompatible passed value")
    private String lastName;
    private String firstName;
    private String email;
    private String company;
    private String phone;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    public Customer(){}
}
