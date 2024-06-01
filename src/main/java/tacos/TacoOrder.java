package tacos;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class TacoOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /* All other properties in TacoOrder will be mapped automatically to columns based on their property names
    e.g. deliveryName will be mapped to a column called delivery_name.
    HOWEVER, if you wanted to explicitly define the column name mapping, you would annotate the property with e.g.

    @Column("customer-delivery-name")
    private String deliveryName;
    */
    private Date placedAt = new Date();

    // Delivery Information

    @NotBlank(message = "Delivery name is required")
    private String deliveryName;
    @NotBlank(message = "Street is required")
    private String deliveryStreet;
    @NotBlank(message = "City is required")
    private String deliveryCity;
    @NotBlank(message = "State is required")
    private String deliveryState;
    @NotBlank(message = "Zip code is required")
    private String deliveryZip;

    // Payment Information
    @CreditCardNumber(message = "Not a valid Credit Card number") // ensures credit card number passes the Luhn algorithm check
    private String ccNumber;
    @Pattern(regexp="^(0[1-9]|1[0-2])([\\/])([2-9][0-9])$",
             message="Must be formatted MM/YY")
    private String ccExpiration;
    @Digits(integer = 3, fraction = 0, message = "Invalid CVV")
    private String ccCVV;

    /*the relationship to the list of Taco objects is annotated with @OneToMany, indicating that the tacos are all
    specific to this one order. Moreover, the cascade attribute is set to CascadeType.ALL so that if the order is
    deleted, its related tacos will also be deleted */
    @OneToMany(cascade = CascadeType.ALL)
    private List<Taco> tacos = new ArrayList<>();

    public void addTaco(Taco taco) {
        this.tacos.add(taco);
    }
}
